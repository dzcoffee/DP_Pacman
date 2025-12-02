package keyInput;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

import game.LevelUIPanel;
import game.entities.Pacman;
import game.keyInputManager.GameState;
import game.keyInputManager.KeyInputManager;
import game.keyInputManager.PauseState;
import game.keyInputManager.PlayState;
import game.utils.KeyHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class KeyInputManagerTest {
    private KeyInputManager manager;

    @Mock
    private Pacman pacman;
    @Mock
    private LevelUIPanel uiPanel;
    @Mock
    private KeyHandler keyHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new KeyInputManager(pacman, uiPanel);
    }

    @Test
    public void testKeyInputManager_ConstructorTest() {
        GameState state = manager.getGameState();
        assertThat(state).isInstanceOf(PlayState.class);
    }

    @Test
    public void testKeyInputManager_ObserverEventTest() {
        manager.updateLevelUpEvent();
        assertThat(manager.getGameState()).isInstanceOf(PauseState.class);

        manager.updateLevelUpEnd();
        assertThat(manager.getGameState()).isInstanceOf(PlayState.class);
    }

    @Test
    public void testKeyInputManager_InputFunctionTest() throws Exception {
        // 현재 state의 input이 불리고 있는지 테스트

        GameState mockState = mock(GameState.class);

        //  manager.gameState 변경
        var field = manager.getClass().getDeclaredField("gameState");
        field.setAccessible(true);
        field.set(manager, mockState);

        manager.input(keyHandler);

        verify(mockState, times(1)).getInput(keyHandler);
    }


}
