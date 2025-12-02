package test.scoremanager;

import game.Observer;
import game.Sujet;
import game.entities.PacGum;
import game.entities.Pacman;
import game.entities.SuperPacGum;
import game.entities.ghosts.Ghost;
import game.level.ILevelUpEventObserver;
import game.score.ScoreManager;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ScoreManagerTest {
    Pacman pacman;
    PacGum pacGum;
    SuperPacGum superPacGum;
    Ghost ghost;


    private static class TestGhost extends Ghost {
        public TestGhost(int x, int y) {
            super(x, y, "blinky.png");
        }
    }

    @Before
    public void setUp() {
         pacman = new Pacman(0,0);
         pacGum = new PacGum(0, 0);
         superPacGum = new SuperPacGum(0, 0);
         ghost = new TestGhost(0, 0);
    }

    @Test
    public void testScoreManager_pacGumEaten() {
        ScoreManager scoreManager = new ScoreManager();

        pacman.registerObserver(scoreManager);

        Assertions.assertThat(scoreManager.getScore()).isEqualTo(0);

        pacman.notifyObserverPacGumEaten(pacGum);
        Assertions.assertThat(scoreManager.getScore()).isEqualTo(10);

    }
    @Test
    public void testScoreManager_superPacGumEaten() {
        ScoreManager scoreManager = new ScoreManager();

        pacman.registerObserver(scoreManager);

        Assertions.assertThat(scoreManager.getScore()).isEqualTo(0);

        pacman.notifyObserverSuperPacGumEaten(superPacGum);
        Assertions.assertThat(scoreManager.getScore()).isEqualTo(100);

    }

    @Test
    public void testScoreManager_ghostCollision_frighten() {
        ScoreManager scoreManager = new ScoreManager();

        pacman.registerObserver(scoreManager);

        Assertions.assertThat(scoreManager.getScore()).isEqualTo(0);

        ghost.switchFrightenedMode();

        pacman.notifyObserverGhostCollision(ghost);
        Assertions.assertThat(scoreManager.getScore()).isEqualTo(500);

    }

    @Test
    public void testScoreManager_ghostCollision_notFrighten() {
        ScoreManager scoreManager = new ScoreManager();

        pacman.registerObserver(scoreManager);

        Assertions.assertThat(scoreManager.getScore()).isEqualTo(0);

        ghost.switchChaseMode();

        pacman.notifyObserverGhostCollision(ghost);
        Assertions.assertThat(scoreManager.getScore()).isEqualTo(0);

    }




    @Test
    public void testScoreManager_levelUpEvent() throws Exception {
        ScoreManager scoreManager = new ScoreManager();

        ILevelUpEventObserver observer = mock(ILevelUpEventObserver.class);
        scoreManager.registerObserver(observer);


        for (int i = 0; i < 100; i++) {
            scoreManager.updatePacGumEaten(mock(PacGum.class));
        }

        verify(observer, times(1)).updateLevelUpEvent();
    }


}
