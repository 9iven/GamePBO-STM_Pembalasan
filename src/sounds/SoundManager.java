package sounds;

import java.awt.Toolkit;
import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {

    // Variabel untuk menyimpan BGM agar bisa di-loop dan dihentikan
    private static Clip bgmClip;

    public static void play(String soundName) {
        System.out.println("[AUDIO SFX] Memainkan: " + soundName);
        try {
            File soundFile = new File("src/assets/audio/" + soundName + ".wav");

            // putar file wav
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
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

            File soundFile = new File("src/assets/audio/" + bgmName + ".wav");
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
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