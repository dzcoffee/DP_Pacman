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
    private Pacman pacman;

    @Mock
    private LevelUIPanel levelUIPanel;

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

        // state 내부 커맨드를 mock으로 설정
        playState = new PlayState(pacman);
        pauseState = new PauseState(levelUIPanel);

        var pacCmdField = PlayState.class.getDeclaredField("pacmanInputCommand");
        pacCmdField.setAccessible(true);
        pacCmdField.set(playState, pacmanCommand);

        var lvlCmdField = PauseState.class.getDeclaredField("levelInputCommand");
        lvlCmdField.setAccessible(true);
        lvlCmdField.set(pauseState, levelCommand);
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

