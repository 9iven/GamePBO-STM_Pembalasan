public class Enemy {
    public int x;
    public int y;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveTowards(Player player, Map map) {
        int dx = 0;
        int dy = 0;

        if (player.x > x) dx = 1;
        else if (player.x < x) dx = -1;

        if (player.y > y) dy = 1;
        else if (player.y < y) dy = -1;

        if (!map.isSolid(x + dx, y)) {
            x += dx;
        } else if (!map.isSolid(x, y + dy)) {
            y += dy;
        }
    }
}