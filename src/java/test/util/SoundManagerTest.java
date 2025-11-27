package test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import game.utils.SoundManager;

public class SoundManagerTest {
    class TestableSoundManager extends SoundManager {
        public List<String> log = new ArrayList<>();

        @Override
        protected void loadSound(String path, String name) {}

        @Override
        protected void playSound(String name) {
            log.add("PLAY: " + name);
        }

        protected void stopSound(String name) {
            log.add("STOP: " + name);
        }

        @Override
        protected void startLoopIfNotRunning(String name) {
            log.add("LOOP_START: " + name);
        }

        @Override
        protected void stopLoop(String name) {
            log.add("LOOP_STOP: " + name);
        }
    }

    private TestableSoundManager soundManager;

    @Before
    public void setUp() {
        soundManager = new TestableSoundManager();
    }

    // 팩검을 먹을 때 뽀까뽀까 소리확인
    @Test
    public void testPacGumSoundToggle() {
        soundManager.playPacGumSound();
        assertEquals("pacgum1", "PLAY: pacgum1", soundManager.log.get(0));
        soundManager.playPacGumSound();
        assertEquals("pacgum2", "PLAY: pacgum2", soundManager.log.get(1));
        soundManager.playPacGumSound();
        assertEquals("pacgum1", "PLAY: pacgum1", soundManager.log.get(2));
    }

    // 사이렌을 끄고 공포음을 켰는지 확인
    @Test
    public void testFrightLoopStart() {
        soundManager.playFrightLoop();
        assertTrue(soundManager.log.contains("LOOP_STOP: siren_loop_1"));
        assertTrue(soundManager.log.contains("LOOP_START: fright_loop"));
    }

    // 공포음을 끄고 사이렌을 켰는지 확인
    @Test
    public void testFrightLoopStop() {
        soundManager.stopFrightLoop();
        assertTrue(soundManager.log.contains("LOOP_STOP: fright_loop"));
        assertTrue(soundManager.log.contains("LOOP_START: siren_loop_1"));
    }

    // superPacgum 먹었을 때 소리 확인
    @Test
    public void testEatenLoop() {
        soundManager.playEatenLoop();
        assertEquals("LOOP_START: eaten_loop", soundManager.log.get(0));
    }
}
