package gameengine;

import actors.mainactors.Player;
import actors.roles.Enemy;
import mapping.Map;
import graphicengine.Renderer;
import sounds.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, ActionListener {

    private final int UI_W = 280, GAME_W = 720, H = 480;

    private Map gameMap = new Map();
    private Player player = new Player();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Renderer renderer = new Renderer();
    private Enemy activeEnemy;

    private enum State {
        MENU,
        INTRO,
        EXPLORE,
        PUZZLE,
        COMBAT_INPUT,
        COMBAT_RESULT,
        GAME_OVER,
        WIN
    }

    private State currentState = State.MENU;

    private Timer gameLoop;

    private double combatTimer = 0.0;
    private int clashMultiplier = 1;
    private int flashAlpha = 0;
    private int playerSelection = 0;

    private ArrayList<String> logMessages = new ArrayList<>();
    private String combatMessage = "";

    // ===== INTRO =====
    private int introStep = 0;

    public Game() {

        setPreferredSize(new Dimension(UI_W + GAME_W, H));
        setBackground(new Color(10, 10, 20));

        setFocusable(true);
        addKeyListener(this);

        gameMap.loadLevelPosition(player);
        enemies = gameMap.getEnemiesForCurrentLevel();

        logMessage("Sistem Aktif. WASD: Bergerak");

        gameLoop = new Timer(16, this);

        SoundManager.playBGM("main_theme"); //Musik Menyala Saat Game Pertama Dibuka
    }

    public void startGameLoop() {
        gameLoop.start();
    }

    private void resetGame() {

        gameMap = new Map();
        player = new Player();

        gameMap.loadLevelPosition(player);

        enemies = gameMap.getEnemiesForCurrentLevel();

        logMessages.clear();

        combatTimer = 0;
        clashMultiplier = 1;
        flashAlpha = 0;
        playerSelection = 0;

        introStep = 0;

        currentState = State.INTRO;

        logMessage("Masuk ke sekolah...");

        SoundManager.playBGM("main_theme"); //Musik Menyala Lagi Saat Restart
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (flashAlpha > 0) {
            flashAlpha = Math.max(0, flashAlpha - 10);
        }

        if (currentState == State.COMBAT_INPUT) {

            combatTimer -= 0.016;

            if (combatTimer <= 0) {

                combatMessage = "WAKTU HABIS!";
                player.takeDamage(30);

                flashAlpha = 200;

                SoundManager.play("hit");

                if (player.getHp() <= 0) {

                    currentState = State.GAME_OVER;
                    SoundManager.stopBGM(); // Musik Mati Saat Kalah (Kehabisan Waktu)

                } else {

                    currentState = State.COMBAT_RESULT;
                }
            }
        }

        repaint();
    }

    private void logMessage(String msg) {

        logMessages.add(msg);

        if (logMessages.size() > 5) {
            logMessages.remove(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // ===== MENU =====
        if (currentState == State.MENU) {
            g.setColor(Color.WHITE);
            drawCenteredString(g, "STM Pembalasan", H / 2 - 40, new Font("Monospaced", Font.BOLD, 40), 0, getWidth());

            g.setColor(Color.YELLOW);
            drawCenteredString(g, "Tekan [SPASI] untuk Mulai", H / 2 + 30, new Font("Monospaced", Font.PLAIN, 20), 0, getWidth());
            return;
        }

        // ===== INTRO =====
        else if (currentState == State.INTRO) {
            g.setColor(Color.WHITE);
            Font introFont = new Font("Monospaced", Font.BOLD, 22);

            String teksIntro = "";
            if (introStep == 0) {
                teksIntro = "Kakakku hanya ingin menjalani hari-harinya dengan damai...";
            } else if (introStep == 1) {
                teksIntro = "Tapi mereka mengambil semuanya darinya.";
            } else if (introStep == 2) {
                teksIntro = "Maka aku sendiri yang akan menjemputnya.";
            }

            drawCenteredString(g, teksIntro, H / 2 - 20, introFont, 0, getWidth());

            g.setColor(Color.YELLOW);
            Font promptFont = new Font("Monospaced", Font.PLAIN, 16);
            drawCenteredString(g, "Tekan SPASI...", H / 2 + 50, promptFont, 0, getWidth());
            return;
        }

        // Komponen UI akan dirender untuk state selain Menu dan Intro
        renderer.drawUI(g, player, gameMap.currentLevel + 1, logMessages, UI_W, H);

        // ===== EXPLORE =====
        if (currentState == State.EXPLORE) {
            renderer.drawWorld(g, gameMap, player, enemies, UI_W);
        }

        // ===== COMBAT =====
        else if (currentState == State.COMBAT_INPUT) {
            // Perhatikan penambahan argumen "player," setelah "g,"
            renderer.drawCombat(g, player, activeEnemy, combatTimer, clashMultiplier, playerSelection, "", UI_W);
        }
        else if (currentState == State.COMBAT_RESULT) {
            // Perhatikan penambahan argumen "player," setelah "g,"
            renderer.drawCombat(g, player, activeEnemy, 0, clashMultiplier, playerSelection, combatMessage, UI_W);
        }

        // ===== GAME OVER =====
        else if (currentState == State.GAME_OVER) {
            g.setColor(Color.RED);
            // Dirender menggunakan getWidth() agar center di layar penuh
            drawCenteredString(g, "Anda Kalah!", H / 2 - 40, new Font("Monospaced", Font.BOLD, 40), 0, getWidth());
            tampilkanOpsiMenu(g);
        }

        // ===== WIN / ENDING =====
        else if (currentState == State.WIN) {
            g.setColor(Color.GREEN);
            drawCenteredString(g, "BALAS DENDAM SELESAI", H / 2 - 90, new Font("Monospaced", Font.BOLD, 36), 0, getWidth() + 30);

            // Teks Cerita Penutup
            g.setColor(Color.WHITE);
            Font endingFont = new Font("Monospaced", Font.PLAIN, 18);
            drawCenteredString(g, "Pimpinan mereka akhirnya tersungkur di hadapanku.", H / 2 - 30, endingFont, 0, getWidth() + 30);
            drawCenteredString(g, "Rasa sakit yang ditanggung kakakku kini telah terbayar lunas.", H / 2, endingFont, 0, getWidth() + 30);
            drawCenteredString(g, "Tidak akan ada lagi yang berani mengusik kami.", H / 2 + 30, endingFont, 0, getWidth() + 30);

            tampilkanOpsiMenu(g);
        }

        if (flashAlpha > 0) {
            g.setColor(new Color(255, 0, 0, flashAlpha));
            g.fillRect(0, 0, UI_W + GAME_W, H);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        // ===== MENU =====
        if (currentState == State.MENU) {

            if (key == KeyEvent.VK_SPACE) {

                resetGame();
            }
        }

        // ===== INTRO =====
        else if (currentState == State.INTRO) {

            if (key == KeyEvent.VK_SPACE) {

                introStep++;

                if (introStep > 2) {

                    currentState = State.EXPLORE;
                }
            }
        }

        // ===== GAME OVER / WIN =====
        else if (
                currentState == State.GAME_OVER ||
                        currentState == State.WIN
        ) {

            if (key == KeyEvent.VK_R) {

                resetGame();
            }

            else if (key == KeyEvent.VK_M) {

                currentState = State.MENU;

                SoundManager.playBGM("main_theme"); //Musik Otomatis Berbunyi Lagi Saat Restart (Tombol R)
            }
        }

        else if (currentState == State.EXPLORE) {

            handleExplore(key);
        }

        else if (currentState == State.COMBAT_INPUT) {

            handleCombatInput(key);
        }

        else if (
                currentState == State.COMBAT_RESULT &&
                        key == KeyEvent.VK_SPACE
        ) {

            if (activeEnemy.isDefeated) {

                if (activeEnemy instanceof actors.roles.GangLeader) {

                    currentState = State.WIN;

                } else {

                    currentState = State.EXPLORE;
                }
            }

            else if (player.getHp() <= 0) {

                currentState = State.GAME_OVER;

            } else {

                combatTimer = player.maxCombatTime;

                activeEnemy.generateIntent();

                currentState = State.COMBAT_INPUT;
            }
        }
    }

    private void handleExplore(int key) {

        int dx = 0;
        int dy = 0;

        if (key == KeyEvent.VK_W) dy = -1;
        if (key == KeyEvent.VK_S) dy = 1;
        if (key == KeyEvent.VK_A) dx = -1;
        if (key == KeyEvent.VK_D) dx = 1;

        if (dx != 0 || dy != 0) {

            player.setFacing(dx, dy);

            int nx = player.x + dx;
            int ny = player.y + dy;

            boolean enemyBlock = false;

            for (Enemy enemy : enemies) {

                if (
                        !enemy.isDefeated &&
                                enemy.x == nx &&
                                enemy.y == ny
                ) {
                    enemyBlock = true;
                    break;
                }
            }

            if (
                    !gameMap.isSolid(nx, ny) &&
                            !enemyBlock
            ) {

                player.move(dx, dy);

                SoundManager.play("step");

            } else {

                SoundManager.play("bump");
            }
        }

        // ===== INTERAKSI =====
        if (key == KeyEvent.VK_SPACE) {

            int tx = player.x + player.facingX;
            int ty = player.y + player.facingY;

            // ===== PINDAH LANTAI =====
            if (gameMap.isInteractable(tx, ty, 3)) {

                SoundManager.play("door");

                gameMap.currentLevel++;

                if (gameMap.isGameWon()) {

                    currentState = State.WIN;

                } else {

                    gameMap.loadLevelPosition(player);

                    enemies = gameMap.getEnemiesForCurrentLevel();

                    logMessage("Naik lantai...");
                }
            }

            else if (gameMap.isInteractable(tx, ty, 2)) {

                if (gameMap.currentLevel > 0) {

                    SoundManager.play("door");

                    gameMap.currentLevel--;

                    gameMap.loadLevelPosition(player);

                    enemies = gameMap.getEnemiesForCurrentLevel();

                    logMessage("Turun lantai.");
                }
            }

            // ===== KANTIN (HEAL) =====
            else if (gameMap.isInteractable(tx, ty, 4)) {
                if (player.currency >= 10 && player.getHp() < player.MAX_HP) {
                    player.currency -= 10;
                    player.setHp(player.MAX_HP);
                    SoundManager.play("win");
                    logMessage("Heal penuh (-10 Koin)");
                } else if (player.getHp() >= player.MAX_HP) {
                    logMessage("Darah sudah penuh.");
                } else {
                    SoundManager.play("error");
                    logMessage("Butuh 10 Koin.");
                }
            }
            // ===== RUANG GURU (LEVEL UP) =====
            else if (gameMap.isInteractable(tx, ty, 5)) {
                if (player.getExperience() >= 50) {
                    player.setExperience(player.getExperience() - 50);
                    player.levelUp(); // Pemanggilan method levelUp()
                    player.maxCombatTime += 1.0;
                    SoundManager.play("win");
                    logMessage("UPGRADE! Level " + player.getLevel());
                } else {
                    SoundManager.play("error");
                    logMessage("Butuh 50 EXP.");
                }
            }
            // ==========================================

            // ===== MUSUH =====
            for (Enemy enemy : enemies) {

                if (
                        enemy.x == tx &&
                                enemy.y == ty &&
                                !enemy.isDefeated
                ) {

                    activeEnemy = enemy;

                    activeEnemy.generateIntent();

                    playerSelection = 0;

                    combatTimer = player.maxCombatTime;

                    currentState = State.COMBAT_INPUT;

                    SoundManager.play("combat_start");

                    logMessage(
                            "Dihadang oleh " +
                                    activeEnemy.getGangFaction()
                    );
                }
            }
        }
    }

    private void handleCombatInput(int key) {

        if (
                key == KeyEvent.VK_A ||
                        key == KeyEvent.VK_W
        ) {

            playerSelection =
                    (playerSelection + 2) % 3;

            SoundManager.play("select");
        }

        if (
                key == KeyEvent.VK_D ||
                        key == KeyEvent.VK_S
        ) {

            playerSelection =
                    (playerSelection + 1) % 3;

            SoundManager.play("select");
        }

        if (key == KeyEvent.VK_SPACE) {

            resolveCombat();
        }
    }

    private void resolveCombat() {

        String[] actions = { "Pukulan", "Tendangan", "Tangkisan" };

        int enemyAttack = activeEnemy.currentIntent;

        // ===== SERI =====
        if (playerSelection == enemyAttack) {

            clashMultiplier++;

            combatTimer = player.maxCombatTime;

            activeEnemy.generateIntent();

            combatMessage =
                    "Seri! " +
                            actions[enemyAttack];

            SoundManager.play("clash");

            flashAlpha = 50;

            return;
        }

        // ===== MENANG =====
        else if (
                (playerSelection == 0 && enemyAttack == 1) ||
                        (playerSelection == 1 && enemyAttack == 2) ||
                        (playerSelection == 2 && enemyAttack == 0)
        ) {

            int damage =
                    player.getBaseDamage() *
                            clashMultiplier;

            activeEnemy.takeDamage(damage);

            if (activeEnemy.getHp() <= 0) {

                activeEnemy.isDefeated = true;

                player.currency += 5;

                player.setExperience(
                        player.getExperience() +
                                activeEnemy.getExpReward()
                );

                combatMessage =
                        "Musuh kalah! +" +
                                activeEnemy.getExpReward() +
                                " EXP";

                logMessage("Musuh dikalahkan.");

            } else {

                combatMessage =
                        "Berhasil menyerang!";
            }

            clashMultiplier = 1;

            SoundManager.play("win");
        }

        // ===== KALAH =====
        else {

            int damage =
                    activeEnemy.getBaseDamage() *
                            clashMultiplier;

            player.takeDamage(damage);

            combatMessage =
                    "Terkena serangan! -" +
                            damage +
                            " HP";

            flashAlpha = 200;

            SoundManager.play("hit");

            if (player.getHp() <= 0) {

                currentState = State.GAME_OVER;

                SoundManager.stopBGM(); // Musik Mati Saat Kalah (Kena Serangan Musuh)
            }
        }

        if (currentState != State.GAME_OVER) {

            currentState = State.COMBAT_RESULT;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void tampilkanOpsiMenu(Graphics g) {
        g.setColor(Color.YELLOW);
        Font fontOpsi = new Font("Monospaced", Font.BOLD, 16);

        // Opsi Restart dan Menu ditarik lebih ke bawah (H / 2 + 100) dan di-center di layar penuh (0, getWidth())
        drawCenteredString(g, "[R] Restart", H / 2 + 100, fontOpsi, 0, getWidth());
        drawCenteredString(g, "[M] Main Menu", H / 2 + 130, fontOpsi, 0, getWidth());
    }

    // Method Helper Baru untuk Komputasi Titik Tengah String
    private void drawCenteredString(Graphics g, String text, int y, Font font, int startX, int areaWidth) {
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int x = startX + (areaWidth - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}