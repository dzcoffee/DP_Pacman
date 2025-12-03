package test.collision;

import game.Game;
import game.entities.Entity;
import game.entities.Pacman;
import game.entities.ghosts.Blinky;
import game.entities.ghosts.Clyde;
import game.entities.ghosts.Ghost;
import game.utils.CollisionDetector;
import org.junit.Before;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CollisionDetectorTest {

    @Mock
    private Game game; // game.getEntities() 모킹용
    // 더불어서 기존 game을 대체하여 임시 game 객체(단순화 됨)를 통해 테스트 진행
    // TODO 아래 메서드들은 모두 충돌을 확인하는 요소, Eaten Mode이거나 충돌 이후의 로직은 별도로 작성 필요

    private CollisionDetector detector;

    private Pacman pacman;
    private Ghost intersectGhost; // 충돌 하는 일반 고스트
    private Ghost unIntersectGhost; //충돌하지 않는 고스트
    private List<Entity> entities;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        detector = new CollisionDetector(game); // 모킹해둔 game을 객체에 주입

        entities = new ArrayList<>();
    }

    @Test
    public void testCheckCollision_PacmanCenterInGhost() {
        // 테스트 1 -1 : pacman의 중심이 히트박스 내부에 위치함.

        pacman = new Pacman(100, 100); // 위치 (100, 100), 중심 (116, 116)
        intersectGhost = new Blinky(100, 100);  // 위치 (100, 100), 히트박스 [100, 131]
        unIntersectGhost = new Clyde(20, 20); // 위치 (20, 20), 히트박스 [20, 51]

        // 게임 내의 entities에 고스트 추가
        entities.add(intersectGhost);  //충돌 고스트
        entities.add(unIntersectGhost); // 충돌하지 않는 고스트

        when(game.getEntities()).thenReturn(entities);

        // 이 함수는 pacman의 중심이 Ghost의 히트박스(위치 + size) 내부에 위치하는 지 판별함
        Entity result = detector.checkCollision(pacman, Ghost.class);

        assertThat(result).isEqualTo(intersectGhost); // 충돌 (고스트 반환)
    }

    @Test
    public void testCheckCollision_PacmanCenterInGhost_isDestroyed() {
        // 테스트 1 - 2 : pacman의 중심이 히트박스 내부에 위치하지만 destroy된 상태라 충돌하지 않음

        pacman = new Pacman(100, 100); // 위치 (100, 100), 중심 (116, 116)
        intersectGhost = new Blinky(100, 100);  // 위치 (100, 100), 히트박스 [100, 131]
        unIntersectGhost = new Clyde(20, 20); // 위치 (20, 20), 히트박스 [20, 51]

        // 출돌해야 하는 고스트를 destroy 상태로
        intersectGhost.destroy();

        // 게임 내의 entities에 고스트 추가
        entities.add(intersectGhost);  //충돌 고스트
        entities.add(unIntersectGhost); // 충돌하지 않는 고스트

        when(game.getEntities()).thenReturn(entities);

        Entity result = detector.checkCollision(pacman, Ghost.class);

        assertThat(result).isEqualTo(null); // 충돌하는 것 없음 (null 반환)
    }

    @Test
    public void testCheckCollision_PacmanCenterNotInGhost() {
        // 테스트 1 - 3 : pacman의 중심이 히트박스 바깥에 존재

        pacman = new Pacman(300, 300); // 위치 (300, 300), 중심 (316, 316)
        intersectGhost = new Blinky(100, 100);  // 위치 (100, 100), 히트박스 [100, 131]
        unIntersectGhost = new Clyde(20, 20); // 위치 (20, 20), 히트박스 [20, 51]

        // 게임 내의 entities에 고스트 추가
        entities.add(intersectGhost);  //충돌하지 않는 고스트
        entities.add(unIntersectGhost); // 충돌하지 않는 고스트

        when(game.getEntities()).thenReturn(entities);

        Entity result = detector.checkCollision(pacman, Ghost.class);

        assertThat(result).isEqualTo(null); // 충돌하는 것 없음 (null 반환)
    }

    @Test
    public void testCheckCollision_PacmanCenterInBorderOfGhost() {
        // 테스트 1 - 4 : pacman의 중심이 히트박스 경계선에 존재함 | 경계선 = Pos + size - 1

        pacman = new Pacman(115, 115); // 위치 (115, 115), 중심 (131, 131)
        intersectGhost = new Blinky(100, 100);  // 위치 (100, 100), 히트박스 [100, 131]
        unIntersectGhost = new Clyde(20, 20); // 위치 (20, 20), 히트박스 [20, 51]

        // 게임 내의 entities에 고스트 추가
        entities.add(intersectGhost);  //충돌 고스트
        entities.add(unIntersectGhost); // 충돌하지 않는 고스트

        when(game.getEntities()).thenReturn(entities);

        // 이 함수는 pacman의 중심이 Ghost의 히트박스(위치 + size) 내부에 위치하는 지 판별함
        Entity result = detector.checkCollision(pacman, Ghost.class);

        assertThat(result).isEqualTo(intersectGhost); // 충돌 (고스트 반환)
    }

    @Test
    public void testCheckCollision_PacmanCenterOutBorderOfGhost() {
        // 테스트 1 - 4 : pacman의 중심이 히트박스 경계선에 바깥에 존재함 | 경계선 = Pos + size - 1

        pacman = new Pacman(116, 116); // 위치 (115, 115), 중심 (131, 131)
        intersectGhost = new Blinky(100, 100);  // 위치 (100, 100), 히트박스 [100, 131]
        unIntersectGhost = new Clyde(20, 20); // 위치 (20, 20), 히트박스 [20, 51]

        // 게임 내의 entities에 고스트 추가
        entities.add(intersectGhost);  //충돌 고스트
        entities.add(unIntersectGhost); // 충돌하지 않는 고스트

        when(game.getEntities()).thenReturn(entities);

        // 이 함수는 pacman의 중심이 Ghost의 히트박스(위치 + size) 내부에 위치하는 지 판별함
        Entity result = detector.checkCollision(pacman, Ghost.class);

        assertThat(result).isEqualTo(null); // 충돌하지 않음 (null 반환)
    }

    @Test
    public void testCheckCollisionRect_PacmanIntersectGhost() {
        // 테스트 2 : 일반 Entity와 같이 조금이라도 교차하는 지 검증 -> 이 메서드는 사용중이 지 않기에 간단한 코드만 작성
        // 중심 점에는 겹치지 않지만 히트박스에는 교차되어 충돌 판정하는 것이 특징임
        // TODO 만약 이 메서드를 사용할 경우 테스트 코드 추가 작성

        pacman = new Pacman(100, 100); // 위치 (100, 100), 히트박스 [100, 131], 중심 (116, 116)
        intersectGhost = new Blinky(130, 130);  // 위치 (130, 130), 히트박스 [130, 161]
        unIntersectGhost = new Clyde(20, 20); // 위치 (20, 20), 히트박스 [20, 51]

        // 게임 내의 entities에 고스트 추가
        entities.add(intersectGhost);  //충돌 고스트
        entities.add(unIntersectGhost); // 충돌하지 않는 고스트

        when(game.getEntities()).thenReturn(entities);

        // 이 함수는 pacman의 중심이 Ghost의 히트박스(위치 + size) 내부에 위치하는 지 판별함
        Entity result = detector.checkCollisionRect(pacman, Ghost.class);

        assertThat(result).isEqualTo(intersectGhost); // 충돌함 (intersectGhost 반환)
    }

}
