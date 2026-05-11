package actors.roles;
import java.util.Random;

public class Enemy extends Bully {
    public int x, y;
    public boolean isDefeated = false;
    public int currentIntent;
    public String currentDialogue;

    public Enemy(int startX, int startY) {
        super("Perundung Kelas", 50, 10, 25, "Geng Lorong");
        this.x = startX;
        this.y = startY;
    }

    // Method untuk mengacak tipe serangan musuh dan kalimat petunjuknya
    public void generateIntent() {
        this.currentIntent = new Random().nextInt(3);
        if (this.currentIntent == 0) this.currentDialogue = "\"Maju selangkah lagi dan saya akan MENGHAJAR Anda!\"";
        else if (this.currentIntent == 1) this.currentDialogue = "\"DENGARKAN perkataan saya, Anda adalah siswa yang gagal!\"";
        else this.currentDialogue = "\"Semua orang menjauhi saya, saya merasa sangat KESEPIAN...\"";
    }
}