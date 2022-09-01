package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.living.Eater;
import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.kelp.Kelp;

import java.util.Arrays;
import java.util.Optional;

import static fr.benco11.javaquarium.JavaQuarium.RANDOM;

/**
 * Poisson
 */
public abstract sealed class Fish implements Living {
    /**
     * Nombre de pvs par défaut d'un poisson
     */
    public static final int DEFAULT_PV = 10;

    /**
     * Seuil sous lequel un poisson a faim (maximum inclusif)
     */
    public static final int HUNGER_THRESHOLD = 5;

    /**
     * Nombre de pvs que perd un poisson lorsqu'il se fait mordre
     */
    public static final int BITING_DAMAGE = 4;

    /**
     * Âge maximal d'un poisson (inclusif)
     */
    public static final int MAX_AGE = 20;

    /**
     * Retrouve un poisson à partir de ses propriétés
     *
     * @param species espèce du poisson
     * @param name    nom du poisson
     * @param sex     sexe du poisson
     * @param age     âge du poisson
     * @param pv      nombre de pvs du poisson
     * @return poisson <code>Fish</code>
     */
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

    /**
     * Nom du poisson
     */
    protected final String name;

    /**
     * Sexe du poisson
     */
    protected Sex sex;

    /**
     * Âge du poisson
     */
    protected int age;

    /**
     * Nombre de pvs du poisson
     */
    protected int pv;

    /**
     * Construit avec un nom et un sexe et définit l'âge à 0 et le nombre de pvs à {@value DEFAULT_PV}
     *
     * @param name nom
     * @param sex  sexe
     */
    protected Fish(String name, Sex sex) {
        this.name = name;
        this.sex = sex;
        pv = DEFAULT_PV;
    }

    /**
     * Constructeur à partir d'un nom, d'un sexe, de l'âge et du nombre de pvs
     *
     * @param name nom
     * @param sex  sexe
     * @param age  âge
     * @param pv   nombre de pvs
     */
    protected Fish(String name, Sex sex, int age, int pv) {
        this(name, sex);
        this.age = age;
        this.pv = pv;
    }

    /**
     * Renvoie le nom du poisson
     *
     * @return le nom
     */
    public String name() {
        return name;
    }

    /**
     * Renvoie le sexe du poisson
     *
     * @return le sexe
     */
    public Sex sex() {
        return sex;
    }

    @Override
    public boolean tick() {
        pv--;
        age++;
        return alive();
    }

    @Override
    public boolean alive() {
        return pv > 0 && age <= MAX_AGE;
    }

    @Override
    public void bitten() {
        pv -= BITING_DAMAGE;
    }

    @Override
    public int age() {
        return age;
    }

    @Override
    public int pv() {
        return pv;
    }

    /**
     * Renvoie vrai si le poisson a faim et cherche à manger
     *
     * @return si le poisson a faim
     */
    public boolean hungry() {
        return pv <= HUNGER_THRESHOLD;
    }

    /**
     * Tente de se reproduire avec un autre poisson
     *
     * @param other autre poisson avec qui se reproduire
     * @return un <code>Optional</code> contenant ou non un nouveau poisson
     */
    public abstract Optional<Fish> reproduce(Fish other);

    /**
     * Énumération des sexes
     */
    public enum Sex {
        /**
         * Sexe mâle
         */
        MALE,
        /**
         * Sexe femelle
         */
        FEMALE;

        /**
         * Renvoie un sexe au hasard
         *
         * @return sexe aléatoire
         */
        public static Sex randomSex() {
            return values()[RANDOM.nextInt(values().length)];
        }

        /**
         * Renvoie le sexe opposé
         *
         * @param sex sexe
         * @return sexe opposé
         */
        public static Sex opposite(Sex sex) {
            return sex == MALE ? FEMALE : MALE;
        }

        /**
         * Renvoie un sexe
         *
         * @param s <code>String</code> du sexe
         * @return un <code>Optional</code> contenant ou non sexe
         */
        public static Optional<Sex> of(String s) {
            return Arrays.stream(values())
                         .filter(v -> v.name()
                                       .equalsIgnoreCase(s))
                         .findAny();
        }
    }

    /**
     * Énumération des espèces de poissons
     */
    public enum Species {
        /**
         * Bar
         */
        BASS("Bar"),
        /**
         * Carpe
         */
        CARP("Carpe"),
        /**
         * Poisson-Clown
         */
        CLOWN_FISH("Poisson-Clown"),
        /**
         * Mérou
         */
        GROUPER("Mérou"),
        /**
         * Sole
         */
        SOLE("Sole"),
        /**
         * Thon
         */
        TUNA("Thon");

        /**
         * Renvoie l'espèce d'un poisson
         *
         * @param fish poisson
         * @return le <code>Species</code> correspondant
         */
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

        /**
         * Renvoie une espèce
         *
         * @param species nom de l'espèce
         * @return un <code>Optional</code> contenant ou non le <code>Species</code> du poisson
         */
        public static Optional<Species> of(String species) {
            return Arrays.stream(values())
                         .filter(sp -> sp.species()
                                         .equals(species))
                         .findAny();
        }

        private final String name;

        Species(String name) {
            this.name = name;
        }

        /**
         * Renvoie le nom de l'espèce
         *
         * @return nom de l'espèce
         */
        public String species() {
            return name;
        }
    }

    /**
     * Poisson carnivore
     */
    public abstract static sealed class CarnivorousFish extends Fish implements Eater.Carnivorous permits ClownFish, GrouperFish, TunaFish {

        /**
         * Construit avec un nom et un sexe et définit l'âge à 0 et le nombre de pvs à {@value DEFAULT_PV}
         *
         * @param name nom
         * @param sex  sexe
         */
        protected CarnivorousFish(String name, Sex sex) {
            super(name, sex);
        }

        /**
         * Construit avec un nom, un sexe, l'âge et le nombre de pvs
         *
         * @param name nom
         * @param sex  sexe
         * @param age  âge
         * @param pv   nombre de pvs
         */
        protected CarnivorousFish(String name, Sex sex, int age, int pv) {
            super(name, sex, age, pv);
        }

        @Override
        public void eat(Fish fish) {
            if(fish.getClass()
                   .equals(getClass())) return;
            fish.bitten();
            pv += 5;
        }
    }

    /**
     * Poisson herbivore
     */
    public abstract static sealed class HerbivorousFish extends Fish implements Eater.Herbivorous permits BassFish, CarpFish, SoleFish {

        /**
         * Construit avec un nom et un sexe et définit l'âge à 0 et le nombre de pvs à {@value DEFAULT_PV}
         *
         * @param name nom
         * @param sex  sexe
         */
        protected HerbivorousFish(String name, Sex sex) {
            super(name, sex);
        }

        /**
         * Construit avec un nom, un sexe, l'âge et le nombre de pvs
         *
         * @param name nom
         * @param sex  sexe
         * @param age  âge
         * @param pv   nombre de pvs
         */
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
