package game.keyInputManager;

import game.GameplayPanel;
import game.LevelUIPanel;
import game.entities.Pacman;
import game.keyInputManager.command.InputCommand;
import game.keyInputManager.command.LevelInputCommand;
import game.keyInputManager.command.NoInputCommand;
import game.keyInputManager.command.PacmanInputCommand;
import game.utils.KeyHandler;

public class KeyInputManager {
    private GameState gameState;

    private final GameState playState;
    private final GameState pauseState;


    public KeyInputManager(Pacman pacman, LevelUIPanel levelUIPannel) {
        playState = new PlayState(pacman);
        pauseState = new PauseState(levelUIPannel);
        gameState = playState;

    }


    public GameState getGameState() {return gameState;}

    public void switchPlayState() {
        gameState = playState;
    }
    public void switchPauseState() {
        gameState = pauseState;
    }

    public void input(KeyHandler k) {
        gameState.getInput(k);
    }


}
