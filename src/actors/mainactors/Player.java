package actors.mainactors;

public class Player extends TransferStudent {
    public int x, y;
    public final int MAX_HP = 100;
    public int facingX = 0, facingY = 1;
    public int combo = 0;

    // Menambahkan kembali atribut progres ekonomi dan waktu interogasi
    public int currency = 0;
    public double maxCombatTime = 4.0;

    public Player() {
        super("Siswa Pindahan", 100, 15, 100);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
        this.facingX = dx;
        this.facingY = dy;
    }

    public void setFacing(int dx, int dy) {
        this.facingX = dx;
        this.facingY = dy;
    }
}