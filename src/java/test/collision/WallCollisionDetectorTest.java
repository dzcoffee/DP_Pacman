package test.collision;

import game.Game;
import game.entities.Entity;
import game.entities.GhostHouse;
import game.entities.Pacman;
import game.entities.Wall;
import game.ghostFactory.AbstractGhostFactory;
import game.ghostFactory.BlinkyFactory;
import game.utils.WallCollisionDetector;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void setUp() throws Exception {

    }

    private List<Wall> setTempWalls() {
        // 기본 Wall만 설치 | 10,10 위치의 벽을 테스트 할 것
        // 벽 사이즈 자체는 20 x 20의 정사각형 형태로 구성됨
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

        return createTestWalls(coordinates);
    }

    private List<Wall> setTempWallsWithGhostHouse() {
        // 기본 Wall와 GhostHouse 설치 | 10,10 위치의 Ghost house를 테스트
        // 벽 사이즈 자체는 20 x 20의 정사각형 형태로 구성됨
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

        // 테두리 벽은 createTestWalls로 생성
        List<Wall> walls = createTestWalls(coordinates);

        // [핵심] (10, 10) 위치에 일반 Wall 대신 GhostHouse를 수동으로 추가
        // (GhostHouse가 Wall을 상속한다고 가정)
        walls.add(new GhostHouse(10 * cellSize, 10 * cellSize));

        return walls;
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

    @Test
    public void testCollision_Ghost_ShouldReturnFalse_WhenIgnoringGhostHouse() {
        // 테스트 2-1 : 고스트가 ignoreHouse = true상태에서 GhostHouse에 충돌 테스트 -> 충돌하지 않아야 함(false)
        try(MockedStatic<Game> gameMock = mockStatic(Game.class)){

            List<Wall> fakeWalls = setTempWallsWithGhostHouse();

            abstractGhostFactory = new BlinkyFactory();
            Entity entity = abstractGhostFactory.makeGhost(10 * cellSize, 6 * cellSize);

            gameMock.when(() -> Game.getWalls()).thenReturn(fakeWalls);

            boolean result = WallCollisionDetector.checkWallCollision(entity, 0, 1, true);

            assertThat(result).isFalse();
        }
    }

    @Test
    public void testCollision_Ghost_ShouldReturnFalse_WhenNotIgnoringGhostHouse() {
        // 테스트 2-2 : 고스트가 ignoreHouse = false 상태에서 GhostHouse에 충돌 테스트 -> 충돌해야 함(true)
        try(MockedStatic<Game> gameMock = mockStatic(Game.class)){

            List<Wall> fakeWalls = setTempWallsWithGhostHouse();

            abstractGhostFactory = new BlinkyFactory();
            Entity entity = abstractGhostFactory.makeGhost(10 * cellSize, 6 * cellSize);

            gameMock.when(() -> Game.getWalls()).thenReturn(fakeWalls);

            boolean result = WallCollisionDetector.checkWallCollision(entity, 0, 1, false);
            // 일반 Wall과 같이 판별되야 한다.

            assertThat(result).isTrue();
        }
    }

    @Test
    public void testCollision_Ghost_ShouldReturnTrue_WhenIgnoringFlagForRegularWall() {
        // 테스트 2-3 : 고스트가 ignoreHouse = true 상태에서 일반 Wall 충돌 테스트 -> 아무 상관 없이 충돌해야 함.
        try(MockedStatic<Game> gameMock = mockStatic(Game.class)){
            // ignoreGhoutHouse가 True여도 일반 Wall에는 충돌해야 함.
            // 일반 벽 List를 이용하였음.
            List<Wall> fakeWalls = setTempWalls();

            abstractGhostFactory = new BlinkyFactory();
            Entity entity = abstractGhostFactory.makeGhost(10 * cellSize, 6 * cellSize);

            gameMock.when(() -> Game.getWalls()).thenReturn(fakeWalls);

            // ignoreGhostHouses = true로 설정하여도 일반 벽(Wall)에 교차하기 때문에 충돌하였음.
            boolean result = WallCollisionDetector.checkWallCollision(entity, 0, 1, true);

            // 충돌 대상이 GhostHouse가 아닌 일반 Wall이므로, 플래그와 상관없이 True를 기대
            assertThat(result).isTrue();
        }
    }
}
