package game;

import game.utils.KeyHandler;
import game.utils.WallCollisionDetector;
import game.level.GameLevelData;
import game.level.ILevelDataObserver;
import game.level.LevelManager;
import game.utils.KeyHandler;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

//TODO Merge이후 ILevelDataObserver를 implements하도록 구현하여야 함
public class LevelUIPanel extends JPanel implements ILevelDataObserver {
    public static int width;
    public static int height;

    private int level = 1;
    private JLabel levelLabel;

    private LevelManager levelManager;

    private boolean nowVisible;

    //레벨업 이후 옵션 변수들
    private JPanel optionsPanel;       // 4개의 옵션을 담을 컨테이너
    private JPanel[] optionItems;      // 개별 옵션 패널 (배경색 변경용)
    private JLabel[] optionTexts;      // 개별 옵션 텍스트
    private JPanel operationPanel;
    private int currentSelectionIndex = 3; // 현재 선택된 옵션 (0~3)

    private final String[] OPTION_NAMES = {
            "PACMAN SPEED UP",
            "GHOST SPEED DOWN",
            "PACMAN LIFE UP",
            "ALL GHOST EATEN STATE"
    };

    //update를 받게 되면 LevelUI의 상태를 변경(아마 그런 용도로 작성하신 게 아닐까?)
    // 그럼 얘가 LevelManager라는 Observer에게 명령을 내리면서도 구독중이라 상태가 변하는? 구조? 이게 맞는지
    @Override
    public void updateLevelData(GameLevelData data) {
        showLevelUpMenu(!nowVisible);
    }

    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void input(KeyHandler k){
        if (!optionsPanel.isVisible()) return;

        if (k.k_up.isPressed) {
            changeSelection(-1);

            k.k_up.isPressed = false;
        }

        else if (k.k_down.isPressed) {
            changeSelection(1);

            k.k_down.isPressed = false;
        }

        else if (k.k_enter.isPressed) {
            executeSelectedOption(); // 선택된 옵션 실행

            k.k_enter.isPressed = false;
        }
    }

    private void executeSelectedOption(){
        switch (currentSelectionIndex){
            case 0:
                levelManager.increasePacmanSpeed();
                break;
            case 1:
                levelManager.decreaseGhostSpeed();
                break;
            case 2:
                levelManager.increasePacmanLife();
                break;
            case 3:
                levelManager.frightenAll();
                break;
        }


    }

    public LevelUIPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        levelLabel = new JLabel("Level: " + level);
        levelLabel.setFont(levelLabel.getFont().deriveFont(20.0F));
        levelLabel.setForeground(Color.white);

        this.add(levelLabel, BorderLayout.NORTH);

        nowVisible = false;

        initOptionsPanel();
    }

    //level Label Update하는 메서드
    public void updateLabel(int incrLevel) {
        this.level += incrLevel;
        this.levelLabel.setText("Level: " + level);
    }

    // 옵션 패널만을 설정한 패널임
    // 아래와 같은 형태로 구성되어 있음
    // optionsPanel [ selectLabel + optionsGrid [ option1, option2, option3, option4 ] ]
    private void initOptionsPanel() {
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BorderLayout());
        optionsPanel.setBackground(Color.BLACK);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // 전체 여백

        operationPanel = new JPanel();
        operationPanel.setLayout(new BorderLayout());
        operationPanel.setBackground(Color.BLACK);
        operationPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // 전체 여백


        JLabel selectLabel = new JLabel("Select Level Option!", SwingConstants.CENTER);
        selectLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        selectLabel.setForeground(Color.CYAN); // 안내 문구는 눈에 띄는 색상(Cyan)
        selectLabel.setBorder(new EmptyBorder(0, 0, 20, 0)); // 옵션들과의 간격 20px
        optionsPanel.add(selectLabel, BorderLayout.NORTH);

        JLabel applyWithEnterLabel = new JLabel("Press Enter for applying!!", SwingConstants.CENTER);
        applyWithEnterLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        applyWithEnterLabel.setForeground(Color.CYAN); // 안내 문구는 눈에 띄는 색상(Cyan)
        applyWithEnterLabel.setBorder(new EmptyBorder(0, 0, 20, 0)); // 옵션들과의 간격 20px
        operationPanel.add(applyWithEnterLabel, BorderLayout.NORTH);

        JPanel optionsGrid = new JPanel(new GridLayout(5, 1, 0, 5)); // 4행 1열, 칸 사이 간격 15px
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

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setBackground(Color.BLACK);

        // 버튼들을 위쪽(NORTH)에 붙여서 높이가 늘어나는 것을 방지
        centerWrap.add(optionsGrid, BorderLayout.NORTH);
        // 안내 문구를 그 바로 아래(CENTER)에 배치
        centerWrap.add(operationPanel, BorderLayout.CENTER);

        // [5] 묶은 패널을 optionsPanel의 중앙에 배치
        optionsPanel.add(centerWrap, BorderLayout.CENTER);

        // 초기에는 숨김 처리
        optionsPanel.setVisible(false);
        operationPanel.setVisible(false);

        this.add(optionsPanel, BorderLayout.CENTER);
        this.add(operationPanel, BorderLayout.SOUTH);

        showLevelUpMenu(true);
        //TODO 테스트 용으로 임시로 작성함 이후 연결 시에는 주석처리 필요
    }

    //해당 메서드로 LevelUpPanel을 보이게 Or 안 보이게 모두 가능
    // show = true -> 보이게 설정
    // show = false -> 안 보이게 설정
    public void showLevelUpMenu(boolean show) {
        optionsPanel.setVisible(show);
        operationPanel.setVisible(show);
        nowVisible = show;
        if (show) {
            currentSelectionIndex = 3;
            updateSelectionVisual();
        }
        revalidate();
        repaint();
    }

    public void changeSelection(int direction) {
        //direction을 +1로 하면 아래로, -1로 하면 위로
        if (!optionsPanel.isVisible()) return; // 메뉴가 안 보이면 무시

        currentSelectionIndex += direction;

        // 옵션 Index 선택이 순환되도록 구현하였음
        // (아래는 옵션이 4개임을 예시로 들어 설명, 실제로는 optionItems의 갯수에 따라 다르게 되도록 구현)
        // index 3 -> 4가 되면 0번째로 순환
        // index 0 -> -1이 되면 3번째로 순환
        if (currentSelectionIndex >= optionItems.length) currentSelectionIndex = 0;
        if (currentSelectionIndex < 0) currentSelectionIndex = optionItems.length - 1;

        updateSelectionVisual();
    }

    //혹시나 SelectionOptionIndex가 필요할까봐 작성했으나 캡슐화 위배로 인해 다르게 수정하는 게 좋음
    public int getSelectedOptionIndex() {
        return currentSelectionIndex;
    }

    // 현재 선택된 옵션 UI의 모습을 바꿔즈는 메서드
    private void updateSelectionVisual() {
        for (int i = 0; i < 4; i++) {
            if (i == currentSelectionIndex) {
                // 선택됨: 노란색 테두리 + 폰트 굵게
                optionItems[i].setBorder(new LineBorder(Color.YELLOW, 3));
                optionTexts[i].setForeground(Color.YELLOW);
                optionTexts[i].setFont(new Font("Arial", Font.BOLD, 18));
            } else {
                // 선택 안됨: 회색 테두리 + 흰색 폰트
                optionItems[i].setBorder(new LineBorder(Color.GRAY, 3));
                optionTexts[i].setForeground(Color.WHITE);
                optionTexts[i].setFont(new Font("Arial", Font.BOLD, 18));
            }
        }
    }

    //TODO get메서드는 Encapsulation 위배로 사용하지 않는 게 좋으나 일단 UIPanel과 유사하게 구성하기 위해 작성
    public int getLevel() {return this.level;}

    public boolean getNowVisible(){
        return nowVisible;
    }
}
