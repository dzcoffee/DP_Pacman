package game;

import game.entities.*;

import game.entities.ghosts.Blinky;
import game.entities.ghosts.Ghost;
import game.ghostFactory.*;
import game.ghostStates.EatenMode;
import game.ghostStates.FrightenedMode;
import game.keyInputManager.KeyInputManager;
import game.ghostStates.GhostState;
import game.level.FrightenAllCommand;
import game.level.ILevelUpEventObserver;
import game.level.LevelManager;
import game.score.ScoreManager;
import game.utils.CollisionDetector;
import game.utils.CsvReader;
import game.utils.KeyHandler;
import game.utils.SoundManager;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



//Classe gérant le jeu en lui même
public class Game implements Observer, ILevelUpEventObserver, GameMediator {
    //Pour lister les différentes entités présentes sur la fenêtre
    private List<Entity> objects = new ArrayList();
    private List<Ghost> ghosts = new ArrayList();
    private static List<Wall> walls = new ArrayList();

    private static Pacman pacman;
    private static Blinky blinky;

    private SoundManager soundManager;
    private volatile boolean paused = false;
    private static boolean firstInput = false;

    private final KeyInputManager keyInputManager;
    private final Color[] portalColors = {Color.CYAN, Color.MAGENTA, Color.ORANGE};
    private boolean isAnyGhostInState(Class<? extends GhostState> ghostState) {
        for(Ghost gh: ghosts) {
            if(ghostState.isInstance(gh.getState())) {
                return true;
            }
        }
        return false;
    }

    private final LevelManager levelManager;
    private final ScoreManager scoreManager;

    public Game(){
        //Initialisation du jeu

        //Chargement du fichier csv du niveau
        List<List<String>> data = null;
        try {
            data = new CsvReader().parseCsv(getClass().getClassLoader().getResource("level/level.csv").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        int cellsPerRow = data.get(0).size();
        int cellsPerColumn = data.size();
        int cellSize = 8;

        CollisionDetector collisionDetector = new CollisionDetector(this);
        AbstractGhostFactory abstractGhostFactory = null;
        this.soundManager = new SoundManager();
        soundManager.gameStart();
        scoreManager = new ScoreManager();
        Map<String, List<TeleportZone>> portalMap = new HashMap<>(); // 포탈 저장용
        //Le niveau a une "grille", et pour chaque case du fichier csv, on affiche une entité parculière sur une case de la grille selon le caracère présent
        for(int xx = 0 ; xx < cellsPerRow ; xx++) {
            for(int yy = 0 ; yy < cellsPerColumn ; yy++) {
                String dataChar = data.get(yy).get(xx);
                if (dataChar.equals("x")) { //Création des murs
                    objects.add(new Wall(xx * cellSize, yy * cellSize));
                }else if (dataChar.equals("P")) { //Création de Pacman
                    pacman = new Pacman(xx * cellSize, yy * cellSize);
                    pacman.setCollisionDetector(collisionDetector);

                    //Enregistrement des différents observers de Pacman
//                    pacman.registerObserver(GameLauncher.getUIPanel());
                    pacman.registerObserver(scoreManager);
                    pacman.registerObserver(this);
                }else if (dataChar.equals("b") || dataChar.equals("p") || dataChar.equals("i") || dataChar.equals("c")) { //Création des fantômes en utilisant les différentes factories
                    switch (dataChar) {
                        case "b":
                            abstractGhostFactory = new BlinkyFactory();
                            break;
                        case "p":
                            abstractGhostFactory = new PinkyFactory();
                            break;
                        case "i":
                            abstractGhostFactory = new InkyFactory();
                            break;
                        case "c":
                            abstractGhostFactory = new ClydeFactory();
                            break;
                    }

                    Ghost ghost = abstractGhostFactory.makeGhost(xx * cellSize, yy * cellSize);
                    ghost.setMediator(this);
                    ghost.setCollisionDetector(collisionDetector);
                    ghosts.add(ghost);
                    if (dataChar.equals("b")) {
                        blinky = (Blinky) ghost;
                    }
                }else if (dataChar.equals(".")) { //Création des PacGums
                    objects.add(new PacGum(xx * cellSize, yy * cellSize));
                }else if (dataChar.equals("o")) { //Création des SuperPacGums
                    objects.add(new SuperPacGum(xx * cellSize, yy * cellSize));
                }else if (dataChar.equals("-")) { //Création des murs de la maison des fantômes
                    objects.add(new GhostHouse(xx * cellSize, yy * cellSize));
                }else if (dataChar.matches("\\d+")) {
                    TeleportZone tz = new TeleportZone(xx * cellSize, yy * cellSize);
                    portalMap.computeIfAbsent(dataChar, k -> new ArrayList<>()).add(tz);
                }
            }
        }
        int colorIndex = 0;
        for (String key : portalMap.keySet()) {
            List<TeleportZone> group = portalMap.get(key);
            if (group.size() == 2) {
                TeleportZone p1 = group.get(0);
                TeleportZone p2 = group.get(1);
                p1.setPartner(p2);
                p2.setPartner(p1);
                Color pairColor = portalColors[colorIndex % portalColors.length];
                p1.setColor(pairColor);
                p2.setColor(pairColor);
                objects.add(p1);
                objects.add(p2);
                colorIndex++;
            } else {
                System.err.println("경고: 포탈 ID " + key + "의 개수가 " + group.size() + "개입니다. (2개여야 함)");
            }
        }

        objects.add(pacman);
        objects.addAll(ghosts);

        for (Entity o : objects) {
            if (o instanceof Wall) {
                walls.add((Wall) o);
            }
        }

        keyInputManager = new KeyInputManager(pacman, GameLauncher.getLevelUIPanel());

        //Level Manager
        levelManager = new LevelManager(new FrightenAllCommand(this));
        registerLevelManager();
        registerScoreManager();
        GameLauncher.addLevelMangerInLevelUIPanel(levelManager);

    }

    // 레벨매니저 구독
    private void registerLevelManager(){
        levelManager.registerObserver(pacman);
        for(Ghost ghost : ghosts){
            levelManager.registerObserver(ghost);
        }
        levelManager.registerObserver(this);
        levelManager.registerObserver(keyInputManager);
        levelManager.registerObserver(GameLauncher.getStatusUIPanel());
    }

    private void registerScoreManager(){
        scoreManager.registerObserver(this);
        scoreManager.registerObserver(keyInputManager);
        scoreManager.setUIPanel(GameLauncher.getUIPanel());
    }

    public void eatGhostAll(){
        for(Ghost ghost : ghosts){
            ghost.getState().eaten();
        }
    }

    public static List<Wall> getWalls() {
        return walls;
    }

    public List<Entity> getEntities() {
        return objects;
    }

    //Mise à jour de toutes les entités
    public void update() {
        if (paused) return;
        for (Entity o: objects) {
            if (!o.isDestroyed()) o.update();
        }
    }

    //Gestion des inputs
    public void input(KeyHandler k) {
        keyInputManager.input(k);
    }

    //Rendu de toutes les entités
    public void render(Graphics2D g) {
        for (Entity o: objects) {
            if (!o.isDestroyed()) o.render(g);
        }
    }

    public static Pacman getPacman() {
        return pacman;
    }
    public static Blinky getBlinky() {
        return blinky;
    }

    //Le jeu est notifiée lorsque Pacman est en contact avec une PacGum, une SuperPacGum ou un fantôme
    @Override
    public void updatePacGumEaten(PacGum pg) {
        pg.destroy(); //La PacGum est détruite quand Pacman la mange
        soundManager.playPacGumSound();
    }

    @Override
    public void updateSuperPacGumEaten(SuperPacGum spg) {
        spg.destroy(); //La SuperPacGum est détruite quand Pacman la mange
        for (Ghost gh : ghosts) {
            gh.getState().superPacGumEaten(); //S'il existe une transition particulière quand une SuperPacGum est mangée, l'état des fantômes change
        }
    }

    @Override
    public void updateGhostCollision(Ghost gh) {
        if (gh.getState() instanceof FrightenedMode) {
            gh.getState().eaten(); //S'il existe une transition particulière quand le fantôme est mangé, son état change en conséquence
        }else if (!(gh.getState() instanceof EatenMode)) {
            // 게임 life 검사 로직 추가
            levelManager.decreasePacmanLife();
            int pacmanLife = levelManager.getPacmanLife();

            if(pacmanLife > 0){
                gh.getState().eaten();
            }
            else{
                //게임 종료 로직
//                System.out.println("Game over !\nScore : " + GameLauncher.getUIPanel().getScore()); //Quand Pacman rentre en contact avec un Fantôme qui n'est ni effrayé, ni mangé, c'est game over !
                System.exit(0); //TODO
            }



        }
    }

    public static void setFirstInput(boolean b) {
        firstInput = b;
    }

    public static boolean getFirstInput() {
        return firstInput;
    }

    @Override
    public void notify(GameColleague colleague, GameEvent event) {
        if (colleague instanceof Ghost) {
            if (event == GameEvent.GHOST_STATE_CHANGED_TO_FRIGHTENED) {
                soundManager.playFrightLoop();
            }
            if (event == GameEvent.GHOST_TIMER_OVER) {
                if (!isAnyGhostInState(FrightenedMode.class)) soundManager.stopFrightLoop();
            }
            else if (event == GameEvent.GHOST_STATE_CHANGED_TO_EATEN) {
                soundManager.playEatenLoop();
            }
            else if (event == GameEvent.GHOST_ARRIVED_HOME) {
                if (!isAnyGhostInState(EatenMode.class)) soundManager.stopEatenLoop();
            }
        }


    }

    @Override
    public void updateLevelUpEvent() {
        paused = true;
    }
    @Override
    public void updateLevelUpEnd() {
        paused = false;
    }
}
