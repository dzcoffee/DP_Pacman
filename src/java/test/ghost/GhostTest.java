package test.ghost;

import game.Game;
import game.entities.ghosts.Ghost;
import game.ghostStates.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class GhostTest {
    static class TestGhost extends Ghost {

        public TestGhost(int x, int y) {
            super(x, y, "blinky.png");
        }


        @Override
        public void updatePosition() {
            //update 시 state만 검사 좌표 이동 안 함
        }



        public void setChasing(boolean isChasing){
            this.isChasing = isChasing;
        }

        public boolean getChasing(){
            return this.isChasing;
        }


        public void setPos(int x, int y){
            this.xPos = x;
            this.yPos = y;
        }



    }


    @Test
    public void testGhostState_initialState() {
        Ghost g = new GhostStateTest.TestGhost(0, 0);
        Assertions.assertThat(g.getState()).isInstanceOf(HouseMode.class);
    }

    @Test
    public void testGhostState_stateChange() {
        Ghost g = new GhostStateTest.TestGhost(0, 0);

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
    public void testGhostState_outsideHouse(){
        GhostStateTest.TestGhost g = new GhostStateTest.TestGhost(0, 0);
        Game.setFirstInput(true);
        g.setPos(208,168);
        g.update();
        Assertions.assertThat(g.getState()).isNotInstanceOf(HouseMode.class);

    }

    @Test
    public void testGhostState_insideHouse(){
        GhostStateTest.TestGhost g = new GhostStateTest.TestGhost(0, 0);
        g.switchEatenMode();
        Game.setFirstInput(true);
        g.setPos(208,200);
        g.update();
        Assertions.assertThat(g.getState()).isInstanceOf(HouseMode.class);

    }


    @Test
    public void testGhostState_modeTimer(){
        GhostStateTest.TestGhost g = new GhostStateTest.TestGhost(0, 0);
        g.switchScatterMode();
        g.setChasing(false);
        Game.setFirstInput(true);
        for (int i = 0; i < 60 * 5; i++) g.update();
        Assertions.assertThat(g.getState()).isInstanceOf(ChaseMode.class);
        Assertions.assertThat(g.getChasing()).isTrue();
        for (int i = 0; i < 60 * 15; i++) g.update();
        Assertions.assertThat(g.getState()).isInstanceOf(ScatterMode.class);
        Assertions.assertThat(g.getChasing()).isFalse();


    }

    @Test
    public void testGhostState_frightenedTimerTest(){

        Ghost g = new GhostStateTest.TestGhost(0, 0);
        g.switchFrightenedMode();
        Assertions.assertThat(g.getState()).isInstanceOf(FrightenedMode.class);
        Game.setFirstInput(true);

        //60 frame * 7 seconds
        for (int i = 0; i < 60 * 7; i++) g.update();
        Assertions.assertThat(g.getState()).isNotInstanceOf(FrightenedMode.class);

    }
}
