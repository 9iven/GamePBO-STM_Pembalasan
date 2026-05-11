package sounds;
import java.awt.Toolkit;

// Class statis untuk mengelola efek audio tanpa perlu instansiasi objek
public class SoundManager {
    public static void play(String soundName) {
        System.out.println("[AUDIO SFX] Memainkan: " + soundName);
        if (soundName.equals("hit") || soundName.equals("bump") || soundName.equals("error")) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}