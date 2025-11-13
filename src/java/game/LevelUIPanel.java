package game;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

//TODO Merge이후 ILevelDataObserver를 implements하도록 구현하여야 함
public class LevelUIPanel extends JPanel {
    public static int width;
    public static int height;

    private int level = 1;
    private JLabel levelLabel;

    //레벨업 이후 옵션 변수들
    private JPanel optionsPanel;       // 4개의 옵션을 담을 컨테이너
    private JPanel[] optionItems;      // 개별 옵션 패널 (배경색 변경용)
    private JLabel[] optionTexts;      // 개별 옵션 텍스트
    private int currentSelectionIndex = 0; // 현재 선택된 옵션 (0~3)

    private final String[] OPTION_NAMES = {
            "PACMAN SPEED UP",
            "GHOST SPEED DOWN",
            "GHOST LIFE UP",
            "ALL GHOST EATEN STATE"
    };

    public LevelUIPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        levelLabel = new JLabel("Level: " + level);
        levelLabel.setFont(levelLabel.getFont().deriveFont(20.0F));
        levelLabel.setForeground(Color.white);

        this.add(levelLabel, BorderLayout.NORTH);

        initOptionsPanel();
    }

    public void updateLabel(int incrLevel) {
        this.level += incrLevel;
        this.levelLabel.setText("Level: " + level);
    }

    private void initOptionsPanel() {
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BorderLayout());
        optionsPanel.setBackground(Color.BLACK);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // 전체 여백

        JLabel selectLabel = new JLabel("Select Level Option!", SwingConstants.CENTER);
        selectLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        selectLabel.setForeground(Color.CYAN); // 안내 문구는 눈에 띄는 색상(Cyan)
        selectLabel.setBorder(new EmptyBorder(0, 0, 20, 0)); // 옵션들과의 간격 20px
        optionsPanel.add(selectLabel, BorderLayout.NORTH);

        JPanel optionsGrid = new JPanel(new GridLayout(4, 1, 0, 5)); // 4행 1열, 칸 사이 간격 15px
        optionsGrid.setBackground(Color.BLACK);

        optionItems = new JPanel[4];
        optionTexts = new JLabel[4];

        for (int i = 0; i < 4; i++) {
            // 개별 옵션 카드 생성
            optionItems[i] = new JPanel(new BorderLayout());
            optionItems[i].setBackground(Color.BLACK); // 기본 검정

            optionItems[i].setBorder(new CompoundBorder(
                    new LineBorder(Color.DARK_GRAY, 2),
                    new EmptyBorder(10, 2, 10, 2)
            ));

            // 텍스트 라벨
            optionTexts[i] = new JLabel(OPTION_NAMES[i], SwingConstants.CENTER);
            optionTexts[i].setFont(new Font("Monospaced", Font.BOLD, 13));
            optionTexts[i].setForeground(Color.GRAY);

            optionItems[i].add(optionTexts[i], BorderLayout.CENTER);
            optionsGrid.add(optionItems[i]);
        }

        optionsPanel.add(optionsGrid, BorderLayout.CENTER);

        // 초기에는 숨김 처리
        optionsPanel.setVisible(false);
        this.add(optionsPanel, BorderLayout.CENTER);

        showLevelUpMenu(true);
        //TODO 테스트 용으로 임시로 작성함 이후 연결 시에는 주석처리 필요
    }

    public void showLevelUpMenu(boolean show) {
        optionsPanel.setVisible(show);
        if (show) {
            currentSelectionIndex = 0;
            updateSelectionVisual();
        }
        revalidate();
        repaint();
    }

    public void changeSelection(int direction) {
        if (!optionsPanel.isVisible()) return; // 메뉴가 안 보이면 무시

        currentSelectionIndex += direction;

        // 범위 제한 (0 ~ 3) - 순환되게 하려면 로직 변경 가능
        if (currentSelectionIndex < 0) currentSelectionIndex = 0;
        if (currentSelectionIndex > 3) currentSelectionIndex = 3;

        updateSelectionVisual();
    }

    public int getSelectedOptionIndex() {
        return currentSelectionIndex;
    }

    private void updateSelectionVisual() {
        for (int i = 0; i < 4; i++) {
            if (i == currentSelectionIndex) {
                // 선택됨: 노란색 테두리 + 폰트 굵게
                optionItems[i].setBorder(new LineBorder(Color.YELLOW, 3));
                optionTexts[i].setForeground(Color.YELLOW);
                optionTexts[i].setFont(new Font("Arial", Font.BOLD, 18));
            } else {
                // 선택 안됨: 회색 테두리 + 흰색 폰트
                optionItems[i].setBorder(new LineBorder(Color.GRAY, 1));
                optionTexts[i].setForeground(Color.WHITE);
                optionTexts[i].setFont(new Font("Arial", Font.PLAIN, 16));
            }
        }
    }



    //TODO get메서드는 Encapsulation 위배로 사용하지 않는 게 좋으나 일단 UIPanel과 유사하게 구성하기 위해 작성
    public int getLevel() {return this.level;}

    //TODO Observer 패턴 연결 이후 update 코드 작성
//    @Override
//    public void update(GameLevelData data){
//        //내부 로직 작성 필요
//    }


}
