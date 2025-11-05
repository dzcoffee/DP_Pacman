package test.ghost;

import game.Game;
import game.entities.ghosts.Ghost;
import game.ghostStates.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class GhostStateTest {
    static class TestGhost extends Ghost {
        public TestGhost(int x, int y) {
            super(x, y, "blinky.png");
        }
        @Override
        public void updatePosition() {
            //update 시 state만 검사 좌표 이동 안 함
        }

    }

    @Test
    public void testGhostState_initialState() {
        Ghost g = new TestGhost(0, 0);
        Assertions.assertThat(g.getState()).isInstanceOf(HouseMode.class);
    }

    @Test
    public void testGhostState_stateChange() {
        TestGhost g = new TestGhost(0, 0);

        g.switchFrightenedMode();
        Assertions.assertThat(g.getState()).isInstanceOf(FrightenedMode.class);

        g.switchEatenMode();
        Assertions.assertThat(g.getState()).isInstanceOf(EatenMode.class);

        g.switchHouseMode();
        Assertions.assertThat(g.getState()).isInstanceOf(HouseMode.class);

        g.switchChaseMode();
        Assertions.assertThat(g.getState()).isInstanceOf(ChaseMode.class);

        g.switchScatterMode();
        Assertions.assertThat(g.getState()).isInstanceOf(ScatterMode.class);

    }

    @Test
    public void testGhostState_frightenedTimerTest(){
        //TODO : frighten 상태에서 7초 뒤에 frightenOver 검사
//        Game.setFirstInput(true);
    }
}
