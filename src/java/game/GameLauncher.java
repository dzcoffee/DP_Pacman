package game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

//Point d'entrée de l'application
public class GameLauncher {
    private static UIPanel uiPanel;
    private static LevelUIPanel levelUIPanel;
    private static GameplayPanel gameplayPanel;

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setTitle("Pacman");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel gameWindow = new JPanel();

        //Création de la "zone de jeu"
        try {
            gameplayPanel = new GameplayPanel(448,496);
            gameWindow.add(gameplayPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel uiContainer = new JPanel(new GridBagLayout());
        uiContainer.setPreferredSize(new Dimension(256, 496)); // 전체 UI 영역 크기는 기존과 같게
        uiContainer.setBackground(Color.BLACK); //UI 백그라운드는 검게
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        uiPanel = new UIPanel(256, 496);
        uiPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        gbc.gridy = 0;      // 첫 번째 줄에 위치
        gbc.weighty = 0.2;
        uiContainer.add(uiPanel, gbc);

        levelUIPanel = new LevelUIPanel(256, 496);
        gbc.gridy = 1;      // 두번째 줄에 위치
        gbc.weighty = 0.8;
        uiContainer.add(levelUIPanel, gbc);

        // 4. UI 컨테이너를 gameWindow의 우측(EAST)에 추가
        gameWindow.add(uiContainer);

        window.setContentPane(gameWindow);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public static UIPanel getUIPanel() {
        return uiPanel;
    }

    public static LevelUIPanel getLevelUIPanel() {
        return levelUIPanel;
    }
}
