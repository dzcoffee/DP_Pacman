package ghost;

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
import game.utils.Utils;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class GhostStrategyTest {

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
    public void testBlinkyStrategy() throws IOException {
        IGhostStrategy strategy = new BlinkyStrategy();
        Game game = new Game();
        GameplayPanel gameplayPanel = new GameplayPanel(100,100);

        // getChaseTargetPosition test
        int[] position1 = strategy.getChaseTargetPosition();
        Assertions.assertThat(position1[0]).isEqualTo(Game.getPacman().getxPos());
        Assertions.assertThat(position1[1]).isEqualTo(Game.getPacman().getyPos());

        // getScatterTargetPosition test
        int[] position2 = strategy.getScatterTargetPosition();
        Assertions.assertThat(position2[0]).isEqualTo(GameplayPanel.width);
        Assertions.assertThat(position2[1]).isEqualTo(0);
    }

    @Test
    public void testPinkyStrategy(){
        IGhostStrategy strategy = new PinkyStrategy();
        Game game = new Game();

        // getChaseTargetPosition test
        int[] position1 = strategy.getChaseTargetPosition();
        int[] answerPosition = Utils.getPointDistanceDirection(Game.getPacman().getxPos(), Game.getPacman().getyPos(), 64, Utils.directionConverter(Game.getPacman().getDirection()));
        Assertions.assertThat(position1).isEqualTo(answerPosition);

        // getScatterTargetPosition test
        int[] position2 = strategy.getScatterTargetPosition();
        Assertions.assertThat(position2[0]).isEqualTo(0);
        Assertions.assertThat(position2[1]).isEqualTo(0);
    }
}
