package sounds;

import java.awt.Toolkit;
import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {

    // Variabel untuk menyimpan BGM agar bisa di-loop dan dihentikan
    private static Clip bgmClip;

    private static java.net.URL getSoundURL(String soundPath) {
        // 1. Coba memuat dari classpath resource
        java.net.URL url = SoundManager.class.getResource("/assets/audio/" + soundPath);
        if (url != null) {
            return url;
        }
        // 2. Coba memuat dari folder fisik "src/assets/audio/"
        File fileSrc = new File("src/assets/audio/" + soundPath);
        if (fileSrc.exists()) {
            try {
                return fileSrc.toURI().toURL();
            } catch (Exception e) {
                // Ignore
            }
        }
        // 3. Coba memuat dari folder fisik "assets/audio/"
        File fileDirect = new File("assets/audio/" + soundPath);
        if (fileDirect.exists()) {
            try {
                return fileDirect.toURI().toURL();
            } catch (Exception e) {
                // Ignore
            }
        }
        return null;
    }

    public static void play(String soundName) {
        System.out.println("[AUDIO SFX] Memainkan: " + soundName);
        try {
            java.net.URL soundURL = getSoundURL(soundName + ".wav");

            // putar file wav
            if (soundURL != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                // ===== FITUR 1: SENSOR ANTI-LAG / MEMORY LEAK =====
                // Otomatis menghancurkan SFX dari memori RAM setelah suaranya habis
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        event.getLine().close();
                    }
                });
                // ==================================================

                clip.start();
            }
            else if (soundName.equals("hit") || soundName.equals("bump") || soundName.equals("error")) {
                // Suara cadangan jika file .wav tidak ditemukan
                Toolkit.getDefaultToolkit().beep();
            }
        } catch (Exception e) {
            System.out.println("Error play SFX: " + e.getMessage());
        }
    }

    public static void playBGM(String bgmName) {
        System.out.println("[AUDIO BGM] Memutar musik latar: " + bgmName);
        try {
            // Matikan BGM sebelumnya (jika ada) sebelum memutar yang baru
            stopBGM();

            java.net.URL soundURL = getSoundURL(bgmName + ".wav");
            if (soundURL != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioIn);

                // ===== FITUR 2: AUDIO MIXING (PENGATUR VOLUME BGM) =====
                // Menurunkan volume BGM agar SFX (langkah kaki/pukulan) tetap terdengar jelas.
                // Ubah angka -15.0f jika dirasa masih terlalu keras atau terlalu pelan.
                FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-15.0f);
                // =======================================================

                bgmClip.loop(Clip.LOOP_CONTINUOUSLY); // Perintah memutar terus-menerus
                bgmClip.start();
            } else {
                System.out.println("[ERROR] File BGM tidak ditemukan: " + bgmName);
            }
        } catch (Exception e) {
            System.out.println("Error play BGM: " + e.getMessage());
        }
    }

    // TOMBOL MATIKAN BGM
    public static void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close(); // Membersihkan memori BGM saat dimatikan
        }
    }
}