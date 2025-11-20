package game;

import game.entities.PacGum;
import game.entities.SuperPacGum;
import game.entities.ghosts.Ghost;
import game.ghostStates.FrightenedMode;

import javax.swing.*;
import java.awt.*;

//Panneau de l'interface utilisateur
public class UIPanel extends JPanel {
    public static int width;
    public static int height;

    private JLabel scoreLabel;

    public UIPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(20.0F));
        scoreLabel.setForeground(Color.white);
        this.add(scoreLabel, BorderLayout.WEST);
    }

    public void updateScore(int score) {
        this.scoreLabel.setText("Score: " + score);
    }



    //L'interface est notifiée lorsque Pacman est en contact avec une PacGum, une SuperPacGum ou un fantôme, et on met à jour le score affiché en conséquence

}
