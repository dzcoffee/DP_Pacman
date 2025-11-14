package game.keyInputManager;

import game.entities.Pacman;
import game.keyInputManager.command.InputCommand;
import game.keyInputManager.command.LevelInputInputCommand;
import game.keyInputManager.command.NoInputCommand;
import game.keyInputManager.command.PacmanInputInputCommand;
import game.utils.KeyHandler;

public class KeyInputManager {
    private GameState gameState;
    private InputCommand inputCommand;

    private final GameState playState;
    private final GameState pauseState;
    private InputCommand pacmanInputInputCommand = new NoInputCommand();
    private InputCommand levelInputInputCommand = new NoInputCommand();

    public KeyInputManager(Pacman pacman, DummyLevelUIPannel levelUIPannel) {
        playState = new PlayState();
        pauseState = new PauseState();
        gameState = playState;

        pacmanInputInputCommand = new PacmanInputInputCommand(pacman);
        levelInputInputCommand = new LevelInputInputCommand(levelUIPannel);
        inputCommand = pacmanInputInputCommand;
    }


    public GameState getGameState() {return gameState;}

    public void switchPlayState() {
        gameState = playState;
        inputCommand = pacmanInputInputCommand;
    }
    public void switchPauseState() {
        gameState = pauseState;
        inputCommand = levelInputInputCommand;
    }

    public void input(KeyHandler k) {
        inputCommand.execute(k);
    }


}
