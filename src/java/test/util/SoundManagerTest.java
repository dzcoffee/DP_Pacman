package test.util;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Map;
import javax.sound.sampled.Clip;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import game.utils.SoundManager;

public class SoundManagerTest {

    private SoundManager soundManager;
    private Clip mockClip1;
    private Clip mockClip2;
    private Clip mockFrightClip;
    private Clip mockSirenClip;
    private Clip mockGameStartClip;
    private Clip mockEatenClip;

    private Map<String, Clip> injectedMap;

    @Before
    public void setUp() throws Exception {
        soundManager = SoundManager.getInstance();
        Field clipsField = SoundManager.class.getDeclaredField("soundClips");
        clipsField.setAccessible(true); // private 접근 허용

        // 3. 실제 Map 객체 꺼내기
        @SuppressWarnings("unchecked")
        Map<String, Clip> realMap = (Map<String, Clip>) clipsField.get(soundManager);
        this.injectedMap = realMap;

        mockClip1 = mock(Clip.class);
        mockClip2 = mock(Clip.class);
        mockFrightClip = mock(Clip.class);
        mockSirenClip = mock(Clip.class);
        mockGameStartClip = mock(Clip.class);
        mockEatenClip = mock(Clip.class);
        
        realMap.put("pacgum1", mockClip1);
        realMap.put("pacgum2", mockClip2);
        realMap.put("fright_loop", mockFrightClip);
        realMap.put("siren_loop_1", mockSirenClip);
        realMap.put("game_start", mockGameStartClip);
        realMap.put("eaten_loop", mockEatenClip);

        Field stateField = SoundManager.class.getDeclaredField("pacGumSoundState");
        stateField.setAccessible(true);
        stateField.setInt(soundManager, 1);
    }
    
    @After
    public void tearDown() {// 테스트 간 간섭 방지
        reset(mockClip1, mockClip2, mockFrightClip, mockSirenClip, mockGameStartClip, mockEatenClip);
    }

    // 팩검 소리 토글 로직 검증
    @Test
    public void testPacGumSoundToggle() {
         // 첫 번째 호출
        soundManager.playPacGumSound();
        verify(mockClip1, times(1)).start();// 검증: pacgum1 클립이 start() 되었는가?
        verify(mockClip2, never()).start(); // 2번은 실행 안 돼야 함

        clearInvocations(mockClip1, mockClip2); // Mockito 상태 초기화 (다음 검증을 위해)

         // 두 번째 호출
        soundManager.playPacGumSound();
        // pacgum2가 실행되어야 함
        verify(mockClip1, never()).start();
        verify(mockClip2, times(1)).start();

        clearInvocations(mockClip1, mockClip2);

         // 세 번째 호출
        soundManager.playPacGumSound();
         // 다시 pacgum1 실행
        verify(mockClip1, times(1)).start();
        verify(mockClip2, never()).start();
    }

    // 게임 시작 시 소리 검증
    @Test
    public void testGameStart() {
        soundManager.gameStart();
        verify(mockGameStartClip, times(1)).start();
        verify(mockSirenClip, times(1)).loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Frighten Mode 소리 검증
    @Test
    public void testFrightLoop() {
        when(mockSirenClip.isRunning()).thenReturn(true);
        soundManager.playFrightLoop();
        verify(mockSirenClip, times(1)).stop();
        verify(mockFrightClip, times(1)).loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    // Frighten 모드 종료 시 배경음 복구 검증
    @Test
    public void testStopFrightLoop() {
        when(mockFrightClip.isRunning()).thenReturn(true);
        soundManager.stopFrightLoop();
        verify(mockFrightClip, times(1)).stop();
        verify(mockSirenClip, times(1)).loop(Clip.LOOP_CONTINUOUSLY);
    }

    // 유령 먹힘 소리 검증
    @Test
    public void testEatenLoop() {
        soundManager.playEatenLoop();
        verify(mockEatenClip, times(1)).loop(Clip.LOOP_CONTINUOUSLY);
    }

    // 유령 먹힘 소리 종료 검증
    @Test
    public void testStopEatenLoop() {
        when(mockEatenClip.isRunning()).thenReturn(true);
        soundManager.stopEatenLoop();  
        verify(mockEatenClip, times(1)).stop();
    }
}