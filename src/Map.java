import java.awt.Color;
import java.awt.Graphics;

public class Map {
    private final int[][][] levels = {
            {
                    {1, 1, 1, 1, 1, 1, 3, 3, 1, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 1, 1, 0, 0, 1, 1, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
            },
            {
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 0, 0, 1, 0, 0, 0, 0, 3, 1},
                    {1, 0, 1, 0, 0, 1, 1, 0, 0, 1},
                    {1, 0, 0, 0, 1, 1, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
            }
    };

    public int currentLevel = 0;
    public final int TILE_SIZE = 48;

    public boolean isSolid(int x, int y) {
        int[][] map = levels[currentLevel];
        if (x < 0 || x >= map[0].length || y < 0 || y >= map.length) return true;
        return map[y][x] == 1;
    }

    public boolean isDoor(int x, int y) {
        int[][] map = levels[currentLevel];
        if (x < 0 || x >= map[0].length || y < 0 || y >= map.length) return false;
        return map[y][x] == 3;
    }

    public void loadLevel(Player p) {
        if (currentLevel == 0) {
            p.setPosition(1, 3);
        } else if (currentLevel == 1) {
            p.setPosition(1, 1);
        }
    }

    public void draw(Graphics g) {
        int[][] map = levels[currentLevel];

        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {

                int px = c * TILE_SIZE;
                int py = r * TILE_SIZE;

                if (map[r][c] == 1) {
                    g.setColor(new Color(50, 50, 50));
                } else if (map[r][c] == 3) {
                    g.setColor(Color.ORANGE);
                } else {
                    g.setColor(new Color(180, 180, 180));
                }

                g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(px, py, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public int getTotalLevels() {
        return levels.length;
    }
}