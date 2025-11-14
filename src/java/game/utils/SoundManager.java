package game.utils;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private Map<String, Clip> soundClips;
    private int pacGumSoundState = 1;
    public SoundManager() {
        try {
            soundClips = new HashMap<>();
            loadSound("sounds/pacgum_eat_1.wav", "pacgum1");
            loadSound("sounds/pacgum_eat_2.wav", "pacgum2");
            loadSound("sounds/ghost_eat.wav", "ghost");
            loadSound("sounds/siren_loop_1.wav", "siren_loop_1");
            loadSound("sounds/fright_loop.wav", "fright_loop");
            loadSound("sounds/eaten_loop.wav", "eaten_loop");
            loadSound("sounds/game_start.wav", "game_start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSound(String path, String name) {
    try {
        InputStream audioInStream = SoundManager.class.getResourceAsStream("/" + path);
        if (audioInStream == null) {
            System.err.println("리소스를 찾을 수 없습니다: /" + path);
                return;
            }
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(audioInStream));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        soundClips.put(name, clip);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void playSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null && !clip.isRunning()) {
                clip.setFramePosition(0);
                clip.start();
        }
    }

    private void stopSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    private void startLoopIfNotRunning(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private void stopLoop(String name) {
        stopSound(name);
    }

    public void playPacGumSound() {
        if (pacGumSoundState == 1) {
            playSound("pacgum1");
            pacGumSoundState = 2;
        } else {
            playSound("pacgum2");
            pacGumSoundState = 1;
        }
    }

    public void playFrightLoop() {
        stopLoop("siren_loop_1");
        startLoopIfNotRunning("fright_loop");
    }

    public void playEatenLoop() {
        startLoopIfNotRunning("eaten_loop");
    }

    public void stopFrightLoop() {
        stopLoop("fright_loop");
        startLoopIfNotRunning("siren_loop_1");
    }

    public void stopEatenLoop() {
        stopLoop("eaten_loop");
    }

    public void gameStart() {
        playSound("game_start");
        startLoopIfNotRunning("siren_loop_1");
    }
}
