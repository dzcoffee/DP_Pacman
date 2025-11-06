package test.collision;

import game.Game;
import game.entities.Entity;
import game.entities.Pacman;
import game.entities.Wall;
import game.ghostFactory.AbstractGhostFactory;
import game.ghostFactory.BlinkyFactory;
import game.utils.WallCollisionDetector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

public class WallCollisionDetectorTest {

    private Entity entity;
    private int cellSize = 8;
    AbstractGhostFactory abstractGhostFactory = null;

    @Before
    public void setUp() throws Exception {

    }

    private List<Wall> setTempWalls() {
        List<Point> coordinates = new ArrayList<>();

        // 테두리 벽
        for (int k = 0; k <= 20; k++) {
            coordinates.add(new Point(k * cellSize, 0 * cellSize));
            coordinates.add(new Point(k * cellSize, 20 * cellSize));
        }
        for (int i = 1; i <= 19; i++) {
            coordinates.add(new Point(0 * cellSize, i * cellSize));
            coordinates.add(new Point(20 * cellSize, i * cellSize));
        }

        // 테스트용 벽 생성
        coordinates.add(new Point(10 * cellSize, 10 * cellSize));

        //TODO 테스트용 Ghost House 생성

        return createTestWalls(coordinates);
    }

    private List<Wall> createTestWalls(List<Point> wallCoordinates) {
        List<Wall> walls = new ArrayList<>();
        for (Point p : wallCoordinates) {
            walls.add(new Wall(p.x, p.y));
            // 만약 Wall 생성자가 (x, y, size) 등을 받는다면 그에 맞게 수정
        }
        return walls;
    }

    @Test
    public void testCollision_Pacman_ShouldReturnTrue() {
        try(MockedStatic<Game> gameMock = mockStatic(Game.class)){
            List<Wall> fakeWalls = setTempWalls();
            Entity entity = new Pacman(10 * cellSize, 6 * cellSize);

            gameMock.when(() -> Game.getWalls()).thenReturn(fakeWalls);

            boolean result = WallCollisionDetector.checkWallCollision(entity, 0, 1);

            assertThat(result).isTrue();
        }
    }

    @Test
    public void testCollision_Ghost_ShouldReturnTrue() {
        try(MockedStatic<Game> gameMock = mockStatic(Game.class)){
            List<Wall> fakeWalls = setTempWalls();
            abstractGhostFactory = new BlinkyFactory();
            //TODO 일단 Blinky 하나만 테스트 함(다른 Ghost 속성이 다르게 테스트 될 경우에는 추가 메서드 작성 필요함
            Entity entity = abstractGhostFactory.makeGhost(10 * cellSize, 6 * cellSize);

            gameMock.when(() -> Game.getWalls()).thenReturn(fakeWalls);

            boolean result = WallCollisionDetector.checkWallCollision(entity, 0, 1);

            assertThat(result).isTrue();
        }
    }

    @Test
    public void testCollision_Pacman_ShouldReturnFalse() {
        try(MockedStatic<Game> gameMock = mockStatic(Game.class)){
            List<Wall> fakeWalls = setTempWalls();
            entity = new Pacman(10 * cellSize, 6 * cellSize);

            gameMock.when(() -> Game.getWalls()).thenReturn(fakeWalls);

            boolean result = WallCollisionDetector.checkWallCollision(entity, 1, 0);

            assertThat(result).isFalse();
        }
    }

    @Test
    public void testCollision_Ghost_ShouldReturnFalse() {
        try(MockedStatic<Game> gameMock = mockStatic(Game.class)){
            List<Wall> fakeWalls = setTempWalls();
            abstractGhostFactory = new BlinkyFactory();
            //TODO 일단 Blinky 하나만 테스트 함(다른 Ghost 속성이 다르게 테스트 될 경우에는 추가 메서드 작성 필요함
            Entity entity = abstractGhostFactory.makeGhost(10 * cellSize, 6 * cellSize);

            gameMock.when(() -> Game.getWalls()).thenReturn(fakeWalls);

            boolean result = WallCollisionDetector.checkWallCollision(entity, 1, 0);

            assertThat(result).isFalse();
        }
    }
}
