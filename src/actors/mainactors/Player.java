package actors.mainactors;

public class Player extends TransferStudent {
    public int x, y;
    // Hapus kata 'final' agar batas maksimal HP bisa bertambah saat Level Up
    public int MAX_HP = 100;
    public int facingX = 0, facingY = 1;

    public int currency = 0;
    public double maxCombatTime = 4.0;

    public Player() {
        super("Siswa Pindahan", 100, 15);
    }

    // MENGGUNAKAN METHOD levelUp():
    // Melakukan overriding dari TransferStudent untuk menambah kapasitas MAX_HP
    @Override
    public void levelUp() {
        super.levelUp(); // Memanggil logika kenaikan level dasar
        this.MAX_HP += 25; // Menambah batas darah maksimal
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