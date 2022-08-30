package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.utils.StringUtils;

import java.util.Optional;

/**
 * Bass (bar)
 */
public final class BassFish extends Fish.HerbivorousFish {

    public BassFish(String name, Sex sex) {
        super(name, sex);
    }

    public BassFish(String name, Sex sex, int age, int pv) {
        super(name, sex, age, pv);
    }

    @Override
    public boolean tick() {
        boolean tick = super.tick();
        if(age == 10) sex = Sex.opposite(sex);
        return tick;
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof BassFish) || other.sex() == sex || other == this) return Optional.empty();
        return Optional.of(new BassFish(StringUtils.getFishName(this), Sex.randomSex()));
    }
}
