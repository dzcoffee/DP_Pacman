package test.ghost;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import game.Game;
import game.entities.PacGum;
import game.entities.StaticEntity;
import game.entities.SuperPacGum;
import game.entities.ghosts.Blinky;
import game.entities.ghosts.Pinky;
import game.utils.CollisionDetector;

public class ReviveGumTest {
    
    @Mock
    private CollisionDetector collisionDetector;
    
    private Pinky pinky;
    private Blinky blinky;
    private PacGum destroyedPacGum;
    private SuperPacGum destroyedSuperPacGum;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pinky = new Pinky(10,10) {
            @Override
            public void updatePosition() {}
        };
        pinky.setCollisionDetector(collisionDetector);
        blinky = new Blinky(10, 10) {
            @Override
            public void updatePosition() {}
        };
        blinky.setCollisionDetector(collisionDetector);
        destroyedPacGum = new PacGum(10, 10);
        destroyedPacGum.destroy();
        destroyedSuperPacGum = new SuperPacGum(10, 10);
        destroyedSuperPacGum.destroy();
        Game.setFirstInput(true);
    }

    // ScatterMode의 Pinky가 지나가면 PacGum이 살아나는지 확인
    @Test
    public void testScatterModePinkyRevivesPacGum() {
        assertTrue(destroyedPacGum.isDestroyed());
        pinky.switchScatterMode();
        when(collisionDetector.checkCollisionWithDestroyed(any(Pinky.class), eq(StaticEntity.class))).thenReturn(destroyedPacGum);
        pinky.update();
        assertFalse("ScatterMode의 Pinky가 지나가면 PacGum이 살아나야 합니다.", destroyedPacGum.isDestroyed());
    }

    // ChaseMode의 Pinky가 지나가면 PacGum이 살아나는지 확인
    @Test
    public void testChaseModePinkyRevivesPacGum() {
        assertTrue(destroyedPacGum.isDestroyed());
        when(collisionDetector.checkCollisionWithDestroyed(any(Pinky.class), eq(StaticEntity.class))).thenReturn(destroyedPacGum);
        pinky.switchChaseMode();
        pinky.update();
        assertFalse("ChaseMode의 Pinky가 지나가면 PacGum이 살아나야 합니다.", destroyedPacGum.isDestroyed());
    }

    // FrightenedMode의 Pinky가 지나가면 SuperPacGum이 살아나는지 확인
    @Test
    public void testFrightenedModePinkyNotRevivesPacGum() {
        assertTrue(destroyedSuperPacGum.isDestroyed());
        when(collisionDetector.checkCollisionWithDestroyed(any(Pinky.class), eq(SuperPacGum.class))).thenReturn(destroyedSuperPacGum);
        pinky.switchFrightenedMode();
        pinky.update();
        assertTrue("FrightenedMode의 Pinky는 PacGum을 부활시키지 못합니다.", destroyedSuperPacGum.isDestroyed());
    }

    // ScatterMode의 Blinky가 지나가도 PacGum 상태에 변화가 없는지 확인
    @Test
    public void testBlinkyNotRevivesPacGum() {
        assertTrue(destroyedPacGum.isDestroyed());
        when(collisionDetector.checkCollisionWithDestroyed(any(Blinky.class), eq(PacGum.class))).thenReturn(destroyedPacGum);
        blinky.switchScatterMode();
        blinky.update();
        assertTrue("ScatterMode의 Blinky는 PacGum을 부활시키지 못합니다.", destroyedPacGum.isDestroyed());
    }
    
    // ScatterMode의 Pinky가 지나가면 SuperPacGum이 살아나는지 확인
    @Test
    public void testScatterModePinkyRevivesSuperPacGum() {
        assertTrue(destroyedSuperPacGum.isDestroyed());
        when(collisionDetector.checkCollisionWithDestroyed(any(Pinky.class), eq(StaticEntity.class))).thenReturn(destroyedSuperPacGum);
        pinky.switchScatterMode();
        pinky.update();
        assertFalse("ScatterMode의 Pinky가 지나가면 PacGum이 살아나야 합니다.", destroyedSuperPacGum.isDestroyed());
    }
}
