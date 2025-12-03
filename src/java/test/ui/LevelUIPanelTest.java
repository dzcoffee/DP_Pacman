package test.ui;

import game.LevelUIPanel;
import game.level.GameLevelData;
import game.level.LevelManager;
import game.utils.KeyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LevelUIPanelTest {

    // 테스트할 객체
    private LevelUIPanel levelUIPanel;

    // 의존성 객체 (Mocking 대상)
    private LevelManager mockLevelManager;
    private KeyHandler mockKeyHandler;
    private GameLevelData mockGameLevelData;

    @BeforeEach
    void setUp() {
        // Mock 객체 생성
        mockLevelManager = mock(LevelManager.class);
        mockKeyHandler = mock(KeyHandler.class);
        mockGameLevelData = mock(GameLevelData.class);

        // 테스트용 LevelUIPanel 인스턴스 생성 (임의의 크기)
        levelUIPanel = new LevelUIPanel(300, 500);
        levelUIPanel.setLevelManager(mockLevelManager);

        mockKeyHandler.k_up = mock(KeyHandler.Key.class);
        mockKeyHandler.k_down = mock(KeyHandler.Key.class);
        mockKeyHandler.k_enter = mock(KeyHandler.Key.class);

    }

    // --- 1. 초기화 및 업데이트 테스트 ---

    @Test
    void testInitialSetupAndLevelManagerAssignment() {
        // 1. 초기 레벨 확인
        assertEquals(1, levelUIPanel.getLevel(), "초기 레벨은 1이어야 한다.");
        assertEquals(3, levelUIPanel.getSelectedOptionIndex(), "초기 선택 인덱스는 3이어야 한다.");
    }

    @Test
    void testUpdateLabel() {
        levelUIPanel.updateLevelUpEvent(); // 레벨 1 증가
        assertEquals(2, levelUIPanel.getLevel(), "레벨은 2가 되어야 한다.");

        levelUIPanel.updateLevelUpEvent(); // 레벨 3 증가
        assertEquals(3, levelUIPanel.getLevel(), "레벨은 5가 되어야 한다.");
    }

    @Test
    void testUpdateLevelData_showsMenu() {
        levelUIPanel.showLevelUpMenu(false); // 메뉴를 강제적으로 숨김
        assertFalse(levelUIPanel.getNowVisible(), "초기 상태는 숨겨져 있어야 한다.");

        // LevelManager로부터 LevelUpEvent를 받으면
        levelUIPanel.updateLevelUpEvent();

        // 메뉴가 다시 표시되어야 함
        assertTrue(levelUIPanel.getNowVisible(), "업데이트를 받은 후 메뉴가 다시 표시되어야 한다.");
    }

    // --- 2. 옵션 선택 변경 (changeSelection) 테스트 ---

    @Test
    void testChangeSelection_Down_Wraps() {
        levelUIPanel.showLevelUpMenu(true);
        levelUIPanel.changeSelection(1); // 현재 3 -> 4 (0으로 순환)
        assertEquals(0, levelUIPanel.getSelectedOptionIndex(), "인덱스가 4->0으로 순환해야 한다.");

        levelUIPanel.changeSelection(1); // 0 -> 1
        assertEquals(1, levelUIPanel.getSelectedOptionIndex(), "인덱스가 1이 되어야 한다.");
    }

    @Test
    void testChangeSelection_Up_Wraps() {
        levelUIPanel.showLevelUpMenu(true);
        levelUIPanel.changeSelection(1); // 3 -> 0
        levelUIPanel.changeSelection(-1); // 0 -> -1 (3으로 순환)
        assertEquals(3, levelUIPanel.getSelectedOptionIndex(), "인덱스가 -1->3으로 순환해야 한다.");

        levelUIPanel.changeSelection(-1); // 3 -> 2
        assertEquals(2, levelUIPanel.getSelectedOptionIndex(), "인덱스가 2가 되어야 한다.");
    }

    @Test
    void testChangeSelection_MenuHidden_Ignored() {
        levelUIPanel.showLevelUpMenu(false); // 메뉴 숨김
        levelUIPanel.changeSelection(1); // 변경 시도

        // 메뉴가 숨겨져 있었으므로 인덱스는 초기값 3이 유지되어야 함
        assertEquals(3, levelUIPanel.getSelectedOptionIndex(), "메뉴가 숨겨지면 선택 변경이 무시되어야 한다.");
    }


    // --- 3. 키 입력 테스트 ---

    @Test
    void testInput_UpKey() {
        levelUIPanel.showLevelUpMenu(true);
        mockKeyHandler.k_up.isPressed = true;
        levelUIPanel.changeSelection(1); // 초기 인덱스 3 -> 0으로 설정
        assertEquals(0, levelUIPanel.getSelectedOptionIndex());

        levelUIPanel.input(mockKeyHandler); // 0에서 위로 이동 시도
        assertEquals(3, levelUIPanel.getSelectedOptionIndex(), "UP 키 입력 시 인덱스가 0->3으로 변경되어야 한다.");
        assertFalse(mockKeyHandler.k_up.isPressed, "UP 키 상태가 해제되어야 한다.");
    }

    @Test
    void testInput_DownKey() {
        levelUIPanel.showLevelUpMenu(true);
        mockKeyHandler.k_down.isPressed = true;

        levelUIPanel.input(mockKeyHandler); // 3에서 아래로 이동 시도
        assertEquals(0, levelUIPanel.getSelectedOptionIndex(), "DOWN 키 입력 시 인덱스가 3->0으로 변경되어야 한다.");
        assertFalse(mockKeyHandler.k_down.isPressed, "DOWN 키 상태가 해제되어야 한다.");
    }

    // --- 4. LevelManger로 위임하여 실행 (executeSelectedOption) 테스트 ---

    @Test
    void testExecuteSelectedOption_Case0_PacmanSpeedUp() {
        levelUIPanel.showLevelUpMenu(true);
        levelUIPanel.changeSelection(1); // 3 -> 0

        mockKeyHandler.k_enter.isPressed = true;

        levelUIPanel.input(mockKeyHandler);

        // LevelManager의 increasePacmanSpeed()가 호출되었는지 확인
        verify(mockLevelManager, times(1)).increasePacmanSpeed();
        verify(mockLevelManager, never()).frightenAll();
        verify(mockLevelManager, never()).decreaseGhostSpeed();
        verify(mockLevelManager, never()).increasePacmanLife();

        assertFalse(mockKeyHandler.k_enter.isPressed, "Enter 키 상태가 해제되어야 한다.");
    }

    @Test
    void testExecuteSelectedOption_Case1_GhostSpeedDown() {
        levelUIPanel.showLevelUpMenu(true);
        levelUIPanel.changeSelection(1); // 3 -> 0
        levelUIPanel.changeSelection(1); // 0 -> 1

        mockKeyHandler.k_enter.isPressed = true;

        levelUIPanel.input(mockKeyHandler);

        // LevelManager의 decreaseGhostSpeed()가 호출되었는지 확인
        verify(mockLevelManager, times(1)).decreaseGhostSpeed();
        verify(mockLevelManager, never()).frightenAll();
        verify(mockLevelManager, never()).increasePacmanSpeed();
        verify(mockLevelManager, never()).increasePacmanLife();

        assertFalse(mockKeyHandler.k_enter.isPressed, "Enter 키 상태가 해제되어야 한다.");
    }

    @Test
    void testExecuteSelectedOption_Case2_PacmanLifeUp() {
        levelUIPanel.showLevelUpMenu(true);
        levelUIPanel.changeSelection(-1); // 3 -> 2

        mockKeyHandler.k_enter.isPressed = true;

        levelUIPanel.input(mockKeyHandler);

        // LevelManager의 increasePacmanLife()가 호출되었는지 확인
        verify(mockLevelManager, times(1)).increasePacmanLife();
        verify(mockLevelManager, never()).frightenAll();
        verify(mockLevelManager, never()).increasePacmanSpeed();
        verify(mockLevelManager, never()).decreaseGhostSpeed();

        assertFalse(mockKeyHandler.k_enter.isPressed, "Enter 키 상태가 해제되어야 한다.");
    }

    @Test
    void testExecuteSelectedOption_Case3_FrightenAll() {
        levelUIPanel.showLevelUpMenu(true);
        mockKeyHandler.k_enter.isPressed = true;

        levelUIPanel.input(mockKeyHandler);

        // LevelManager의 frightenAll()이 호출되었는지 확인
        verify(mockLevelManager, times(1)).frightenAll();
        verify(mockLevelManager, never()).increasePacmanSpeed();
        verify(mockLevelManager, never()).increasePacmanLife();
        verify(mockLevelManager, never()).decreaseGhostSpeed();

        assertFalse(mockKeyHandler.k_enter.isPressed, "Enter 키 상태가 해제되어야 한다.");
    }
}
