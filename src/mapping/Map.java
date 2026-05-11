package mapping;

import actors.mainactors.Player;
import actors.roles.Enemy;
import actors.roles.GangLeader;
import java.util.ArrayList;

public class Map {
    // Array 3D untuk merepresentasikan peta: levels[Lantai][Baris_Y][Kolom_X]
    private final int[][][] levels = {
            { // LEVEL 0: Halaman Depan
                    {1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1},
                    {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
            },
            { // LEVEL 1: Lantai 1 (Hub)
                    {1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1},
                    {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1},
                    {1, 0, 4, 0, 1, 1, 0, 0, 1, 1, 0, 5, 0, 0, 1}, // 4: Kantin, 5: Ruang Guru
                    {1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1}  // 2: Pintu Turun
            },
            { // LEVEL 2: Lantai 2 (Kelas)
                    {1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                    {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1},
                    {1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1}
            },
            { // LEVEL 3: Rooftop (Arena Boss)
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1}
            }
    };

    public int currentLevel = 0;
    public final int TILE_SIZE = 48;

    // Struktur data untuk menyimpan status musuh secara persisten antar lantai
    private ArrayList<ArrayList<Enemy>> allLevelEnemies = new ArrayList<>();

    public Map() {
        for (int i = 0; i < levels.length; i++) {
            allLevelEnemies.add(new ArrayList<>());
        }
        initAllEnemies();
    }

    private void initAllEnemies() {
        allLevelEnemies.get(0).add(new Enemy(6, 2));
        allLevelEnemies.get(0).add(new Enemy(7, 2));

        allLevelEnemies.get(1).add(new Enemy(6, 1));

        allLevelEnemies.get(2).add(new Enemy(2, 2));
        allLevelEnemies.get(2).add(new Enemy(11, 2));

        allLevelEnemies.get(3).add(new GangLeader(7, 1));
    }

    public boolean isSolid(int x, int y) {
        int[][] map = levels[currentLevel];
        if (x < 0 || x >= map[0].length || y < 0 || y >= map.length) return true;
        return map[y][x] == 1 || map[y][x] == 2 || map[y][x] == 3 || map[y][x] == 4 || map[y][x] == 5;
    }

    public boolean isInteractable(int x, int y, int type) {
        int[][] map = levels[currentLevel];
        if (x < 0 || x >= map[0].length || y < 0 || y >= map.length) return false;
        return map[y][x] == type;
    }

    public ArrayList<Enemy> getEnemiesForCurrentLevel() {
        return allLevelEnemies.get(currentLevel);
    }

    // Mengatur titik spawn berdasarkan lantai saat transisi ruangan
    public void loadLevelPosition(Player p) {
        p.setPosition(7, 5);
    }

    public int[][] getCurrentMap() { return levels[currentLevel]; }
    public boolean isGameWon() { return currentLevel >= levels.length; }
}