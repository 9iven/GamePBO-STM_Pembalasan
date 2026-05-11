package actors.roles;
import actors.core.Siswa;
import java.util.Random;

// Merepresentasikan musuh standar
public class Bully extends Siswa {
    private int expReward;
    private String gangFaction;

    public Bully(String name, int hp, int baseDamage, int expReward, String gangFaction) {
        super(name, hp, baseDamage);
        this.expReward = expReward;
        this.gangFaction = gangFaction;
    }

    @Override
    public void attack() {
        int randomDamage = getBaseDamage() + new Random().nextInt(5);
        System.out.println(getName() + " menyerang dengan damage " + randomDamage);
    }

    public int getExpReward() { return expReward; }
    public String getGangFaction() { return gangFaction; }
}