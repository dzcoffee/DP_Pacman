package test.ghost;

import game.entities.ghosts.*;
import game.ghostFactory.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;


public class GhostFactoryTest {

    void assertGhostFactory(Ghost g,Class<?> GhostType, int x, int y){
        // Concrete product Type check
        Assertions.assertThat(g).isInstanceOf(GhostType);

        // Factory Parameter check
        Assertions.assertThat(g.getxPos()).isEqualTo(x);
        Assertions.assertThat(g.getyPos()).isEqualTo(y);
    }

    void testGhostFactory(AbstractGhostFactory ghostFactory, Class<?> GhostType) {
        int x = 0;
        int y = 0;
        Ghost ghost = ghostFactory.makeGhost(0,0);
        assertGhostFactory(ghost, GhostType,x,y);
    }

    @Test
    public void testBlinkyFactory() {
        testGhostFactory(new BlinkyFactory(),Blinky.class);
    }

    @Test
    public void testClydeFactory(){
        testGhostFactory(new ClydeFactory(), Clyde.class);
    }

    @Test
    public void testInkyFactory(){
        testGhostFactory(new InkyFactory(), Inky.class);
    }

    @Test
    public void testPinkyFactory(){
        testGhostFactory(new PinkyFactory(), Pinky.class);
    }
}
