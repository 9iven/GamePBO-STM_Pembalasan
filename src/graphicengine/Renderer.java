package graphicengine;

import mapping.Map;
import actors.mainactors.Player;
import actors.roles.Enemy;
import java.awt.*;
import java.util.ArrayList;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

// Class untuk mengurus semua proses rendering (penggambaran pixel) ke layar
public class Renderer {
    // --- MODIFIKASI TEMA ORANYE ---
    private final Color COLOR_UI_LINE = new Color(255, 140, 0); // Oranye tegas untuk garis pinggir
    private final Color COLOR_TEXT = new Color(255, 230, 200);  // Putih dengan rona oranye untuk teks

    private BufferedImage imgDepan, imgBelakang, imgKanan, imgKiri;
    private BufferedImage imgRumput, imgKelasLantai;

    // Penambahan variabel aset baru
    private BufferedImage imgBossBully, imgBullyKroco, imgGuru, imgSiswi;
    private BufferedImage imgKelasTembok, imgTangga, imgTembokLuar;

    public Renderer() {
        try {
            // Memuat gambar dari folder fisik menggunakan File (Menghindari null pointer pada build)
            imgDepan = ImageIO.read(new File("src/assets/karakter/mc_depan.png"));
            imgBelakang = ImageIO.read(new File("src/assets/karakter/mc_belakang.png"));
            imgKanan = ImageIO.read(new File("src/assets/karakter/mc_samping_kanan.png"));
            imgKiri = ImageIO.read(new File("src/assets/karakter/mc_samping_kiri.png"));
            imgRumput = ImageIO.read(new File("src/assets/lantai/rumput.png"));
            imgKelasLantai = ImageIO.read(new File("src/assets/lantai/kelas_lantai.png"));

            // Memuat aset tambahan baru
            imgBossBully = ImageIO.read(new File("src/assets/karakter/boss_bully.png"));
            imgBullyKroco = ImageIO.read(new File("src/assets/karakter/bully_kroco.png"));
            imgGuru = ImageIO.read(new File("src/assets/karakter/glantai.png"));
            imgSiswi = ImageIO.read(new File("src/assets/karakter/slantai.png"));
            imgKelasTembok = ImageIO.read(new File("src/assets/tembok/kelas_tembok.png"));
            imgTangga = ImageIO.read(new File("src/assets/tembok/tangga.png"));
            imgTembokLuar = ImageIO.read(new File("src/assets/tembok/tembok_luar.png"));

        } catch (IOException | NullPointerException e) {
            System.out.println("Gagal memuat gambar aset: " + e.getMessage());
        }
    }

    public void drawWorld(Graphics g, Map map, Player player, ArrayList<Enemy> enemies, int uiWidth) {
        int ox = uiWidth + 10, oy = 20, ts = map.TILE_SIZE;
        int[][] grid = map.getCurrentMap();

        // Melakukan iterasi array 2D untuk menggambar lingkungan
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                int px = ox + (c * ts), py = oy + (r * ts);

                if (grid[r][c] == 0) {
                    // === LOGIKA GAMBAR LANTAI ===
                    if (map.currentLevel == 0) {
                        if (imgRumput != null) { g.drawImage(imgRumput, px, py, ts, ts, null); }
                        else { g.setColor(new Color(20, 20, 30)); g.fillRect(px, py, ts, ts); }
                    } else {
                        if (imgKelasLantai != null) { g.drawImage(imgKelasLantai, px, py, ts, ts, null); }
                        else { g.setColor(new Color(20, 20, 30)); g.fillRect(px, py, ts, ts); }
                    }
                }
                else if (grid[r][c] == 1) {
                    if (map.currentLevel == 0 && imgTembokLuar != null) { g.drawImage(imgTembokLuar, px, py, ts, ts, null); }
                    else if (map.currentLevel > 0 && imgKelasTembok != null) { g.drawImage(imgKelasTembok, px, py, ts, ts, null); }
                    else { g.setColor(new Color(75, 0, 130)); g.fillRect(px, py, ts, ts); g.setColor(Color.BLACK); g.drawRect(px, py, ts, ts); }
                }
                else if (grid[r][c] == 2 || grid[r][c] == 3) {
                    if (imgTangga != null) { g.drawImage(imgTangga, px, py, ts, ts, null); }
                    else { g.setColor(new Color(150, 0, 0)); g.fillRect(px, py, ts, ts); g.setColor(Color.WHITE); g.drawString("OUT", px+15, py+30); }
                }
                else if (grid[r][c] == 4) {
                    if (imgSiswi != null) { g.drawImage(imgSiswi, px, py, ts, ts, null); }
                }
                else if (grid[r][c] == 5) {
                    if (imgGuru != null) { g.drawImage(imgGuru, px, py, ts, ts, null); }
                }
            }
        }


        // Menggambar entitas musuh
        for (Enemy e : enemies) {
            if (!e.isDefeated) {
                if (e instanceof actors.roles.GangLeader && imgBossBully != null) {
                    g.drawImage(imgBossBully, ox + e.x * ts, oy + e.y * ts, ts, ts, null);
                } else if (imgBullyKroco != null) {
                    g.drawImage(imgBullyKroco, ox + e.x * ts, oy + e.y * ts, ts, ts, null);
                } else {
                    g.setColor(new Color(255, 105, 180));
                    g.fillRect(ox + e.x * ts + 8, oy + e.y * ts + 8, 32, 32);
                }
            }
        }

        // Menggambar posisi pemain
        int px = ox + player.x * ts, py = oy + player.y * ts;

        // --- LOGIKA BARU UNTUK GAMBAR KARAKTER ---
        BufferedImage currentSprite = imgDepan; // Set default menghadap depan

        // Cek arah menggunakan variabel facingX dan facingY dari class Player
        if (player.facingY == -1) { currentSprite = imgBelakang; }
        else if (player.facingY == 1) { currentSprite = imgDepan; }
        else if (player.facingX == -1) { currentSprite = imgKiri; }
        else if (player.facingX == 1) { currentSprite = imgKanan; }

        // Gambar sprite jika berhasil dimuat, jika gagal kembalikan ke bentuk kotak
        if (currentSprite != null) {
            g.drawImage(currentSprite, px, py, ts, ts, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(px + 8, py + 8, 32, 32);
        }
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

        // Logika untuk word wrapping
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

    // PENTING: Signature method telah diubah, menerima "Player player"
    public void drawCombat(Graphics g, Player player, Enemy e, double timer, int clash, int selection, String msg, int uiWidth) {
        int ox = uiWidth + 30;

        // --- RENDERING AVATAR KARAKTER (MENGGELAP SESUAI HP) ---
        int pxPlayer = uiWidth + 320;
        int pxEnemy = uiWidth + 480;
        int pyAvatars = 200;
        int avatarSize = 120;

        // 1. Gambar Player (Menghadap Kanan)
        if (imgKanan != null) {
            g.drawImage(imgKanan, pxPlayer, pyAvatars, avatarSize, avatarSize, null);
            // Kalkulasi warna gelap overlay
            float pRatio = (float) player.getHp() / player.MAX_HP;
            float pDarkness = Math.max(0f, Math.min(1f, 1f - pRatio)); // Rentang 0.0 - 1.0
            g.setColor(new Color(10, 10, 20, (int)(pDarkness * 255)));
            g.fillRect(pxPlayer, pyAvatars, avatarSize, avatarSize);
        }

        // 2. Gambar Enemy
        BufferedImage enemySprite = (e instanceof actors.roles.GangLeader) ? imgBossBully : imgBullyKroco;
        if (enemySprite != null) {
            g.drawImage(enemySprite, pxEnemy, pyAvatars, avatarSize, avatarSize, null);
            // Asumsi Max HP Musuh Standar adalah 100, ubah jika struktur Anda berbeda
            float eRatio = (float) e.getHp() / 100f;
            float eDarkness = Math.max(0f, Math.min(1f, 1f - eRatio));
            g.setColor(new Color(10, 10, 20, (int)(eDarkness * 255)));
            g.fillRect(pxEnemy, pyAvatars, avatarSize, avatarSize);
        }

        // 3. Teks "VS" di tengah Avatar
        g.setColor(Color.RED);
        g.setFont(new Font("Monospaced", Font.BOLD, 28));
        g.drawString("VS", pxPlayer + avatarSize + 10, pyAvatars + 65);
        // --------------------------------------------------------

        g.setColor(new Color(255, 105, 180));
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString(e.getName() + " berkata:", ox, 60);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.ITALIC, 14));
        g.drawString(e.currentDialogue, ox, 90);

// --- KODE TELEGRAPHING (INDIKATOR GERAKAN) ---
        // Menggunakan seed statis agar hasil random konsisten per ronde dan tidak berkedip (flicker)
        java.util.Random rand = new java.util.Random(e.getHp() * 31L + e.currentIntent);

        boolean isBoss = e instanceof actors.roles.GangLeader;

        // Komputasi Probabilitas (Chance)
        // Boss: 2/3 (Jika hasil random 0 atau 1 dari rentang 0-2)
        // Kroco: 1/3 (Jika hasil random 0 dari rentang 0-2)
        boolean isUnknown = isBoss ? (rand.nextInt(3) < 2) : (rand.nextInt(3) < 1);

        String indikator = "";

        if (isUnknown) {
            // Probabilitas 1/2 (50%) untuk status parsial (hanya dialog yang memandu)
            boolean isPartial = rand.nextBoolean();

            if (isPartial) {
                g.setColor(new Color(255, 215, 0)); // Warna emas muda
                indikator = "[ INDIKATOR: (???) Perhatikan intonasi dialog musuh! ]";
            } else {
                g.setColor(new Color(255, 50, 50)); // Warna merah peringatan
                indikator = "[ INDIKATOR: (UNKNOWN) Pergerakan sepenuhnya tidak terbaca! ]";
            }
        } else {
            // Status Normal (Indikator terlihat jelas)
            g.setColor(new Color(255, 165, 0)); // Warna oranye
            if (e.currentIntent == 0) indikator = "[ INDIKATOR: Musuh mengepalkan tangan (Pukulan) ]";
            else if (e.currentIntent == 1) indikator = "[ INDIKATOR: Musuh memundurkan kaki (Tendangan) ]";
            else if (e.currentIntent == 2) indikator = "[ INDIKATOR: Musuh menyilangkan lengan (Tangkisan) ]";
        }

        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString(indikator, ox, 120);

        if (timer > 0) {
            g.setColor(timer < 1.5 ? Color.RED : Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
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

        // --- CHEAT SHEET / PETUNJUK COMBAT ---
        g.setColor(new Color(173, 216, 230)); // Biru muda agar mencolok dari warna oranye
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString("PETUNJUK: Pukulan > Tendangan > Tangkisan > Pukulan", ox, 420);
    }
}