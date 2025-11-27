package game;

import game.level.GameLevelData;
import game.level.ILevelDataObserver;

import javax.swing.*;
import java.awt.*;

public class StatusUIPanel extends JPanel implements ILevelDataObserver {
    public static int width;
    public static int height;

    private JLabel pacmanSpeedLabel;
    private JLabel ghostSpeedLabel;
    private JLabel pacmanLifeLabel;

    // 상태바 행 전체를 담을 JPanel을 멤버 변수로 선언
    private JPanel statusRowPanel;

    @Override
    public void updateLevelData(GameLevelData data){
        updateStatus(data.getPacmanSpeed(), data.getGhostSpeed(), data.getPacmanLife());
    }

    public StatusUIPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);

        // 1. 레이아웃 설정: 전체 요소(Score, Level, Options)를 수직으로 쌓기 위해 BoxLayout 사용
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 스크린샷과 동일한 수직 쌓기 구조

        // 2. JLabel 초기화 및 스타일 설정 (여기서 statusRowPanel도 생성됨)
        initializeLabels();

        // 3. 레이블 배치: 세부 레이블(SPEED, GHOST, LIFE)이 담긴 Row 패널 전체를 메인 패널에 추가
        // (주의: Score, Level 등 다른 요소도 여기에 추가되어야 합니다.)
        this.add(statusRowPanel);
    }

    private void initializeLabels() {
        Font statusFont = new Font("Arial", Font.BOLD, 14);
        Color textColor = Color.WHITE;

        // statusRowPanel을 여기서 초기화하고, 이 패널에만 레이블을 추가합니다.
        statusRowPanel = new JPanel();
        statusRowPanel.setLayout(new GridLayout(1,3,10,0)); // SPEED/GHOST/LIFE 간 간격 10px
        statusRowPanel.setBackground(Color.BLACK);
        // BoxLayout에서 중앙 정렬을 위해 필요
        statusRowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 좌측: Pacman Speed
        pacmanSpeedLabel = new JLabel("SPEED: 1.0x");
        pacmanSpeedLabel.setFont(statusFont);
        pacmanSpeedLabel.setForeground(textColor);
        pacmanSpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pacmanSpeedLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 좌측 패딩
        statusRowPanel.add(pacmanSpeedLabel, BorderLayout.WEST);

        // 중앙: Ghost Speed
        ghostSpeedLabel = new JLabel("GHOST: 1.0x");
        ghostSpeedLabel.setFont(statusFont);
        ghostSpeedLabel.setForeground(textColor);
        ghostSpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ghostSpeedLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        statusRowPanel.add(ghostSpeedLabel, BorderLayout.CENTER);

        // 우측: Pacman Life
        pacmanLifeLabel = new JLabel("LIFE: 1");
        pacmanLifeLabel.setFont(statusFont);
        pacmanLifeLabel.setForeground(Color.RED);
        pacmanLifeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pacmanLifeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 우측 패딩
        statusRowPanel.add(pacmanLifeLabel, BorderLayout.EAST);
    }

    private void updateStatus(float pacmanSpeedIndex, float ghostSpeedIndex, int pacmanLife) {
        // 좌측 업데이트
        pacmanSpeedLabel.setText(String.format("SPEED: %.1fx", pacmanSpeedIndex));

        // 중앙 업데이트
        ghostSpeedLabel.setText(String.format("GHOST: %.1fx", ghostSpeedIndex));

        // 우측 업데이트: pacmanLife 수만큼 하트(♥)를 표시
        String lifeDisplay = "LIFE: " + pacmanLife;
        pacmanLifeLabel.setText(lifeDisplay);

        this.revalidate();
        this.repaint();
    }
}
