package actors.roles;

public class GangLeader extends Enemy {
    private int armorPoint;
    private boolean isEnraged = false;

    public GangLeader(int startX, int startY) {
        super(startX, startY);
        setName("Ketua Faksi (BOSS)");
        setHp(150); // HP tinggi untuk simulasi pertarungan panjang
        setBaseDamage(20);
        this.armorPoint = 50;
    }

    @Override
    public void generateIntent() {
        super.generateIntent();

        if (currentIntent == 0) {
            this.currentDialogue = "\"Nyali lo gede juga berani sampai ke sini.\"";
        } else if (currentIntent == 1) {
            this.currentDialogue = "\"Kakak lo aja habis di tangan gue, apalagi lo!\"";
        } else {
            this.currentDialogue = "\"Lo pikir lo bisa keluar dari sini hidup-hidup?\"";
        }
    }

    public void specialSkill() {
        this.isEnraged = true;
        System.out.println(getName() + " MENGAKTIFKAN MODE ENRAGED!");
    }


    // Getter untuk diakses oleh Game.java
    public boolean isEnraged() { return isEnraged; }
    public int getArmorPoint() { return armorPoint; }
}