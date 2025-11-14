package game.keyInputManager;

import game.entities.Pacman;
import game.keyInputManager.command.InputCommand;
import game.keyInputManager.command.LevelInputCommand;
import game.keyInputManager.command.NoInputCommand;
import game.keyInputManager.command.PacmanInputCommand;
import game.utils.KeyHandler;

public class KeyInputManager {
    private GameState gameState;
    private InputCommand inputCommand;

    private final GameState playState;
    private final GameState pauseState;
    private InputCommand pacmanInputCommand = new NoInputCommand();
    private InputCommand levelInputCommand = new NoInputCommand();

    public KeyInputManager(Pacman pacman, DummyLevelUIPannel levelUIPannel) {
        playState = new PlayState();
        pauseState = new PauseState();
        gameState = playState;

        pacmanInputCommand = new PacmanInputCommand(pacman);
        levelInputCommand = new LevelInputCommand(levelUIPannel);
        inputCommand = pacmanInputCommand;
    }


    public GameState getGameState() {return gameState;}

    public void switchPlayState() {
        gameState = playState;
        inputCommand = pacmanInputCommand;
    }
    public void switchPauseState() {
        gameState = pauseState;
        inputCommand = levelInputCommand;
    }

    public void input(KeyHandler k) {
        inputCommand.execute(k);
    }


}
