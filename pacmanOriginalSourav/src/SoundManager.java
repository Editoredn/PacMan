package src;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public class SoundManager {

    private Clip themeMusicClip;
    private Map<String, Clip> effectClips;
    private int currentLevel = 1; 
    private String currentThemeFolder;
    private Map<String, Clip> activeEffectClips = new HashMap<>();

    public SoundManager(int startLevel) {
        this.currentLevel = startLevel;
        this.effectClips = new HashMap<>();
        this.currentThemeFolder = getThemeFolderForLevel(currentLevel);
        // loadThemeMusic();
        loadEffectSounds();
    }

    private String getThemeFolderForLevel(int level) {
        if (level >= 1 && level <= 5)
            return "theme1";
        else if (level >= 6 && level <= 10)
            return "theme2";
        else if (level >= 11 && level <= 14)
            return "theme3";
        else if (level == 15)
            return "theme4";
        else if (level >= 16 && level <= 20)
            return "theme5";
        else if (level >= 21 && level <= 24)
            return "theme6";
        else if (level >= 25)
            return "theme7";
        else if (level >= 26 && level <= 30)
            return "theme8";
        else if (level >= 31 && level <= 35)
            return "theme9";
        else
            return "default";
    }

    public void loadThemeMusic() {
        System.out.println("CURRENT WORKING DIRECTORY : " + System.getProperty("user.dir"));
        stopThemeMusic();

        currentThemeFolder = getThemeFolderForLevel(currentLevel);
        String musicPath = "src/sounds/" + currentThemeFolder + "/theme_music.wav";
        File musicFile = new File(musicPath);

        if (!musicFile.exists()) {
            System.err.println("THEME MUSIC FILE NOT FOUND : " + musicPath);
            return;
        }

        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
            themeMusicClip = AudioSystem.getClip();
            themeMusicClip.open(audioIn);
            themeMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            themeMusicClip.start();
        } catch (Exception e) {
            System.err.println("FAILED TO LOAD THEME MUSIC  : " + e.getMessage());
        }
    }

    
    public void stopThemeMusic() {
        if (themeMusicClip != null && themeMusicClip.isRunning()) {
            themeMusicClip.stop();
            themeMusicClip.close();
        }
    }

    private void loadEffectSounds() {
        loadEffectClip("eat", "effects/eat.wav");
        loadEffectClip("LandingPage", "effects/LandingPage.wav");
        loadEffectClip("die", "effects/die.wav");
        loadEffectClip("start", "effects/start.wav");
        loadEffectClip("gameover", "effects/gameover.wav");
        loadEffectClip("theme1", "effects/theme1music.wav");
        loadEffectClip("theme2", "effects/theme2music.wav");
        loadEffectClip("theme3", "effects/theme3music.wav");
        loadEffectClip("theme4", "effects/theme4music.wav");
        loadEffectClip("theme5", "effects/theme5music.wav");
        loadEffectClip("theme6", "effects/theme6music.wav");
        loadEffectClip("theme7", "effects/theme7music.wav");
        loadEffectClip("theme8", "effects/theme8music.wav");
        loadEffectClip("theme9", "effects/theme9music.wav");
        loadEffectClip("electricshock", "effects/electricshock.wav");
    }

    private void loadEffectClip(String key, String relativePath) {
        try {
            File soundFile = new File("src/sounds/" + relativePath);
            if (!soundFile.exists()) {
                System.err.println("EFFECT SOUND FILE NOT FOUND : sounds/" + relativePath);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            effectClips.put(key, clip);
        } catch (Exception e) {
            System.err.println("FAILED TO LOAD EFFECT SOUND : " + relativePath + " - " + e.getMessage());
        }
    }

    public void playEffect(String key) {
        String relativePath = getEffectPath(key);

        if (relativePath == null) {
            System.err.println("NO PATH FOUND FOR EFFECT KEY : " + key);
            return;
        }

        try {
            File soundFile = new File("src/sounds/" + relativePath);
            if (!soundFile.exists()) {
                System.err.println("EFFECT SOUND FILE NOT FOUND : " + soundFile.getPath());
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip newClip = AudioSystem.getClip();
            newClip.open(audioIn);
            newClip.start();

            if (key.equals("LandingPage") || key.startsWith("theme")) {
                activeEffectClips.put(key, newClip);
            }

            System.out.println("PLAYING EFFECT : " + key);
        } catch (Exception e) {
            System.err.println("ERROR PLAYING EFFECT '" + key + "': " + e.getMessage());
        }
    }

    public void stopEffect(String key) {
        Clip clip = activeEffectClips.get(key);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            activeEffectClips.remove(key);
            System.out.println("STOPPED EFFECT : " + key);
        }
    }

    private String getEffectPath(String key) {
        switch (key) {
            case "eat":
                return "effects/eat.wav";
            case "LandingPage":
                return "effects/LandingPage.wav";
            case "die":
                return "effects/die.wav";
            case "start":
                return "effects/start.wav";
            case "gameover":
                return "effects/gameover.wav";
            case "electricshock":
                return "effects/electricshock.wav";
            case "theme1":
                return "effects/theme1music.wav";
            case "theme2":
                return "effects/theme2music.wav";
            case "theme3":
                return "effects/theme3music.wav";
            case "theme4":
                return "effects/theme4music.wav";
            case "theme5":
                return "effects/theme5music.wav";
            case "theme6":
                return "effects/theme6music.wav";
            case "theme7":
                return "effects/theme7music.wav";
            case "theme8":
                return "effects/theme8music.wav";
            case "theme9":
                return "effects/theme9music.wav";
            default:
                return null;
        }
    }

    public void setLevel(int newLevel) {
        if (newLevel != currentLevel) {
            currentLevel = newLevel;
            loadThemeMusic(); 
        }
    }

    
    public void cleanup() {
        stopThemeMusic();
        for (Clip clip : effectClips.values()) {
            if (clip.isRunning())
                clip.stop();
            clip.close();
        }

        activeEffectClips.clear();
    }



    public static void main(String[] args) {
        SoundManager sm = new SoundManager(1);
        System.out.println("PLAYING THEME MUSIC FOR LEVEL 1...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        System.out.println("PLAYING 'EAT' EFFECT SOUND...");
        sm.playEffect("eat");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        System.out.println("CHANGING TO LEVEL 6 (THEME2)...");
        sm.setLevel(6);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        System.out.println("PLAYING 'ELECTRICSHOCK' EFFECT SOUND...");
        sm.playEffect("electricshock");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }

        sm.cleanup();
        System.out.println("SOUNDS CLEANED UP. EXITING.");
    }
}
