package game.keyInputManager;

import game.LevelUIPanel;
import game.entities.Pacman;
import game.keyInputManager.command.LevelInputCommand;
import game.keyInputManager.command.PacmanInputCommand;
import game.level.ILevelUpEventObserver;
import game.utils.KeyHandler;

public class KeyInputManager implements ILevelUpEventObserver {
    private GameState gameState;

    private final GameState playState;
    private final GameState pauseState;


    public KeyInputManager(Pacman pacman, LevelUIPanel levelUIPanel) {
        playState = new PlayState(new PacmanInputCommand(pacman));
        pauseState = new PauseState(new LevelInputCommand(levelUIPanel));
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

    
    @Override
    public void updateLevelUpEvent() {
        switchPauseState();
    }

    @Override
    public void updateLevelUpEnd() {
        switchPlayState();
    }
}
