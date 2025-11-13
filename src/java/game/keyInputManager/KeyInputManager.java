package game.keyInputManager;

public class KeyInputManager {
    private GameState gameState;

    private final GameState playState;
    private final GameState pauseState;

    public KeyInputManager() {
        playState = new PlayState();
        pauseState = new PauseState();
        gameState = playState;
    }


    public int getGameState() {return gameState.getGameState();}

    public void switchPlayState() {gameState = playState;}
    public void switchPauseState() {gameState = pauseState;}

    


}
