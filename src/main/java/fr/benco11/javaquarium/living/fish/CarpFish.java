package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.utils.StringUtils;

import java.util.Optional;

/**
 * Carpe
 */
public final class CarpFish extends Fish.HerbivorousFish {
    /**
     * Construit avec un nom et un sexe et définit l'âge à 0 et le nombre de pvs à {@value DEFAULT_PV}
     *
     * @param name nom
     * @param sex  sexe
     */
    public CarpFish(String name, Sex sex) {
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
    public CarpFish(String name, Sex sex, int age, int pv) {
        super(name, sex, age, pv);
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof CarpFish) || other.sex() == sex || other == this)
            return Optional.empty();
        return Optional.of(new CarpFish(StringUtils.getFishName(this), Fish.Sex.randomSex()));
    }
}
