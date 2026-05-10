public class Player {
    public int x;
    public int y;

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
//ngedit
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }
}