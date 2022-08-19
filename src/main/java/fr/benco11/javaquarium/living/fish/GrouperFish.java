package fr.benco11.javaquarium.living.fish;

import java.util.Optional;

/**
 * Grouper (Mérou)
 */
public final class GrouperFish extends Fish.CarnivorousFish {
    public GrouperFish(String name, Sex sex) {
        super(name, sex);
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
        return Optional.of(new GrouperFish("", Fish.Sex.randomSex()));
    }
}
