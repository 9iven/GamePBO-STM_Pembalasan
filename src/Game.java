import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, Runnable {

    private Map gameMap;
    private Player player;
    private Enemy enemy;

    private boolean isWon = false;
    private boolean isGameOver = false;

    private Thread gameThread;

    private boolean up, down, left, right;

    private int moveDelay = 0;
    private final int MOVE_COOLDOWN = 10;

    private int enemyDelay = 0;
    private final int ENEMY_COOLDOWN = 20;

    private int score = 0;

    public Game() {
        this.setPreferredSize(new Dimension(480, 240));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        gameMap = new Map();
        player = new Player();
        enemy = new Enemy(5, 2);

        gameMap.loadLevel(player);

        startGameThread();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (isWon || isGameOver) return;

        // PLAYER
        if (moveDelay > 0) {
            moveDelay--;
        } else {
            int dx = 0, dy = 0;

            if (up) dy = -1;
            else if (down) dy = 1;
            else if (left) dx = -1;
            else if (right) dx = 1;

            if (dx != 0 || dy != 0) {
                int nextX = player.x + dx;
                int nextY = player.y + dy;

                if (!gameMap.isSolid(nextX, nextY)) {
                    player.move(dx, dy);
                    moveDelay = MOVE_COOLDOWN;

                    if (gameMap.isDoor(player.x, player.y)) {
                        score += 100;

                        if (gameMap.currentLevel + 1 >= gameMap.getTotalLevels()) {
                            isWon = true;
                        } else {
                            gameMap.currentLevel++;
                            gameMap.loadLevel(player);

                            // reset enemy tiap level
                            enemy.x = 5;
                            enemy.y = 2;
                        }
                    }
                }
            }
        }

        // ENEMY
        if (enemyDelay > 0) {
            enemyDelay--;
        } else {
            enemy.moveTowards(player, gameMap);
            enemyDelay = ENEMY_COOLDOWN;
        }

        // COLLISION
        if (player.x == enemy.x && player.y == enemy.y) {
            isGameOver = true;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        gameMap.draw(g);

        // PLAYER
        g.setColor(Color.GREEN);
        g.fillOval(player.x * gameMap.TILE_SIZE + 8,
                player.y * gameMap.TILE_SIZE + 8,
                32, 32);

        // ENEMY
        g.setColor(Color.RED);
        g.fillRect(enemy.x * gameMap.TILE_SIZE + 10,
                enemy.y * gameMap.TILE_SIZE + 10,
                28, 28);

        // UI
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 10, 20);

        if (isWon) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("KAMU MENANG!", 140, 120);
        }

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("GAME OVER!", 150, 120);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) up = true;
        if (key == KeyEvent.VK_S) down = true;
        if (key == KeyEvent.VK_A) left = true;
        if (key == KeyEvent.VK_D) right = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) up = false;
        if (key == KeyEvent.VK_S) down = false;
        if (key == KeyEvent.VK_A) left = false;
        if (key == KeyEvent.VK_D) right = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}