package test.util;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import game.Game;
import game.entities.PacGum;
import game.entities.ghosts.Blinky;
import game.level.LevelManager;
import game.score.ScoreManager;
import game.utils.SoundManager;

public class GameSoundTest {

    @Mock
    private SoundManager mockSoundManager; // 가짜 스피커
    @Mock
    private LevelManager mockLevelManager; // init에 필요
    @Mock
    private ScoreManager mockScoreManager; // init에 필요
    
    private Game game;
    private SoundManager originalInstance; // 백업용

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        game = new Game();
        game.init(mockLevelManager, mockScoreManager, mockSoundManager);
    }
    
    @After
    public void tearDown() {
    }

    // 유령이 공포 모드가 되면 공포 음악 재생 요청이 가야 한다.
    @Test
    public void testMediator_FrightenedMode() {
        Blinky blinky = new Blinky(0, 0);
        blinky.setMediator(game);
        blinky.switchFrightenedMode();
        verify(mockSoundManager, times(1)).playFrightLoop();
    }
    
    // 팩검을 먹으면 팩검 소리 재생 요청이 가야 한다.
    @Test
    public void testPacGumEaten() {
        PacGum pg = new PacGum(0, 0);
        game.updatePacGumEaten(pg);
        verify(mockSoundManager, times(1)).playPacGumSound();
    }

    // 유령을 먹으면 배경음이 변경되어야한다.
    @Test
    public void testGhostEaten() {
        Blinky blinky = new Blinky(0, 0);
        blinky.setMediator(game);
        blinky.switchEatenMode(); 
        verify(mockSoundManager, times(1)).playEatenLoop();
    }

    // 유령이 집에 도착하면 소리를 끄라고 해야 한다.
    @Test
    public void testGhostArrivedHome() {
        Blinky blinky = new Blinky(0, 0);
        blinky.setMediator(game);
        blinky.switchHouseMode(); 
        verify(mockSoundManager, times(1)).stopEatenLoop();
    }
}