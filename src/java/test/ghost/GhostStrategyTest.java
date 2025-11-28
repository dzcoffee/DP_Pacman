package test.ghost;

import game.Game;
import game.GameplayPanel;
import game.entities.ghosts.Blinky;
import game.entities.ghosts.Clyde;
import game.entities.ghosts.Ghost;
import game.entities.ghosts.Inky;
import game.entities.ghosts.Pinky;
import game.ghostStrategies.BlinkyStrategy;
import game.ghostStrategies.ClydeStrategy;
import game.ghostStrategies.IGhostStrategy;
import game.ghostStrategies.InkyStrategy;
import game.ghostStrategies.PinkyStrategy;
import game.utils.SoundManager;
import java.io.IOException;

import game.level.LevelManager;
import game.score.ScoreManager;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

public class GhostStrategyTest {

    static Game game;
    static GameplayPanel gameplayPanel;

    @BeforeClass
    public static void setup() throws IOException {
        game = new Game();
        LevelManager levelManager = new LevelManager();
        ScoreManager scoreManager = new ScoreManager();
        game.init(levelManager, scoreManager, SoundManager.getInstance());
        gameplayPanel = new GameplayPanel(100,100);
    }

    @Test
    public void testCreateStrategy() {
        Ghost ghost1 = new Blinky(0,0);
        Ghost ghost2 = new Clyde(0,0);
        Ghost ghost3 = new Inky(0,0);
        Ghost ghost4 = new Pinky(0,0);

        Assertions.assertThat(ghost1.getStrategy()).isInstanceOf(BlinkyStrategy.class);
        Assertions.assertThat(ghost2.getStrategy()).isInstanceOf(ClydeStrategy.class);
        Assertions.assertThat(ghost3.getStrategy()).isInstanceOf(InkyStrategy.class);
        Assertions.assertThat(ghost4.getStrategy()).isInstanceOf(PinkyStrategy.class);
    }

    @Test
    public void testBlinkyStrategy() {
        IGhostStrategy strategy = new BlinkyStrategy();

        // getChaseTargetPosition test
        int[] position1 = strategy.getChaseTargetPosition();
        Assertions.assertThat(position1).isEqualTo(new int[] {208, 360});

        // getScatterTargetPosition test
        int[] position2 = strategy.getScatterTargetPosition();
        Assertions.assertThat(position2).isEqualTo(new int[] {100, 0});
    }

    @Test
    public void testPinkyStrategy(){
        IGhostStrategy strategy = new PinkyStrategy();

        // getChaseTargetPosition test
        int[] position1 = strategy.getChaseTargetPosition();
        Assertions.assertThat(position1).isEqualTo(new int[] {272, 360});

        // getScatterTargetPosition test
        int[] position2 = strategy.getScatterTargetPosition();
        Assertions.assertThat(position2).isEqualTo(new int[] {0, 0});
    }

    @Test
    public void testClydeStrategy(){
        IGhostStrategy strategy1 = new ClydeStrategy(new Clyde(0,0));
        IGhostStrategy strategy2 = new ClydeStrategy(new Clyde(208,350));

        // getChaseTargetPosition test
        int[] position1 = strategy1.getChaseTargetPosition();
        int[] position2 = strategy2.getChaseTargetPosition();
        Assertions.assertThat(position1).isEqualTo(new int[] {208, 360});
        Assertions.assertThat(position2).isEqualTo(new int[] {0, 100});

        // getScatterTargetPosition test
        int[] position3 = strategy1.getScatterTargetPosition();
        Assertions.assertThat(position3).isEqualTo(new int[] {0, 100});
    }

    @Test
    public void testInkyStrategy(){
        IGhostStrategy strategy1 = new InkyStrategy(new Blinky(0,0));

        // getChaseTargetPosition test
        int[] position1 = strategy1.getChaseTargetPosition();
        Assertions.assertThat(position1).isEqualTo(new int[] {480, 720});

        // getScatterTargetPosition test
        int[] position2 = strategy1.getScatterTargetPosition();
        Assertions.assertThat(position2).isEqualTo(new int[] {100, 100});
    }
}
