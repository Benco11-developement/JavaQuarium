package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.utils.StringUtils;

import java.util.Optional;

/**
 * Sole
 */
public final class SoleFish extends Fish.HerbivorousFish {

    /**
     * Constructeur à partir d'un nom et d'un sexe
     *
     * @param name nom
     * @param sex  sexe
     */
    public SoleFish(String name, Sex sex) {
        super(name, sex);
    }

    /**
     * Constructeur à partir d'un nom, d'un sexe, de l'âge et du nombre de pvs
     *
     * @param name nom
     * @param sex  sexe
     * @param age  âge
     * @param pv   nombre de pvs
     */
    public SoleFish(String name, Sex sex, int age, int pv) {
        super(name, sex, age, pv);
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof SoleFish) || other == this) return Optional.empty();
        sex = Fish.Sex.opposite(other.sex());
        return Optional.of(new SoleFish(StringUtils.getFishName(this), Fish.Sex.randomSex()));
    }
}
