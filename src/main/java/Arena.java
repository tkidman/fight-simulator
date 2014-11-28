import java.util.ArrayList;
import java.util.List;

public class Arena {
    private List<Hero> heroes = new ArrayList<Hero>();

    public Arena() {
        heroes.add(new Hero.Luna(this));
        heroes.add(new Hero.Axe(this));
        heroes.add(new Hero.WraithKing(this));
        heroes.add(new Hero.DrowRanger(this));
    }

    public void fight() {
        while (heroes.size() > 1) {
            getNextAttacker().attack();
        }
        System.out.println("The fight is over. " + heroes.get(0).getName() + " stands triumphant!");
    }

    private Hero getNextAttacker() {
        Hero nextAttacker = null;
        for (Hero hero : heroes) {
            if (nextAttacker == null || hero.getNextAttackTime() <= nextAttacker.getNextAttackTime()) {
                nextAttacker = hero;
            }
        }
        return nextAttacker;
    }

    public List<Hero> getOpponents(final Hero hero) {
        List<Hero> opponents = new ArrayList<Hero>(heroes);
        opponents.remove(hero);
        return opponents;
    }

    public void die(final Hero hero) {
        this.heroes.remove(hero);
    }

    public static void main(String[] args) {
        new Arena().fight();
    }
}
