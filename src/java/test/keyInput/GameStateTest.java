package keyInput;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import game.LevelUIPanel;
import game.entities.Pacman;
import game.keyInputManager.PauseState;
import game.keyInputManager.PlayState;
import game.keyInputManager.command.LevelInputCommand;
import game.keyInputManager.command.PacmanInputCommand;
import game.utils.KeyHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GameStateTest {
    @Mock
    private KeyHandler keyHandler;

    @Mock
    private PacmanInputCommand pacmanCommand;

    @Mock
    private LevelInputCommand levelCommand;

    private PlayState playState;
    private PauseState pauseState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        playState = new PlayState(pacmanCommand);
        pauseState = new PauseState(levelCommand);
    }

    @Test
    public void testGameState_playStateFunction() {
        playState.getInput(keyHandler);

        verify(pacmanCommand, times(1)).execute(keyHandler);
    }

    @Test
    public void testGameState_pauseStateFunction() {
        pauseState.getInput(keyHandler);

        verify(levelCommand, times(1)).execute(keyHandler);
    }
}

