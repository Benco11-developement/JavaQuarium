package fr.benco11.javaquarium.living.fish;

import fr.benco11.javaquarium.utils.StringUtils;

import java.util.Optional;

/**
 * Grouper (Mérou)
 */
public final class GrouperFish extends Fish.CarnivorousFish {
    public GrouperFish(String name, Sex sex) {
        super(name, sex);
    }

    public GrouperFish(String name, Sex sex, int age, int pv) {
        super(name, sex, age, pv);
    }

    @Override
    public boolean tick() {
        boolean tick = super.tick();
        if(age == 10) sex = Fish.Sex.opposite(sex);
        return tick;
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof GrouperFish) || other.sex() == sex || other == this) return Optional.empty();
        return Optional.of(new GrouperFish(StringUtils.getFishName(this), Fish.Sex.randomSex()));
    }
}
