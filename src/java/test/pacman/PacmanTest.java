package test.pacman;

import game.Observer;
import game.entities.PacGum;
import game.entities.Pacman;
import game.entities.SuperPacGum;
import game.entities.ghosts.Ghost;
import game.utils.CollisionDetector;
import game.utils.WallCollisionDetector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class PacmanTest {
    private CollisionDetector mockCollisionDetector;
    private Observer mockObserver;
    private Pacman pacman;

    @Before
    public void setUp() {
        mockCollisionDetector = Mockito.mock(CollisionDetector.class);
        mockObserver = Mockito.mock(Observer.class);
        pacman = Mockito.spy(new Pacman(100, 100)); 
        pacman.setCollisionDetector(mockCollisionDetector);
        pacman.registerObserver(mockObserver);
    }

    @Test
    public void test_PacGumCollision_notifiesObserver() {
        PacGum mockPacGum = Mockito.mock(PacGum.class);
        when(mockCollisionDetector.checkCollision(pacman, PacGum.class)).thenReturn(mockPacGum);

        try (MockedStatic<WallCollisionDetector> wallMock = Mockito.mockStatic(WallCollisionDetector.class)) {
            wallMock.when(() -> WallCollisionDetector.checkWallCollision(any(), anyInt(), anyInt())).thenReturn(false);
            pacman.update();
            verify(mockObserver, times(1)).updatePacGumEaten(mockPacGum);
            verify(mockObserver, never()).updateSuperPacGumEaten(any());
            verify(mockObserver, never()).updateGhostCollision(any());
        }
    }

    @Test
    public void test_SuperPacGumCollision_notifiesObserver() {
        SuperPacGum mockSuperPacGum = Mockito.mock(SuperPacGum.class);
        when(mockCollisionDetector.checkCollision(pacman, SuperPacGum.class)).thenReturn(mockSuperPacGum);

        try (MockedStatic<WallCollisionDetector> wallMock = Mockito.mockStatic(WallCollisionDetector.class)) {
            wallMock.when(() -> WallCollisionDetector.checkWallCollision(any(), anyInt(), anyInt())).thenReturn(false);
            pacman.update();
            verify(mockObserver, times(1)).updateSuperPacGumEaten(mockSuperPacGum);
            verify(mockObserver, never()).updatePacGumEaten(any());
            verify(mockObserver, never()).updateGhostCollision(any());
        }
    }

    @Test
    public void test_GhostCollision_notifiesObserver() {
        Ghost mockGhost = Mockito.mock(Ghost.class);
        when(mockCollisionDetector.checkCollision(pacman, Ghost.class)).thenReturn(mockGhost);

        try (MockedStatic<WallCollisionDetector> wallMock = Mockito.mockStatic(WallCollisionDetector.class)) {
            wallMock.when(() -> WallCollisionDetector.checkWallCollision(any(), anyInt(), anyInt())).thenReturn(false);
            pacman.update();
            verify(mockObserver, times(1)).updateGhostCollision(mockGhost);
            verify(mockObserver, never()).updatePacGumEaten(any());
            verify(mockObserver, never()).updateSuperPacGumEaten(any());
        }
    }
}
