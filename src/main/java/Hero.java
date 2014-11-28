import java.util.List;
import java.util.Random;

public abstract class Hero {
    private static final int DROW_SLOW = 200;

    protected int health;
    private int damage;
    private int attackSpeed;
    private int hitChance;
    protected Arena arena;
    private int nextAttackTime;
    protected int slowedAttackCount;

    private Random random = new Random();

    private Hero(Arena arena, int health, int damage, int attackSpeed, int hitChance) {
        this.arena = arena;
        this.health = health;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.hitChance = hitChance;
        this.nextAttackTime = attackSpeed;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void attack() {
        Hero opponent = chooseOpponent();
        System.out.println(this.getName() + " is attacking " + opponent.getName());
        if (tryAttack()) {
            giveDamage(opponent);
        }
        nextAttackTime += attackSpeed;
        if (slowedAttackCount > 0) {
            nextAttackTime += DROW_SLOW;
            slowedAttackCount--;
        }
    }

    protected void giveDamage(Hero opponent) {
        System.out.println(this.getName() + " has hit " + opponent.getName() + " for " + damage + " damage.");
        opponent.takeDamage(damage);
    }

    private void takeDamage(final int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            die();
        } else {
            System.out.println(this.getName() + " has " + health + " health remaining.");
        }
    }

    protected void die() {
        System.out.println(this.getName() + " has died!");
        arena.die(this);
    }

    private boolean tryAttack() {
        return (Math.abs(random.nextInt()) % 100) <= hitChance;
    }

    protected Hero chooseOpponent() {
        List<Hero> opponents = arena.getOpponents(this);
        int opponentIndex = Math.abs(random.nextInt()) % opponents.size();
        return opponents.get(opponentIndex);
    }

    public int getNextAttackTime() {
        return nextAttackTime;
    }

    public static final class Luna extends Hero {
        public Luna(Arena arena) {
            super(arena, 500, 100, 500, 50);
        }

        @Override
        protected Hero chooseOpponent() {
            List<Hero> opponents = arena.getOpponents(this);
            Hero opponent = null;
            for (Hero hero : opponents) {
                if (opponent == null || hero.health < opponent.health) {
                    opponent = hero;
                }
            }
            return opponent;
        }
    }

    public static final class Axe extends Hero {
        public Axe(Arena arena) {
            super(arena, 900, 50, 800, 60);
        }

        @Override
        protected void giveDamage(final Hero opponent) {
            super.giveDamage(opponent);
            for (Hero hero : arena.getOpponents(this)) {
                if (hero != opponent) {
                    hero.takeDamage(10);
                }
            }
        }
    }

    public static final class WraithKing extends Hero {
        private int deathCount = 0;
        private static final int INITIAL_HEALTH = 700;

        public WraithKing(Arena arena) {
            super(arena, INITIAL_HEALTH, 60, 900, 80);
        }

        @Override
        protected void die() {
            if (deathCount == 0) {
                System.out.println(this.getClass().getSimpleName() + " has died but is reborn!");
                deathCount++;
                health = INITIAL_HEALTH / 2;
            } else {
                super.die();
            }
        }
    }

    public static final class DrowRanger extends Hero {
        public DrowRanger(Arena arena) {
            super(arena, 500, 120, 400, 70);
        }

        @Override
        protected void giveDamage(final Hero opponent) {
            super.giveDamage(opponent);
            opponent.nextAttackTime += DROW_SLOW;
            opponent.slowedAttackCount = 1;
        }
    }
}
