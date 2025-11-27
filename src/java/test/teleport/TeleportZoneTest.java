package test.teleport;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import game.Game;
import game.entities.Entity;
import game.entities.Pacman;
import game.entities.TeleportZone;
import game.entities.ghosts.Blinky;
import game.entities.ghosts.Ghost;
import game.utils.CollisionDetector;

public class TeleportZoneTest {
    @Mock
    private CollisionDetector collisionDetector;
    
    private Pacman pacman;
    private Ghost ghost;
    private TeleportZone portalA;
    private TeleportZone portalB;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pacman = new Pacman(0, 0) {
            @Override
            public void updatePosition() {} // 포탈 시험중에는 안 움직이도록
        };
        ghost = new Blinky(10, 10) {
            @Override
            public void updatePosition() {} // 포탈 시험중에는 안 움직이도록
        };
        portalA = new TeleportZone(10, 10);
        portalB = new TeleportZone(100, 100);
        portalA.setPartner(portalB);
        portalB.setPartner(portalA);
        pacman.setCollisionDetector(collisionDetector);
        ghost.setCollisionDetector(collisionDetector);
        Game.setFirstInput(true);
    }
    
    // 팩맨 텔레포트 테스트
    @Test
    public void testPacmanTeleport() {
        when(collisionDetector.checkCollision(any(Pacman.class), eq(TeleportZone.class))).thenReturn(portalA);
        pacman.update();
        assertEquals("팩맨은 포탈을 타면 이동해야 합니다.(x)", 100, pacman.getxPos());
        assertEquals("팩맨은 포탈을 타면 이동해야 합니다.(y)", 100, pacman.getyPos());
    }
    
    // 유령 텔레포트 테스트
    @Test
    public void testGhostTeleport() {
        when(collisionDetector.checkCollision(any(Ghost.class), eq(TeleportZone.class))).thenReturn(portalA);
        ghost.update();
        assertEquals("유령은 포탈을 타면 이동해야 합니다.(x)", 100, ghost.getxPos());
        assertEquals("유령은 포탈을 타면 이동해야 합니다.(y)", 100, ghost.getyPos());
    }

    // 연속 텔레포트 방지
    @Test
    public void testPingPongPrevention() {
        when(collisionDetector.checkCollision(any(Pacman.class), eq(TeleportZone.class))).thenReturn(portalA);
        pacman.update();
        assertEquals(100, pacman.getxPos());
        when(collisionDetector.checkCollision(any(Pacman.class), eq(TeleportZone.class))).thenReturn(portalB);
        pacman.update();
        System.out.println(pacman.getxPos());
        assertEquals("방금 도착한 포탈에서는 다시 이동하지 않아야 합니다.", 100, pacman.getxPos());
    }
}
