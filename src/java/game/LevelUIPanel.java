package game;

import javax.swing.*;
import java.awt.*;

//TODO Merge이후 ILevelDataObserver를 implements하도록 구현하여야 함
public class LevelUIPanel extends JPanel {
    public static int width;
    public static int height;

    private int level = 1;
    private JLabel levelLabel;

    public LevelUIPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        levelLabel = new JLabel("Level: " + level);
        levelLabel.setFont(levelLabel.getFont().deriveFont(20.0F));
        levelLabel.setForeground(Color.white);
        this.add(levelLabel, BorderLayout.WEST);
    }

    public void updateLabel(int incrLevel) {
        this.level += incrLevel;
        this.levelLabel.setText("Level: " + level);
    }

    //TODO get메서드는 Encapsulation 위배로 사용하지 않는 게 좋으나 일단 UIPanel과 유사하게 구성하기 위해 작성
    public int getLevel() {return this.level;}

    //TODO Observer 패턴 연결 이후 update 코드 작성
//    @Override
//    public void update(GameLevelData data){
//        //내부 로직 작성 필요
//    }


}
