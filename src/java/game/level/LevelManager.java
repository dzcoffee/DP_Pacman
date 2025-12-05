package game.level;

import game.Observer;
import game.entities.PacGum;
import game.entities.SuperPacGum;
import game.entities.ghosts.Ghost;
import game.ghostStates.EatenMode;
import game.ghostStates.FrightenedMode;

import java.util.ArrayList;
import java.util.List;




public class LevelManager implements Observer,ILevelDataSubject, ILevelUpEventSubject {
    private final GameLevelData levelData;
    private final List<ILevelDataObserver> observerCollection;
    private final List<ILevelUpEventObserver> levelUpEventObserverCollection;


    private FrightenAllCommand frightenAllCommand;

    public LevelManager() {

        this.levelData = new GameLevelData();
        this.observerCollection = new ArrayList<>();
        this.levelUpEventObserverCollection = new ArrayList<>();
    }

    public void setFrightenAllCommand(FrightenAllCommand frightenAllCommand) {
        this.frightenAllCommand = frightenAllCommand;
    }

    public int getPacmanLife(){
        return levelData.getPacmanLife();
    }

    public void increasePacmanLife(){
        levelData.increasePacmanLife();
        notify(levelData);
    }

    public void decreasePacmanLife(){
        levelData.decreasePacmanLife();
        notify(levelData);


    }

    public void increasePacmanSpeed(){
        levelData.increasePacmanSpeed();
        notify(levelData);
    }

    public void decreasePacmanSpeed(){
        levelData.decreasePacmanSpeed();
        notify(levelData);
    }

    public void increaseGhostSpeed(){
        levelData.increaseGhostSpeed();
        notify(levelData);
    }

    public void decreaseGhostSpeed(){
        levelData.decreaseGhostSpeed();
        notify(levelData);
    }

    public void frightenAll(){
        frightenAllCommand.execute();
        notifyLevelUpEnd();

    }

    @Override
    public void registerObserver(ILevelDataObserver o) {
        observerCollection.add(o);
    }

    @Override
    public void removeObserver(ILevelDataObserver o) {
        observerCollection.remove(o);
    }

    @Override
    public void notify(GameLevelData data) {
        observerCollection.forEach(o -> o.updateLevelData(data));
        notifyLevelUpEnd();
    }

    @Override
    public void registerObserver(ILevelUpEventObserver observer) {
        levelUpEventObserverCollection.add(observer);
    }

    @Override
    public void removeObserver(ILevelUpEventObserver observer) {
        levelUpEventObserverCollection.remove(observer);
    }

    @Override
    public void notifyLevelUpEvent() {

    }

    @Override
    public void notifyLevelUpEnd() {
        levelUpEventObserverCollection.forEach(o -> o.updateLevelUpEnd());
    }

    @Override
    public void updatePacGumEaten(PacGum pg) {

    }

    @Override
    public void updateSuperPacGumEaten(SuperPacGum spg) {

    }

    @Override
    public void updateGhostCollision(Ghost gh) {
        if (gh.getState() instanceof FrightenedMode) {
            gh.getState().eaten(); //S'il existe une transition particulière quand le fantôme est mangé, son état change en conséquence
        }else if (!(gh.getState() instanceof EatenMode)) {
            // 게임 life 검사 로직 추가
            decreasePacmanLife();
            int pacmanLife = getPacmanLife();

            if(pacmanLife > 0){
                gh.getState().eaten();
            }
            else{
                exitGame();
            }
        }
    }

    protected void exitGame() {
        System.exit(0);
    }
}
