package actors.roles;

public class Enemy extends Bully {
    public int x, y;
    public boolean isDefeated = false;
    public int currentIntent;
    public String currentDialogue;

    // 1. Constructor asli (hanya meminta x dan y, nama di-set default)
    public Enemy(int startX, int startY) {
        super("Perundung Kelas", 50, 10, 25, "Geng Lorong");
        this.x = startX;
        this.y = startY;
    }

    // 2. IMPLEMENTASI CONSTRUCTOR OVERLOADING
    // Namanya sama-sama "Enemy", tetapi meminta tambahan parameter "customName"
    public Enemy(String customName, int startX, int startY) {
        super(customName, 50, 10, 25, "Geng Lorong");
        this.x = startX;
        this.y = startY;
    }

    // Method untuk mengacak tipe serangan musuh dan kalimat petunjuknya
    public void generateIntent() {
        this.currentIntent = new java.util.Random().nextInt(3);

        // currentIntent 0 = Musuh menggunakan Pukulan (Pemain harus memilih [2] Tangkisan)
        if (this.currentIntent == 0) {
            this.currentDialogue = "\"Maju sini lo, rasain tinju gue!\"";
        }
        // currentIntent 1 = Musuh menggunakan Tendangan (Pemain harus memilih [0] Pukulan)
        else if (this.currentIntent == 1) {
            this.currentDialogue = "\"Gue tendang muka lo sekarang!\"";
        }
        // currentIntent 2 = Musuh menggunakan Tangkisan (Pemain harus memilih [1] Tendangan)
        else {
            this.currentDialogue = "\"Coba aja serang gue kalau bisa tembus!\"";
        }
    }
}