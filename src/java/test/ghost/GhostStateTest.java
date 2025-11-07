package test.ghost;

import game.Game;
import game.Observer;
import game.entities.Pacman;
import game.entities.SuperPacGum;
import game.entities.ghosts.Ghost;
import game.ghostStates.*;
import java.util.ArrayList;
import java.util.List;
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

        public void setPosition(int x, int y) {
            this.xPos = x;
            this.yPos = y;
        }

    }

    static class DummyGame implements Observer {
        List<Ghost> ghosts = new ArrayList<>();

        @Override
        public void updatePacGumEaten(game.entities.PacGum pg) {}

        @Override
        public void updateSuperPacGumEaten(SuperPacGum spg) {
            for (Ghost g : ghosts) {
                g.getState().superPacGumEaten();
            }
        }

        @Override
        public void updateGhostCollision(Ghost gh) {
            if (gh.getState() instanceof FrightenedMode) {
                gh.getState().eaten();
            }
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
    public void testGhostState_frightenedTimerTest() {
        TestGhost g = new TestGhost(0, 0);

        g.switchFrightenedMode();
        Assertions.assertThat(g.getState()).isInstanceOf(FrightenedMode.class);

        Game.setFirstInput(true);
        // 7초(60 * 7 = 420 frame) 경과시키기
        for (int i = 0; i < 60 * 7; i++) {
            g.update();
        }
        Assertions.assertThat(g.getState()).isNotInstanceOf(FrightenedMode.class);
    }

    @Test
    public void testGhostState_ChaseScatterModeTimerTest() {
        TestGhost g = new TestGhost(0, 0);

        g.switchScatterMode();
        Assertions.assertThat(g.getState()).isInstanceOf(ScatterMode.class);

        Game.setFirstInput(true);

        // 5초 동안 업데이트
        for (int i = 0; i < 60 * 5; i++) {
            g.update();
        }
        Assertions.assertThat(g.getState()).isInstanceOf(ChaseMode.class);

        // 다시 20초 동안 업데이트
        for (int i = 0; i < 60 * 20; i++) {
            g.update();
        }
        Assertions.assertThat(g.getState()).isInstanceOf(ScatterMode.class);
    }

    @Test
    public void testGhostState_ChangeToFrightenedTest() {
        Pacman pacman = new Pacman(0, 0);
        DummyGame dummyGame = new DummyGame();

        pacman.registerObserver(dummyGame);

        TestGhost ghost1 = new TestGhost(0, 0);
        TestGhost ghost2 = new TestGhost(0, 0);
        ghost1.switchScatterMode();
        ghost2.switchChaseMode();
        dummyGame.ghosts.add(ghost1);
        dummyGame.ghosts.add(ghost2);

        SuperPacGum spg = new SuperPacGum(0, 0);
        pacman.notifyObserverSuperPacGumEaten(spg);

        Assertions.assertThat(ghost1.getState()).isInstanceOf(FrightenedMode.class);
        Assertions.assertThat(ghost2.getState()).isInstanceOf(FrightenedMode.class);
    }

    @Test
    public void testGhostState_ChangeToEatenTest() {
        Pacman pacman = new Pacman(0, 0);
        DummyGame dummyGame = new DummyGame();

        pacman.registerObserver(dummyGame);

        TestGhost ghost = new TestGhost(0, 0);
        ghost.switchFrightenedMode();
        dummyGame.ghosts.add(ghost);

        pacman.notifyObserverGhostCollision(ghost);

        Assertions.assertThat(ghost.getState()).isInstanceOf(EatenMode.class);
    }

    @Test
    public void testGhostState_ChangeToHouseTest()  {
        TestGhost ghost = new TestGhost(0, 0);
        ghost.switchEatenMode();
        ghost.setPosition(208,200);
        Game.setFirstInput(true);
        ghost.update();
        Assertions.assertThat(ghost.getState()).isInstanceOf(HouseMode.class);
    }

    @Test
    public void testGhostState_ChangeToChaseOrScatterTest()  {
        TestGhost ghost = new TestGhost(0, 0);
        ghost.switchHouseMode();
        ghost.setPosition(208,168);
        Game.setFirstInput(true);
        ghost.update();
        Assertions.assertThat(ghost.getState()).isNotInstanceOf(HouseMode.class);
    }
}
