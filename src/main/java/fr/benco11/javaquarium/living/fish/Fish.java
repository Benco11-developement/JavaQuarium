package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.living.Eater;
import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.kelp.Kelp;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static fr.benco11.javaquarium.JavaQuarium.RANDOM;

public sealed class Fish implements Living {
    protected String name;
    protected Sex sex;
    protected int age;
    protected int pv;

    protected boolean hasEaten;

    protected Fish(String name, Sex sex) {
        this.name = name;
        this.sex = sex;
        pv = 10;
    }

    public String name() {
        return name;
    }

    public Sex sex() {
        return sex;
    }

    public int age() {
        return age;
    }

    public int pv() {
        return pv;
    }

    @Override
    public boolean tick() {
        if(!hasEaten) pv--;
        hasEaten = false;
        age++;
        return alive();
    }

    @Override
    public void bitten() {
        pv -= 4;
    }

    @Override
    public boolean alive() {
        return pv > 0 && age <= 20;
    }

    public boolean hungry() {
        return pv <= 5;
    }

    public Optional<Fish> reproduce(Fish other) {
        throw new UnsupportedOperationException("Reproduce method can only be implemented in subclasses");
    }

    static sealed class CarnivorousFish extends Fish implements Eater.Carnivorous permits ClownFish, GrouperFish, TunaFish {
        protected CarnivorousFish(String name, Sex sex) {
            super(name, sex);
        }

        @Override
        public void eat(Fish fish) {
            fish.bitten();
            if(fish.getClass().equals(getClass()) && fish != this) {
                pv += 5;
                hasEaten = true;
            }
        }
    }

    public static sealed class HerbivorousFish extends Fish implements Eater.Herbivorous permits BassFish, CarpFish, SoleFish {
        protected HerbivorousFish(String name, Sex sex) {
            super(name, sex);
        }

        @Override
        public void eat(Kelp kelp) {
            kelp.bitten();
            pv += 3;
            hasEaten = true;
        }
    }

    public enum Sex {
        MALE, FEMALE;

        public static Sex randomSex() {
            return values()[RANDOM.nextInt(values().length)];
        }

        public static Sex opposite(Sex sex) {
            return sex == MALE ? FEMALE : MALE;
        }
    }
}
