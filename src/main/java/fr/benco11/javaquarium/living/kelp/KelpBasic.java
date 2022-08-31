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
    private int pv;
    private int age;

    /**
     * Constructeur par défaut
     */
    public KelpBasic() {
        this.pv = DEFAULT_PV;
    }

    /**
     * Constructeur à partir du nombre de pvs
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
        return pv > 0 && age <= 20;
    }

    @Override
    public void bitten() {
        pv -= 2;
    }

    @Override
    public Optional<Kelp> reproduce() {
        if(pv >= 10) {
            pv /= 2;
            return Optional.of(new KelpBasic(pv));
        }
        return Optional.empty();
    }

    @Override
    public int age() {
        return age;
    }

    @Override
    public int pv() {
        return pv;
    }
}
