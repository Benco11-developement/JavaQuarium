package fr.benco11.javaquarium.living.fish;

import java.util.Optional;

/**
 * Clownfish (Poisson-clown)
 */
public final class ClownFish extends Fish.CarnivorousFish {

    public ClownFish(String name, Sex sex) {
        super(name, sex);
    }

    @Override
    public Optional<Fish> reproduce(Fish other) {
        if(!(other instanceof ClownFish) || other == this) return Optional.empty();
        sex = Fish.Sex.opposite(other.sex());
        return Optional.of(new ClownFish("", Fish.Sex.randomSex()));
    }
}
