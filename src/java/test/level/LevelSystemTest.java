package test.level;

import game.LevelUIPanel;
import game.entities.Pacman;
import game.entities.ghosts.Ghost;
import game.ghostStates.GhostState;
import game.level.FrightenAllCommand;
import game.level.GameLevelData;
import game.level.ILevelDataObserver;
import game.level.LevelManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.crypto.Data;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LevelSystemTest {

    @Test
    public void levelManagerTest_frightenAll() {
        LevelManager levelManager = new LevelManager();
        FrightenAllCommand frightenAllCommand = Mockito.mock(FrightenAllCommand.class);
        levelManager.setFrightenAllCommand(frightenAllCommand);

        levelManager.frightenAll();

        verify(frightenAllCommand, times(1)).execute();


    }

    @Test
    public void levelManagerTest_LevelData() {
        LevelManager levelManager = new LevelManager();
        class TestLevelDataObserver implements ILevelDataObserver {
            public GameLevelData data;

            @Override
            public void updateLevelData(GameLevelData data) {
                this.data = data;
            }
        }
        TestLevelDataObserver observer = new TestLevelDataObserver();

        levelManager.registerObserver(observer);

        levelManager.increasePacmanSpeed();

        Assertions.assertThat(observer.data.getPacmanSpeed()).isEqualTo(2.0f);

        levelManager.decreaseGhostSpeed();
        Assertions.assertThat(observer.data.getGhostSpeed()).isEqualTo(0.5f);

        levelManager.increasePacmanLife();
        Assertions.assertThat(observer.data.getPacmanLife()).isEqualTo(2);

    }

    @Test
    public void levelManagerTest_PacmanLife() throws Exception {
        class TestGhost extends Ghost {

            public TestGhost(int xPos, int yPos) {
                super(xPos, yPos, "blinky.png");
            }
        }
        class TestLevelManager extends LevelManager {
            public boolean isExited = false;
            @Override
            protected void exitGame(){
                isExited = true;
            }
        }
        TestLevelManager levelManager = new TestLevelManager();
        class TestLevelDataObserver implements ILevelDataObserver {
            public GameLevelData data;

            @Override
            public void updateLevelData(GameLevelData data) {
                this.data = data;
            }
        }
        TestLevelDataObserver observer = new TestLevelDataObserver();

        levelManager.registerObserver(observer);

        levelManager.increasePacmanLife();
        Pacman pacman = new Pacman(0, 0);
        pacman.registerObserver(levelManager);
        Ghost ghost = new TestGhost(0, 0);
        ghost.switchFrightenedMode();
        pacman.notifyObserverGhostCollision(ghost);
        Assertions.assertThat(observer.data.getPacmanLife()).isEqualTo(2);

        ghost.switchChaseMode();
        pacman.notifyObserverGhostCollision(ghost);
        Assertions.assertThat(observer.data.getPacmanLife()).isEqualTo(1);

        ghost.switchChaseMode();
        pacman.notifyObserverGhostCollision(ghost);
        Assertions.assertThat(observer.data.getPacmanLife()).isEqualTo(0);
        Assertions.assertThat(levelManager.isExited).isTrue();


    }


}
