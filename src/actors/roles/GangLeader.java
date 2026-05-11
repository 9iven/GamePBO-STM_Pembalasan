package actors.roles;

// Konsep Multilevel Inheritance: GangLeader -> Enemy -> Bully -> Siswa
public class GangLeader extends Enemy {
    private int armorPoint;
    private boolean isEnraged = false;

    public GangLeader(int startX, int startY) {
        super(startX, startY);
        setName("Ketua Faksi (BOSS)");
        setHp(150);
        setBaseDamage(20);
        this.armorPoint = 50;
    }

    @Override
    public void generateIntent() {
        super.generateIntent();
        // Menimpa dialog spesifik untuk Boss
        if (currentIntent == 0) this.currentDialogue = "\"KEKUATAN FISIK ADALAH HUKUM MUTLAK DI SINI!\"";
        else if (currentIntent == 1) this.currentDialogue = "\"Kau pikir ceramahmu bisa mengubah aturan sekolah ini?\"";
        else this.currentDialogue = "\"Jangan bertingkah seolah kau peduli padaku!\"";
    }

    public void specialSkill() {
        this.isEnraged = true;
    }
}