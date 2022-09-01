package fr.benco11.javaquarium.living.kelp;

import java.util.Optional;

/**
 * Implémentation par défaut de l'algue
 */
public final class KelpBasic implements Kelp {
    /**
     * Nombre de pvs par défaut d'une algue
     */
    public static final int DEFAULT_PV = 10;

    /**
     * Dégâts que subit l'algue lorsqu'elle se fait mordre
     */
    public static final int BITING_DAMAGE = 2;

    /**
     * Âge maximum d'une algue (inclusif)
     */
    public static final int MAX_AGE = 20;

    private int pv;
    private int age;

    /**
     * Constructeur par défaut, définit le nombre de pv à {@value DEFAULT_PV}
     */
    public KelpBasic() {
        this.pv = DEFAULT_PV;
    }

    /**
     * Construit avec le nombre de pvs
     *
     * @param pv nombre de pvs
     */
    public KelpBasic(int pv) {
        this.pv = pv;
    }

    @Override
    public boolean tick() {
        pv++;
        age++;
        return alive();
    }

    @Override
    public boolean alive() {
        return pv > 0 && age <= MAX_AGE;
    }

    @Override
    public void bitten() {
        pv -= KelpBasic.BITING_DAMAGE;
    }

    @Override
    public int age() {
        return age;
    }

    @Override
    public int pv() {
        return pv;
    }

    @Override
    public Optional<Kelp> reproduce() {
        if(pv >= 10) {
            pv /= 2;
            return Optional.of(new KelpBasic(pv));
        }
        return Optional.empty();
    }
}
