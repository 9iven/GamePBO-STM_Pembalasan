package graphicengine;

import mapping.Map;
import actors.mainactors.Player;
import actors.roles.Enemy;
import java.awt.*;
import java.util.ArrayList;

// Class untuk mengurus semua proses rendering (penggambaran pixel) ke layar
public class Renderer {
    private final Color COLOR_UI_LINE = new Color(100, 100, 255);
    private final Color COLOR_TEXT = new Color(200, 200, 255);

    public void drawWorld(Graphics g, Map map, Player player, ArrayList<Enemy> enemies, int uiWidth) {
        int ox = uiWidth + 10, oy = 20, ts = map.TILE_SIZE;
        int[][] grid = map.getCurrentMap();

        // Melakukan iterasi array 2D untuk menggambar lingkungan
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                int px = ox + (c * ts), py = oy + (r * ts);
                if (grid[r][c] == 0) { g.setColor(new Color(20, 20, 30)); g.fillRect(px, py, ts, ts); }
                else if (grid[r][c] == 1) { g.setColor(new Color(75, 0, 130)); g.fillRect(px, py, ts, ts); g.setColor(Color.BLACK); g.drawRect(px, py, ts, ts); }
                else if (grid[r][c] == 2) { g.setColor(new Color(150, 0, 0)); g.fillRect(px, py, ts, ts); g.setColor(Color.WHITE); g.drawString("OUT", px+15, py+30); }
                else if (grid[r][c] == 3) { g.setColor(new Color(0, 255, 255)); g.fillRect(px, py, ts, ts); }
                else if (grid[r][c] == 4) {
                    g.setColor(new Color(50, 205, 50));
                    g.fillRect(px, py, ts, ts);
                    g.setColor(Color.WHITE);
                    // Mengubah "K" jadi "Heal" dan menggeser px sedikit ke kiri (px+8) agar teksnya muat di dalam kotak
                    g.drawString("Heal", px+8, py+30);
                }
                else if (grid[r][c] == 5) {
                    g.setColor(new Color(255, 165, 0));
                    g.fillRect(px, py, ts, ts);
                    g.setColor(Color.WHITE);
                    // Mengubah "R" jadi "UP" dan menggeser px sedikit agar letaknya pas di tengah
                    g.drawString("UP", px+15, py+30);
                }
            }
        }

        // Menggambar entitas musuh
        g.setColor(new Color(255, 105, 180));
        for (Enemy e : enemies) {
            if (!e.isDefeated) { g.fillRect(ox + e.x * ts + 8, oy + e.y * ts + 8, 32, 32); }
        }

        // Menggambar posisi pemain
        g.setColor(Color.WHITE);
        int px = ox + player.x * ts, py = oy + player.y * ts;
        g.fillRect(px + 8, py + 8, 32, 32);

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

        g.drawString("[ LOG [Controls: WASD, Space - Interact]]", 15, 220);
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

        if (timer > 0) {
            g.setColor(timer < 1.5 ? Color.RED : Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g.drawString(String.format("WAKTU: %.1f DETIK", timer), ox, 140);
            g.setColor(COLOR_TEXT);
            g.setFont(new Font("Monospaced", Font.PLAIN, 16));
            g.drawString("Pilih respons Anda (A/D & SPASI):", ox, 220);

            String[] opts = {"[0] Tindakan Fisik", "[1] Memberi Nasihat", "[2] Pendekatan Empati"};
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