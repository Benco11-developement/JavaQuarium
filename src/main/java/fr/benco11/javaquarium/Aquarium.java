package fr.benco11.javaquarium;

import fr.benco11.javaquarium.living.Living;

public interface Aquarium {
    void add(Living living);
    void remove(Living living);
    void update();
    void census();
}
