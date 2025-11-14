package game.keyInputManager;

import game.entities.Pacman;
import game.keyInputManager.command.Command;
import game.keyInputManager.command.LevelInputCommand;
import game.keyInputManager.command.NoCommand;
import game.keyInputManager.command.PacmanInputCommand;
import game.utils.KeyHandler;

public class KeyInputManager {
    private GameState gameState;
    private Command inputCommand;

    private final GameState playState;
    private final GameState pauseState;
    private Command pacmanInputCommand = new NoCommand();
    private Command levelInputCommand = new NoCommand();

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
