package fr.benco11.javaquarium;

import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;
import fr.benco11.javaquarium.utils.Options;
import fr.benco11.javaquarium.utils.Pair;

import java.util.List;
import java.util.Map;

public interface Aquarium {
    void add(Living living);

    void remove(Living living);

    void update();

    void census();

    Map<Integer, List<Living>> remainingLivingsToAdd();

    Map<Integer, Pair<List<Options>, List<Options>>> remainingRemoveOptions();

    List<Kelp> kelps();

    List<Fish> fishes();
}
