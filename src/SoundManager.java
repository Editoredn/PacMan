package src;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private Clip currentMusicClip;
    private final Map<String, Clip> soundCache = new HashMap<>();
    private float volume = 1.0f;
    public float getVolume() {
        return volume;
    }

    public void playThemeMusic(int themeNumber) {
        if (themeNumber < 1 || themeNumber > 9) {
            throw new IllegalArgumentException("Theme number must be between 1 and 9");
        }

        stopThemeMusic();

        String musicPath = String.format("src/sounds/theme%d/theme%d_music.wav", themeNumber, themeNumber);
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicPath))) {
            currentMusicClip = AudioSystem.getClip();
            currentMusicClip.open(audioStream);
            setClipVolume(currentMusicClip, volume);
            currentMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            currentMusicClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing theme music: " + e.getMessage());
        }
    }

    public void stopThemeMusic() {
        if (currentMusicClip != null && currentMusicClip.isRunning()) {
            currentMusicClip.stop();
            currentMusicClip.close();
        }
    }

    private void playSoundEffect(String path) {
        try {
            Clip clip = soundCache.get(path);
            if (clip == null || !clip.isOpen()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                soundCache.put(path, clip);
            }

            if (clip.isRunning()) {
                clip.stop();
            }

            clip.setFramePosition(0);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing sound effect: " + e.getMessage());
        }
    }

    public void playEatSound() {
        playSoundEffect("src/sounds/effects/eat.wav");
    }

    public void playDeathSound() {
        playSoundEffect("src/sounds/effects/die.wav");
    }

    public void playGameOverSound() {
        playSoundEffect("src/sounds/effects/gameover.wav");
    }

    public void playCollectCoinsSound() {
        playSoundEffect("src/sounds/effects/start.wav");
    }

    public void playElectricShockSound() {
        playSoundEffect("src/sounds/effects/electricshock.wav");
    }

    public void setVolume(float volume) {
        if (volume < 0.0f || volume > 1.0f) {
            throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
        }
        this.volume = volume;
        if (currentMusicClip != null && currentMusicClip.isOpen()) {
            setClipVolume(currentMusicClip, volume);
        }
    }

    private void setClipVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = 20f * (float) Math.log10(volume == 0.0f ? 0.0001f : volume);
            gain.setValue(dB);
        }
    }

    public void cleanup() {
        stopThemeMusic();
        for (Clip clip : soundCache.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        }
        soundCache.clear();
    }
}