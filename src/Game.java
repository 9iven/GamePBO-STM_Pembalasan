import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener {
    private Map gameMap;
    private Player player;
    private boolean isWon = false;

    public Game() {
        this.setPreferredSize(new Dimension(480, 240));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        gameMap = new Map();
        player = new Player();

        gameMap.loadLevel(player);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isWon) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("SansSerif", Font.BOLD, 24));
            g.drawString("EKSPLORASI SELESAI", 100, 120);
            return;
        }

        gameMap.draw(g);

        g.setColor(Color.BLUE);
        g.fillOval(player.x * gameMap.TILE_SIZE + 8, player.y * gameMap.TILE_SIZE + 8, 32, 32);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isWon) return;

        int key = e.getKeyCode();
        int dx = 0, dy = 0;

        if (key == KeyEvent.VK_W) dy = -1;
        if (key == KeyEvent.VK_S) dy = 1;
        if (key == KeyEvent.VK_A) dx = -1;
        if (key == KeyEvent.VK_D) dx = 1;

        if (dx != 0 || dy != 0) {
            int nextX = player.x + dx;
            int nextY = player.y + dy;

            if (!gameMap.isSolid(nextX, nextY)) {
                player.move(dx, dy);

                if (gameMap.isDoor(player.x, player.y)) {
                    gameMap.currentLevel++;
                    if (gameMap.isGameWon()) {
                        isWon = true;
                    } else {
                        gameMap.loadLevel(player);
                    }
                }
            }
        }
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}