package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.living.Eater;
import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.kelp.Kelp;

import java.util.Arrays;
import java.util.Optional;

import static fr.benco11.javaquarium.JavaQuarium.RANDOM;

public abstract sealed class Fish implements Living {
    public static final int DEFAULT_PV = 10;

    public static Fish reInstantiateFish(Species species, String name, Sex sex, int age, int pv) {
        return switch(species) {
            case BASS -> new BassFish(name, sex, age, pv);
            case CARP -> new CarpFish(name, sex, age, pv);
            case CLOWN_FISH -> new ClownFish(name, sex, age, pv);
            case GROUPER -> new GrouperFish(name, sex, age, pv);
            case SOLE -> new SoleFish(name, sex, age, pv);
            case TUNA -> new TunaFish(name, sex, age, pv);
        };
    }

    protected final String name;
    protected Sex sex;
    protected int age;
    protected int pv;

    protected Fish(String name, Sex sex) {
        this.name = name;
        this.sex = sex;
        pv = DEFAULT_PV;
    }

    protected Fish(String name, Sex sex, int age, int pv) {
        this(name, sex);
        this.age = age;
        this.pv = pv;
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
        pv--;
        age++;
        return alive();
    }

    @Override
    public boolean alive() {
        return pv > 0 && age <= 20;
    }

    @Override
    public void bitten() {
        pv -= 4;
    }

    public boolean hungry() {
        return pv <= 5;
    }

    public abstract Optional<Fish> reproduce(Fish other);

    public enum Sex {
        MALE, FEMALE;

        public static Sex randomSex() {
            return values()[RANDOM.nextInt(values().length)];
        }

        public static Sex opposite(Sex sex) {
            return sex == MALE ? FEMALE : MALE;
        }

        public static Optional<Sex> of(String s) {
            return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(s)).findAny();
        }
    }

    public enum Species {
        BASS("Bar"), CARP("Carpe"), CLOWN_FISH("Poisson-Clown"), GROUPER("Mérou"), SOLE("Sole"), TUNA("Thon");

        public static Species of(Fish fish) {
            return switch(fish) {
                case ClownFish ignored -> CLOWN_FISH;
                case GrouperFish ignored -> GROUPER;
                case TunaFish ignored -> TUNA;
                case BassFish ignored -> BASS;
                case CarpFish ignored -> CARP;
                case SoleFish ignored -> SOLE;
            };
        }

        public static Optional<Species> of(String species) {
            return Arrays.stream(values()).filter(sp -> sp.species().equals(species)).findAny();
        }

        private final String name;

        Species(String name) {
            this.name = name;
        }

        public String species() {
            return name;
        }
    }

    public abstract static sealed class CarnivorousFish extends Fish implements Eater.Carnivorous permits ClownFish, GrouperFish, TunaFish {
        protected CarnivorousFish(String name, Sex sex) {
            super(name, sex);
        }

        protected CarnivorousFish(String name, Sex sex, int age, int pv) {
            super(name, sex, age, pv);
        }

        @Override
        public void eat(Fish fish) {
            if(fish.getClass().equals(getClass())) return;
            fish.bitten();
            pv += 5;
        }
    }

    public abstract static sealed class HerbivorousFish extends Fish implements Eater.Herbivorous permits BassFish, CarpFish, SoleFish {
        protected HerbivorousFish(String name, Sex sex) {
            super(name, sex);
        }

        protected HerbivorousFish(String name, Sex sex, int age, int pv) {
            super(name, sex, age, pv);
        }

        @Override
        public void eat(Kelp kelp) {
            kelp.bitten();
            pv += 3;
        }
    }
}
