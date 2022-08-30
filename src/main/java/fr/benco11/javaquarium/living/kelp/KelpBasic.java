package fr.benco11.javaquarium.living.kelp;

import java.util.Optional;

public final class KelpBasic implements Kelp {
    public static final int DEFAULT_PV = 10;
    private int pv;
    private int age;

    public KelpBasic() {
        this.pv = DEFAULT_PV;
    }

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
}
