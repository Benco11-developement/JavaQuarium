package fr.benco11.javaquarium;

import fr.benco11.javaquarium.living.Eater;
import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.fish.GrouperFish;
import fr.benco11.javaquarium.living.fish.SoleFish;
import fr.benco11.javaquarium.living.kelp.Kelp;
import fr.benco11.javaquarium.living.kelp.KelpBasic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class JavaQuarium implements Aquarium {
    public static final Random RANDOM = new Random();

    public static void main(String[] args) {
        JavaQuarium javaQuarium = new JavaQuarium();
        javaQuarium.add(new KelpBasic());
        javaQuarium.add(new SoleFish("marcus", Fish.Sex.MALE));
        javaQuarium.add(new GrouperFish("MAAARC", Fish.Sex.FEMALE));
        for(int i = 0; i != 20; i++)
            javaQuarium.update();
    }

    private List<Kelp> kelps;
    private List<Fish> fishes;

    private JavaQuarium() {
        this.kelps = new ArrayList<>();
        this.fishes = new ArrayList<>();
    }

    @Override
    public void add(Living living) {
        if(living instanceof Kelp kelp) {
            this.kelps.add(kelp);
        } else if(living instanceof Fish fish) {
            this.fishes.add(fish);
        }
    }

    @Override
    public void remove(Living living) {
        if(living instanceof Kelp kelp) {
            this.kelps.remove(kelp);
        } else if(living instanceof Fish fish) {
            this.fishes.remove(fish);
        }
    }

    @Override
    public void update() {
        List<Fish> fishesToAdd = new ArrayList<>();
        List<Kelp> kelpsToAdd = new ArrayList<>();

        fishes.forEach(fish -> {
            tryToEatIfHungry(fish);
            try {
                tryToReproduce(fish).ifPresent(fishesToAdd::add);
            } catch(InvocationTargetException | IllegalAccessException | NoSuchMethodException |
                    InstantiationException e) {
                throw new RuntimeException(e);
            }
        });

        kelps.forEach(kelp -> tryToReproduce(kelp).ifPresent(kelpsToAdd::add));

        fishes = Stream.concat(fishes.stream().filter(Fish::tick), fishesToAdd.stream()).toList();
        kelps = Stream.concat(kelps.stream().filter(Kelp::tick), kelpsToAdd.stream()).toList();

        census();
    }

    private void tryToEatIfHungry(Fish fish) {
        if(fish.hungry()) {
            if(fish instanceof Eater.Carnivorous carnivorous) {
                randomFish(true).ifPresent(carnivorous::eat);
            } else if(fish instanceof Eater.Herbivorous herbivorous) {
                randomKelp(true).ifPresent(herbivorous::eat);
            }
        }
    }

    private Optional<Fish> tryToReproduce(Fish fish) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Optional<Fish> other = randomFish(true);
        return other.isEmpty() ? Optional.empty() : other.get().reproduce(fish);
    }

    private Optional<Kelp> tryToReproduce(Kelp kelp) {
        return kelp.reproduce();
    }

    public Optional<Fish> randomFish(boolean living) {
        return randomLiving(living, fishes);
    }

    public Optional<Kelp> randomKelp(boolean living) {
        return randomLiving(living, kelps);
    }

    public Optional<Living> randomLiving(boolean isLiving) {
        return randomLiving(isLiving, Stream.concat(kelps.stream(), fishes.stream()).toList());
    }

    public <T extends Living> Optional<T> randomLiving(boolean isLiving, List<T> originalList) {
        List<T> livingFiltered = originalList.stream().filter(living -> !isLiving || living.alive()).toList();
        return (livingFiltered.isEmpty()) ? Optional.empty() : Optional.of(livingFiltered.get(RANDOM.nextInt(livingFiltered.size())));
    }

    @Override
    public void census() {
        System.out.println("Il y a actuellement "+kelps.size()+" algue"+(kelps.size() > 1 ? "s" : "")+" dans l'aquarium");
        System.out.println("\nRecensement des poissons :\n");
        fishes.forEach(fish -> System.out.println(fish.name()+" : "+fish.sex()));
    }
}
