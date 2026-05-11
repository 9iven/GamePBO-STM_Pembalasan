package actors.core;

// Konsep Abstract Class: Menjadi blueprint utama. Tidak bisa diinstansiasi langsung.
// Class ini melakukan implements pada interface Combatant.
public abstract class Siswa implements Combatant {
    // Konsep Encapsulation: Menggunakan modifier private agar data tidak bisa
    // dimanipulasi secara langsung dari luar class.
    private String name;
    private int hp;
    private int baseDamage;

    // Constructor untuk inisialisasi awal nilai atribut
    public Siswa(String name, int hp, int baseDamage) {
        this.name = name;
        this.hp = hp;
        this.baseDamage = baseDamage;
    }

    // Abstract method: Memaksa semua class turunan (subclass) untuk
    // membuat logika serangannya masing-masing (Polymorphism).
    public abstract void attack();

    // Implementasi method dari interface Combatant
    @Override
    public void takeDamage(int amount) {
        // Mencegah nilai HP menjadi minus menggunakan fungsi Math.max
        this.hp = Math.max(0, this.hp - amount);
    }

    // Penyediaan Getter dan Setter untuk mengakses data private (Encapsulation)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public int getBaseDamage() { return baseDamage; }
    public void setBaseDamage(int baseDamage) { this.baseDamage = baseDamage; }
}