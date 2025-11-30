package test.scoremanager;

import game.Observer;
import game.Sujet;
import game.entities.PacGum;
import game.entities.Pacman;
import game.entities.SuperPacGum;
import game.entities.ghosts.Ghost;
import game.score.ScoreManager;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class ScoreManagerTest {


    @Test
    public void testScoreManager_observeScore() {
        ScoreManager scoreManager = new ScoreManager();
        Pacman pacman = new Pacman(0,0);
        PacGum pacGum = new PacGum(0, 0);
        SuperPacGum superPacGum = new SuperPacGum(0, 0);
        class TestGhost extends Ghost {
            public TestGhost(int x, int y) {
                super(x, y, "blinky.png");
            }
        }
        Ghost ghost = new TestGhost(0, 0);


        pacman.registerObserver(scoreManager);

        Assertions.assertThat(scoreManager.getScore()).isEqualTo(0);

        pacman.notifyObserverPacGumEaten(pacGum);
        Assertions.assertThat(scoreManager.getScore()).isEqualTo(10);

        pacman.notifyObserverSuperPacGumEaten(superPacGum);
        Assertions.assertThat(scoreManager.getScore()).isEqualTo(110);

        pacman.notifyObserverGhostCollision(ghost);
        Assertions.assertThat(scoreManager.getScore()).isEqualTo(110);

        ghost.switchFrightenedMode();

        pacman.notifyObserverGhostCollision(ghost);
        Assertions.assertThat(scoreManager.getScore()).isEqualTo(610);



    }



}
