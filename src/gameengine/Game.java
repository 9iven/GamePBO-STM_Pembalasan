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
    private final int UI_W = 280, GAME_W = 720, H = 450;
    private Map gameMap = new Map();
    private Player player = new Player();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Renderer renderer = new Renderer();
    private Enemy activeEnemy;

    // 1. Tambahkan MENU pada enum
    private enum State { MENU, EXPLORE, COMBAT_INPUT, COMBAT_RESULT, GAME_OVER, WIN }
    // 2. Set state awal menjadi MENU
    private State currentState = State.MENU;

    private Timer gameLoop;
    private double combatTimer = 0.0;
    private int clashMultiplier = 1, flashAlpha = 0, playerSelection = 0;
    private ArrayList<String> logMessages = new ArrayList<>();
    private String combatMessage = "";

    public Game() {
        setPreferredSize(new Dimension(UI_W + GAME_W, H));
        setBackground(new Color(10, 10, 20));
        setFocusable(true);
        addKeyListener(this);
        gameMap.loadLevelPosition(player);
        this.enemies = gameMap.getEnemiesForCurrentLevel();
        logMessage("Sistem Aktif. WASD: Bergerak, SPASI: Interaksi");
        gameLoop = new Timer(16, this);
    }

    public void startGameLoop() { gameLoop.start(); }

    // 3. Tambahkan method resetGame untuk mengulang semuanya dari awal
    private void resetGame() {
        gameMap = new Map();
        player = new Player();
        gameMap.loadLevelPosition(player);
        this.enemies = gameMap.getEnemiesForCurrentLevel();

        logMessages.clear();
        logMessage("Sistem Aktif. WASD: Bergerak, SPASI: Interaksi");

        combatTimer = 0.0;
        clashMultiplier = 1;
        flashAlpha = 0;
        playerSelection = 0;
        currentState = State.EXPLORE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (flashAlpha > 0) flashAlpha = Math.max(0, flashAlpha - 10);
        if (currentState == State.COMBAT_INPUT) {
            combatTimer -= 0.016;
            if (combatTimer <= 0) {
                combatMessage = "WAKTU HABIS! (-30 HP)";
                SoundManager.play("hit");
                player.takeDamage(30);
                flashAlpha = 200;
                player.combo = 0;
                clashMultiplier = 1;
                currentState = player.getHp() <= 0 ? State.GAME_OVER : State.COMBAT_RESULT;
            }
        }
        repaint();
    }

    private void logMessage(String msg) {
        logMessages.add(msg);
        if (logMessages.size() > 5) logMessages.remove(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 4. Tampilan saat di Menu Utama
        if (currentState == State.MENU) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 40));
            g.drawString("DISCIPLINARY", UI_W + 100, H / 2 - 20);

            g.setFont(new Font("Monospaced", Font.PLAIN, 20));
            g.setColor(Color.YELLOW);
            g.drawString("Tekan [SPASI] untuk Mulai", UI_W + 70, H / 2 + 30);
            return; // Hentikan render komponen game lain
        }

        renderer.drawUI(g, player, gameMap.currentLevel + 1, logMessages, UI_W, H);
        if (currentState == State.EXPLORE) renderer.drawWorld(g, gameMap, player, enemies, UI_W);
        else if (currentState == State.COMBAT_INPUT) renderer.drawCombat(g, activeEnemy, combatTimer, clashMultiplier, playerSelection, "", UI_W);
        else if (currentState == State.COMBAT_RESULT) renderer.drawCombat(g, activeEnemy, 0, clashMultiplier, playerSelection, combatMessage, UI_W);

            // 5. Tampilan dan Teks Keterangan Tombol saat Kalah
        else if (currentState == State.GAME_OVER) {
            g.setColor(Color.RED); g.setFont(new Font("Monospaced", Font.BOLD, 40));
            g.drawString("SYSTEM FAILURE", UI_W + 150, 200);
            tampilkanOpsiMenu(g);
        }
        else if (currentState == State.WIN) {
            g.setColor(Color.GREEN); g.setFont(new Font("Monospaced", Font.BOLD, 40));
            g.drawString("MISSION ACCOMPLISHED", UI_W + 120, 200);
            tampilkanOpsiMenu(g); // Panggil fungsi pembantu untuk menampilkan tombol
        }

        if (flashAlpha > 0) { g.setColor(new Color(255, 0, 0, flashAlpha)); g.fillRect(0, 0, UI_W + GAME_W, H); }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // 7. Logika Tombol di Menu Utama
        if (currentState == State.MENU) {
            if (key == KeyEvent.VK_SPACE) {
                resetGame(); // Mulai game dari awal
            }
        }
        // 8. Logika Tombol saat Game Over ATAU Menang (Restart dan Back to Menu)
        else if (currentState == State.GAME_OVER || currentState == State.WIN) {
            if (key == KeyEvent.VK_R) {
                resetGame();
            } else if (key == KeyEvent.VK_M) {
                currentState = State.MENU;
            }
        }
        else if (currentState == State.EXPLORE) handleExplore(key);
        else if (currentState == State.COMBAT_INPUT) handleCombatInput(key);
        else if (currentState == State.COMBAT_RESULT && key == KeyEvent.VK_SPACE) {
            if (activeEnemy.isDefeated) {
                // Cek apakah musuh yang kalah adalah Boss
                if (activeEnemy instanceof actors.roles.GangLeader) {
                    currentState = State.WIN; // Jika boss kalah, langsung ke layar Menang
                } else {
                    currentState = State.EXPLORE; // Jika musuh biasa, kembali jalan-jalan
                }
            } else if (player.getHp() <= 0) {
                currentState = State.GAME_OVER;
            } else {
                combatTimer = player.maxCombatTime;
                activeEnemy.generateIntent();
                currentState = State.COMBAT_INPUT;
            }
        }
    }

    private void handleExplore(int key) {
        int dx = 0, dy = 0;
        if (key == KeyEvent.VK_W) dy = -1;
        if (key == KeyEvent.VK_S) dy = 1;
        if (key == KeyEvent.VK_A) dx = -1;
        if (key == KeyEvent.VK_D) dx = 1;

        if (dx != 0 || dy != 0) {
            player.setFacing(dx, dy);
            int nx = player.x + dx, ny = player.y + dy;
            boolean enemyBlock = false;
            for (Enemy enemy : enemies) {
                if (!enemy.isDefeated && enemy.x == nx && enemy.y == ny) { enemyBlock = true; break; }
            }
            if (!gameMap.isSolid(nx, ny) && !enemyBlock) { player.move(dx, dy); SoundManager.play("step"); }
            else SoundManager.play("bump");
        }

        if (key == KeyEvent.VK_SPACE) {
            int tx = player.x + player.facingX, ty = player.y + player.facingY;

            if (gameMap.isInteractable(tx, ty, 3)) {
                SoundManager.play("door"); gameMap.currentLevel++;
                if (gameMap.isGameWon()) currentState = State.WIN;
                else { gameMap.loadLevelPosition(player); this.enemies = gameMap.getEnemiesForCurrentLevel(); logMessage("Naik lantai..."); }
            }
            else if (gameMap.isInteractable(tx, ty, 2)) {
                if (gameMap.currentLevel > 0) {
                    SoundManager.play("door"); gameMap.currentLevel--;
                    gameMap.loadLevelPosition(player); this.enemies = gameMap.getEnemiesForCurrentLevel(); logMessage("Turun lantai.");
                }
            }
            else if (gameMap.isInteractable(tx, ty, 4)) {
                if (player.currency >= 10 && player.getHp() < player.MAX_HP) {
                    player.currency -= 10; player.setHp(player.MAX_HP); SoundManager.play("select"); logMessage("Heal penuh (-10 Koin)");
                } else if (player.getHp() >= player.MAX_HP) logMessage("Darah sudah penuh.");
                else { SoundManager.play("error"); logMessage("Butuh 10 Koin."); }
            }
            else if (gameMap.isInteractable(tx, ty, 5)) {
                if (player.getExperience() >= 50) {
                    player.setExperience(player.getExperience() - 50);
                    player.levelUp();
                    player.maxCombatTime += 1.0;
                    SoundManager.play("win");
                    logMessage("UPGRADE! Anda naik ke Level " + player.getLevel());
                } else {
                    SoundManager.play("error"); logMessage("Butuh 50 EXP.");
                }
            }

            for (Enemy enemy : enemies) {
                if (enemy.x == tx && enemy.y == ty && !enemy.isDefeated) {
                    activeEnemy = enemy; activeEnemy.generateIntent();
                    playerSelection = 0; combatTimer = player.maxCombatTime;
                    currentState = State.COMBAT_INPUT;
                    SoundManager.play("combat_start");
                    logMessage("Dihadang oleh faksi: " + activeEnemy.getGangFaction());
                }
            }
        }
    }

    private void handleCombatInput(int key) {
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_W) { playerSelection = (playerSelection + 2) % 3; SoundManager.play("select"); }
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_S) { playerSelection = (playerSelection + 1) % 3; SoundManager.play("select"); }
        if (key == KeyEvent.VK_SPACE) resolveCombat();
    }

    private void resolveCombat() {
        String[] actions = {"Fisik", "Nasihat", "Empati"};
        int enemyAttack = activeEnemy.currentIntent;

        if (playerSelection == enemyAttack) {
            clashMultiplier++; combatTimer = player.maxCombatTime; activeEnemy.generateIntent();
            combatMessage = "Seri! Keduanya memilih " + actions[enemyAttack] + ".";
            SoundManager.play("clash"); flashAlpha = 50; player.combo = 0; return;

        } else if ((playerSelection == 0 && enemyAttack == 1) || (playerSelection == 1 && enemyAttack == 2) || (playerSelection == 2 && enemyAttack == 0)) {
            player.combo++;
            player.attack();
            int damageDealt = player.getBaseDamage() * clashMultiplier;

            if (activeEnemy instanceof actors.roles.GangLeader) {
                actors.roles.GangLeader boss = (actors.roles.GangLeader) activeEnemy;
                if (boss.isEnraged()) {
                    damageDealt -= (boss.getArmorPoint() / 10);
                }
            }

            activeEnemy.takeDamage(damageDealt);

            if (activeEnemy.getHp() <= 0) {
                activeEnemy.isDefeated = true;
                player.currency += 5;
                player.setExperience(player.getExperience() + activeEnemy.getExpReward());
                combatMessage = "Musuh kalah! (+" + activeEnemy.getExpReward() + " EXP)";
                logMessage("Menang melawan perundung.");
            } else {
                combatMessage = "Berhasil! Musuh tersisa " + activeEnemy.getHp() + " HP";

                if (activeEnemy instanceof actors.roles.GangLeader) {
                    actors.roles.GangLeader boss = (actors.roles.GangLeader) activeEnemy;
                    if (boss.getHp() <= 75 && !boss.isEnraged()) {
                        boss.specialSkill();
                        logMessage("BOSS MARAH! DMG x2 & Armor Aktif!");
                    }
                }
            }

            if (player.combo >= 3) { player.setHp(Math.min(player.MAX_HP, player.getHp() + 20)); logMessage("COMBO! Memulihkan 20 HP."); }
            clashMultiplier = 1; SoundManager.play("win");

        } else {
            activeEnemy.attack();
            int damage = activeEnemy.getBaseDamage() * clashMultiplier;

            if (activeEnemy instanceof actors.roles.GangLeader && ((actors.roles.GangLeader)activeEnemy).isEnraged()) {
                damage *= 2;
            }

            combatMessage = "Gagal! Terkena serangan (-" + damage + " HP)";
            player.takeDamage(damage); player.combo = 0; clashMultiplier = 1; flashAlpha = 200;
            SoundManager.play("hit"); logMessage("HP Berkurang.");
            if (player.getHp() <= 0) currentState = State.GAME_OVER;
        }
        if (currentState != State.GAME_OVER) currentState = State.COMBAT_RESULT;
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    private void tampilkanOpsiMenu(Graphics g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString("[R] RESTART GAME", UI_W + 220, 250);
        g.drawString("[M] MAIN MENU", UI_W + 235, 280);
    }
}