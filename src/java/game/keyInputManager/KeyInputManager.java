package game.keyInputManager;

import game.LevelUIPanel;
import game.entities.Pacman;
import game.level.ILevelUpEventObserver;
import game.utils.KeyHandler;

public class KeyInputManager implements ILevelUpEventObserver {
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

    
    @Override
    public void updateLevelUpEvent() {
        switchPauseState();
    }

    @Override
    public void updateLevelUpEnd() {
        switchPlayState();
    }
}
