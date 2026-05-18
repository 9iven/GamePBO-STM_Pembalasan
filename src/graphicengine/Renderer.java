package graphicengine;

import mapping.Map;
import actors.mainactors.Player;
import actors.roles.Enemy;
import java.awt.*;
import java.util.ArrayList;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

// Class untuk mengurus semua proses rendering (penggambaran pixel) ke layar
public class Renderer {
    private final Color COLOR_UI_LINE = new Color(100, 100, 255);
    private final Color COLOR_TEXT = new Color(200, 200, 255);
    private BufferedImage imgDepan, imgBelakang, imgKanan, imgKiri;
    private BufferedImage imgRumput, imgKelasLantai;

    public Renderer() {
        try {
            // Memuat gambar dari folder resources
            imgDepan = ImageIO.read(getClass().getResourceAsStream("/assets/karakter/mc_depan.png"));
            imgBelakang = ImageIO.read(getClass().getResourceAsStream("/assets/karakter/mc_belakang.png"));
            imgKanan = ImageIO.read(getClass().getResourceAsStream("/assets/karakter/mc_samping_kanan.png"));
            imgKiri = ImageIO.read(getClass().getResourceAsStream("/assets/karakter/mc_samping_kiri.png"));
            imgRumput = ImageIO.read(getClass().getResourceAsStream("/assets/lantai/rumput.png"));
            imgKelasLantai = ImageIO.read(getClass().getResourceAsStream("/assets/lantai/kelas_lantai.png"));
        } catch (IOException | NullPointerException e) {
            System.out.println("Gagal memuat gambar karakter: " + e.getMessage());
        }
    }

    public void drawWorld(Graphics g, Map map, Player player, ArrayList<Enemy> enemies, int uiWidth) {
        int ox = uiWidth + 10, oy = 20, ts = map.TILE_SIZE;
        int[][] grid = map.getCurrentMap();

        // Melakukan iterasi array 2D untuk menggambar lingkungan
        // Melakukan iterasi array 2D untuk menggambar lingkungan
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                int px = ox + (c * ts), py = oy + (r * ts);

                if (grid[r][c] == 0) {
                    // === LOGIKA GAMBAR LANTAI ===
                    // Level 0 dan 1 menggunakan gambar rumput
                    if (map.currentLevel == 0) {
                        if (imgRumput != null) {
                            g.drawImage(imgRumput, px, py, ts, ts, null);
                        } else {
                            g.setColor(new Color(20, 20, 30));
                            g.fillRect(px, py, ts, ts);
                        }
                    }
                    // Level 2 dan seterusnya menggunakan lantai kelas
                    else {
                        if (imgKelasLantai != null) {
                            g.drawImage(imgKelasLantai, px, py, ts, ts, null);
                        } else {
                            g.setColor(new Color(20, 20, 30));
                            g.fillRect(px, py, ts, ts);
                        }
                    }
                    // ==============================
                }
                else if (grid[r][c] == 1) { g.setColor(new Color(75, 0, 130)); g.fillRect(px, py, ts, ts); g.setColor(Color.BLACK); g.drawRect(px, py, ts, ts); }
                else if (grid[r][c] == 2) { g.setColor(new Color(150, 0, 0)); g.fillRect(px, py, ts, ts); g.setColor(Color.WHITE); g.drawString("OUT", px+15, py+30);}
            }
        }


        // Menggambar entitas musuh
        g.setColor(new Color(255, 105, 180));
        for (Enemy e : enemies) {
            if (!e.isDefeated) { g.fillRect(ox + e.x * ts + 8, oy + e.y * ts + 8, 32, 32); }
        }

        // Menggambar posisi pemain
        // Menggambar posisi pemain
        int px = ox + player.x * ts, py = oy + player.y * ts;

        // --- LOGIKA BARU UNTUK GAMBAR KARAKTER ---
        BufferedImage currentSprite = imgDepan; // Set default menghadap depan

        // Cek arah menggunakan variabel facingX dan facingY dari class Player
        if (player.facingY == -1) {
            currentSprite = imgBelakang;  // Bergerak ke atas
        } else if (player.facingY == 1) {
            currentSprite = imgDepan;     // Bergerak ke bawah
        } else if (player.facingX == -1) {
            currentSprite = imgKiri;      // Bergerak ke kiri
        } else if (player.facingX == 1) {
            currentSprite = imgKanan;     // Bergerak ke kanan
        }

        // Gambar sprite jika berhasil dimuat, jika gagal kembalikan ke bentuk kotak
        if (currentSprite != null) {
            // Menggambar gambar menyesuaikan posisi (px, py) dan ukurannya (ts)
            g.drawImage(currentSprite, px, py, ts, ts, null);
        } else {
            // Kode asli Anda sebagai cadangan (fallback)
            g.setColor(Color.WHITE);
            g.fillRect(px + 8, py + 8, 32, 32);
            g.setColor(Color.YELLOW);
            g.drawLine(px + ts/2, py + ts/2, px + ts/2 + (player.facingX * 24), py + ts/2 + (player.facingY * 24));
        }
        // -----------------------------------------

        // Indikator arah hadap pemain
        g.setColor(Color.YELLOW);
        g.drawLine(px + ts/2, py + ts/2, px + ts/2 + (player.facingX * 24), py + ts/2 + (player.facingY * 24));
    }

    public void drawUI(Graphics g, Player player, int level, ArrayList<String> logs, int uiWidth, int height) {
        g.setColor(COLOR_UI_LINE);
        g.drawRect(5, 5, uiWidth - 10, height - 10);
        g.drawLine(5, 75, uiWidth - 5, 75);

        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.setColor(COLOR_TEXT);
        g.drawString("LOKASI: Lantai " + level, 15, 25);
        g.drawString("HP    : " + player.getHp() + "/" + player.MAX_HP, 15, 45);
        g.drawString("KOIN  : " + player.currency + " | EXP: " + player.getExperience(), 15, 65);

        g.drawString("[ WASD - Move ]", 15, 180);
        g.drawString("[ Space - Interact ]", 15, 200);
        g.drawString("[ LOG ]", 15, 220);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Logika untuk word wrapping (memecah string panjang agar tidak keluar batas panel)
        int drawY = 245;
        for (String log : logs) {
            if (log.length() > 30) {
                g.drawString("> " + log.substring(0, 30), 15, drawY);
                drawY += 15;
                g.drawString("  " + log.substring(30), 15, drawY);
            } else {
                g.drawString("> " + log, 15, drawY);
            }
            drawY += 15;
        }
    }

    public void drawCombat(Graphics g, Enemy e, double timer, int clash, int selection, String msg, int uiWidth) {
        int ox = uiWidth + 30;
        g.setColor(new Color(255, 105, 180));
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString(e.getName() + " berkata:", ox, 60);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.ITALIC, 14));
        g.drawString(e.currentDialogue, ox, 90);

        // --- AWAL PENAMBAHAN KODE TELEGRAPHING ---
        g.setColor(new Color(255, 165, 0)); // Warna Oranye
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        String indikator = "";
        if (e.currentIntent == 0) indikator = "[ INDIKATOR: Musuh mengepalkan tangan (Pukulan) ]";
        else if (e.currentIntent == 1) indikator = "[ INDIKATOR: Musuh memundurkan kaki (Tendangan) ]";
        else if (e.currentIntent == 2) indikator = "[ INDIKATOR: Musuh menyilangkan lengan (Tangkisan) ]";
        g.drawString(indikator, ox, 120);
        // --- AKHIR PENAMBAHAN KODE TELEGRAPHING ---

        if (timer > 0) {
            g.setColor(timer < 1.5 ? Color.RED : Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            // Posisi Y untuk waktu diturunkan sedikit agar tidak bertumpuk dengan indikator
            g.drawString(String.format("WAKTU: %.1f DETIK", timer), ox, 160);
            g.setColor(COLOR_TEXT);
            g.setFont(new Font("Monospaced", Font.PLAIN, 16));
            g.drawString("Pilih respons Anda (A/D & SPASI):", ox, 220);

            String[] opts = {"[0] Pukulan", "[1] Tendangan", "[2] Tangkisan"};
            for (int i = 0; i < 3; i++) {
                g.setColor(selection == i ? Color.YELLOW : COLOR_TEXT);
                g.drawString((selection == i ? ">> " : "   ") + opts[i], ox + 30, 260 + (i * 30));
            }
        } else {
            g.setColor(COLOR_TEXT);
            g.setFont(new Font("Monospaced", Font.PLAIN, 16));
            g.drawString(msg, ox, 220);
            g.setColor(Color.YELLOW);
            g.drawString("[ TEKAN SPASI UNTUK LANJUT ]", ox, 300);
        }
    }
}