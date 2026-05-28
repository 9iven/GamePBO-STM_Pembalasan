package mapping;

import actors.mainactors.Player;
import actors.roles.Enemy;
import actors.roles.GangLeader;

import java.util.ArrayList;

public class Map {

    // ================= MAP =================
    private final int[][][] levels = {

            // ===== LEVEL 0 =====
            {
                    {1,1,1,1,1,1,3,3,1,1,1,1,1,1,1},
                    {1,0,0,0,1,1,0,0,1,1,0,0,0,0,1},
                    {1,0,0,0,1,1,0,0,1,1,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,1,1,1,1,1,0,0,1,1,1,1,1,1,1}
            },

            // ===== LEVEL 1 =====
            {
                    {1,1,1,1,1,1,3,3,1,1,1,1,1,1,1},
                    {1,0,0,0,1,1,0,0,1,1,0,0,0,0,1},

                    // 4 = Heal
                    // 5 = Upgrade
                    {1,0,4,0,1,1,0,0,1,1,0,5,0,0,1},

                    {1,1,0,1,1,1,0,0,1,1,1,0,1,1,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},

                    // 2 = turun
                    {1,1,1,1,1,1,2,2,1,1,1,1,1,1,1}
            },

            // ===== LEVEL 2 =====
            {
                    {1,1,1,1,1,1,3,3,1,1,1,1,1,1,1},
                    {1,1,1,1,1,1,0,0,1,1,1,1,1,1,1},
                    {1,0,0,0,1,1,0,0,1,1,0,0,0,0,1},
                    {1,1,0,1,1,1,0,0,1,1,1,0,1,1,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,1,1,1,1,1,2,2,1,1,1,1,1,1,1}
            },

            // ===== LEVEL 3 =====
            {
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,1,1,1,1,1,2,2,1,1,1,1,1,1,1}
            }
    };

    public int currentLevel = 0;

    public final int TILE_SIZE = 48;

    // ===== ENEMY STORAGE =====
    private ArrayList<ArrayList<Enemy>> allLevelEnemies =
            new ArrayList<>();

    public Map() {

        for (int i = 0; i < levels.length; i++) {

            allLevelEnemies.add(new ArrayList<>());
        }

        initAllEnemies();
    }

    // ===== INIT ENEMY =====
    private void initAllEnemies() {

        // LEVEL 0
        allLevelEnemies.get(0).add(new Enemy(6, 2));
        // Menggunakan constructor overloading: Enemy(String, int, int) -> Nama kustom
        allLevelEnemies.get(0).add(new Enemy("Penjaga Gerbang", 7, 2));

        // LEVEL 1 (Area Kantin dan Ruang Guru)
        // Menggunakan constructor overloading untuk variasi nama musuh
        allLevelEnemies.get(1).add(new Enemy("Preman Kantin", 6, 1));
        allLevelEnemies.get(1).add(new Enemy("Preman Kantin", 7, 1));
        allLevelEnemies.get(1).add(new Enemy("Penjaga Tangga", 11, 3));

        // Menggunakan constructor asli
        allLevelEnemies.get(1).add(new Enemy(2, 3));

        // LEVEL 2
        allLevelEnemies.get(2).add(new Enemy("Senior Lorong", 2, 2));
        allLevelEnemies.get(2).add(new Enemy("Penjaga Tangga", 11, 2));

        // LEVEL 3
        allLevelEnemies.get(3).add(new GangLeader(7, 1));
    }

    // ===== SOLID TILE =====
    public boolean isSolid(int x, int y) {

        int[][] map = levels[currentLevel];

        if (
                x < 0 ||
                        x >= map[0].length ||
                        y < 0 ||
                        y >= map.length
        ) {
            return true;
        }

        return map[y][x] == 1
                || map[y][x] == 2
                || map[y][x] == 3
                || map[y][x] == 4
                || map[y][x] == 5;
    }

    // ===== INTERACT =====
    public boolean isInteractable(
            int x,
            int y,
            int type
    ) {

        int[][] map = levels[currentLevel];

        if (
                x < 0 ||
                        x >= map[0].length ||
                        y < 0 ||
                        y >= map.length
        ) {
            return false;
        }

        return map[y][x] == type;
    }

    // ===== ENEMY =====
    public ArrayList<Enemy> getEnemiesForCurrentLevel() {

        return allLevelEnemies.get(currentLevel);
    }

    // ===== LOAD POSITION =====
    public void loadLevelPosition(Player p) {

        p.setPosition(7, 5);
    }

    // ===== GET MAP =====
    public int[][] getCurrentMap() {

        return levels[currentLevel];
    }

    // ===== GAME WIN =====
    public boolean isGameWon() {

        return currentLevel >= levels.length;
    }
}