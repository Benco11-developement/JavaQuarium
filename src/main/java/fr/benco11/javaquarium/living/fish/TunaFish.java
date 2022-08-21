package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.utils.NameUtils;

import java.util.Optional;

/**
 * Tuna (thon)
 */
public final class TunaFish extends Fish.CarnivorousFish {
    public TunaFish(String name, Sex sex) {
        super(name, sex);
    }

    public TunaFish(String name, Sex sex, int age) {
        super(name, sex, age);
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof TunaFish) || other.sex() == sex || other == this) return Optional.empty();
        return Optional.of(new TunaFish(NameUtils.getName(this), Fish.Sex.randomSex()));
    }
}
