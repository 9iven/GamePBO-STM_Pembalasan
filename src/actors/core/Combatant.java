package actors.core;

// Konsep Interface: Menetapkan aturan baku bahwa setiap entitas yang bertarung
// wajib memiliki method takeDamage.
public interface Combatant {
    void takeDamage(int amount);
}