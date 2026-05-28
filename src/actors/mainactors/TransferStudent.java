package actors.mainactors;
import actors.core.Siswa;

// Konsep Inheritance: TransferStudent mengambil semua sifat dari class Siswa
public class TransferStudent extends Siswa {
    private int level = 1;
    private int experience = 0;

    public TransferStudent(String name, int hp, int baseDamage) {
        super(name, hp, baseDamage); // Memanggil constructor dari parent class
    }

    // Konsep Overriding: Menimpa abstract method dari parent class
    @Override
    public void attack() {
        System.out.println(getName() + " melakukan serangan standar.");
    }

    public void levelUp() {
        this.level++;
        this.experience = 0;
        setHp(getHp() + 25);
        setBaseDamage(getBaseDamage() + 5);
    }

    // Getter dan Setter
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
}