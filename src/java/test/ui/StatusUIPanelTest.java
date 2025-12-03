package test.ui;

import game.StatusUIPanel;
import game.level.GameLevelData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatusUIPanelTest {

    // 테스트할 객체
    private StatusUIPanel statusUIPanel;

    // 의존성 객체 (Mocking 대상)
    private GameLevelData mockGameLevelData;

    @BeforeEach
    void setUp() {
        // Mock 객체 생성
        mockGameLevelData = mock(GameLevelData.class);

        // 테스트용 StatusUIPanel 인스턴스 생성 (임의의 크기)
        statusUIPanel = new StatusUIPanel(400, 100);
    }

    // --- 1. 초기화 및 UI 구조 테스트 ---

    @Test
    void testInitialSetup() {
        // 너비, 높이 설정 확인
        assertEquals(400, StatusUIPanel.width);
        assertEquals(100, StatusUIPanel.height);

        // 배경색 확인
        assertEquals(Color.BLACK, statusUIPanel.getBackground());

        // 레이아웃 매니저 확인 (BoxLayout)
        assertTrue(statusUIPanel.getLayout() instanceof BoxLayout, "메인 패널은 BoxLayout이어야 한다.");

        // 내부 컴포넌트(statusRowPanel) 존재 확인
        assertTrue(statusUIPanel.getComponentCount() > 0, "statusRowPanel이 추가되어 있어야 한다.");
    }

    @Test
    void testInitialLabelValues() {
        // StatusUIPanel 생성자 -> initializeLabels()에서 설정된 초기 텍스트값 검증
        // 소스 코드상 초기값: SPEED: 1.0x, GHOST: 1.0x, LIFE: 1
        verifyLabelsText("SPEED: 1.0x", "GHOST: 1.0x", "LIFE: 1");
    }

    // --- 2. 데이터 업데이트 테스트 (updateLevelData) ---

    @Test
    void testUpdateLevelData_StandardValues() {
        // Given: GameLevelData가 특정 값을 반환하도록 설정
        when(mockGameLevelData.getPacmanSpeed()).thenReturn(2.0f);
        when(mockGameLevelData.getGhostSpeed()).thenReturn(0.5f);
        when(mockGameLevelData.getPacmanLife()).thenReturn(3);

        // When: 데이터 업데이트 수행
        statusUIPanel.updateLevelData(mockGameLevelData);

        // Then: UI 라벨들이 예상되는 텍스트로 변경되었는지 검증
        verifyLabelsText("SPEED: 2.0x", "GHOST: 0.5x", "LIFE: 3");
    }


     // Private Label에 직접 접근할 수 없으므로, Component 트리를 탐색하여 텍스트를 검증합니다.

    private void verifyLabelsText(String expectedSpeed, String expectedGhost, String expectedLife) {

        // 1. 메인 패널에서 statusRowPanel 추출
        Component[] mainComponents = statusUIPanel.getComponents();
        assertTrue(mainComponents.length > 0, "내부 패널이 존재해야 합니다.");

        JPanel statusRowPanel = null;
        for (Component comp : mainComponents) {
            if (comp instanceof JPanel) {
                statusRowPanel = (JPanel) comp;
                break;
            }
        }
        assertNotNull(statusRowPanel, "statusRowPanel을 찾을 수 없습니다.");

        // 2. statusRowPanel 내부의 JLabel 추출
        Component[] labels = statusRowPanel.getComponents();
        assertEquals(3, labels.length, "statusRowPanel 안에는 3개의 라벨이 있어야 합니다.");

        JLabel pacmanSpeedLabel = (JLabel) labels[0];
        JLabel ghostSpeedLabel = (JLabel) labels[1];
        JLabel pacmanLifeLabel = (JLabel) labels[2];

        // 3. 텍스트 검증
        assertEquals(expectedSpeed, pacmanSpeedLabel.getText(), "Pacman Speed 텍스트 불일치");
        assertEquals(expectedGhost, ghostSpeedLabel.getText(), "Ghost Speed 텍스트 불일치");
        assertEquals(expectedLife, pacmanLifeLabel.getText(), "Life 텍스트 불일치");
    }
}
