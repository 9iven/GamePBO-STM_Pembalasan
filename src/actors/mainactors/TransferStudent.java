package actors.mainactors;
import actors.core.Siswa;

// Konsep Inheritance: TransferStudent mengambil semua sifat dari class Siswa
public class TransferStudent extends Siswa {
    private int level = 1;
    private int experience = 0;
    private int stamina;

    public TransferStudent(String name, int hp, int baseDamage, int stamina) {
        super(name, hp, baseDamage); // Memanggil constructor dari parent class
        this.stamina = stamina;
    }

    // Konsep Overriding: Menimpa abstract method dari parent class
    @Override
    public void attack() {
        System.out.println(getName() + " melakukan serangan standar.");
    }

    // Konsep Overloading: Membuat method dengan nama yang sama (attack)
    // namun dengan parameter yang berbeda (String weapon)
    public void attack(String weapon) {
        System.out.println(getName() + " menyerang menggunakan " + weapon);
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

    public int getStamina() { return stamina; }
    public void setStamina(int stamina) { this.stamina = stamina; }
}