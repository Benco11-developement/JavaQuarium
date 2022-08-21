package fr.benco11.javaquarium;

import fr.benco11.javaquarium.io.AquariumReader;
import fr.benco11.javaquarium.io.AquariumWriter;
import fr.benco11.javaquarium.living.Eater;
import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.fish.GrouperFish;
import fr.benco11.javaquarium.living.fish.TunaFish;
import fr.benco11.javaquarium.living.kelp.Kelp;
import fr.benco11.javaquarium.living.kelp.KelpBasic;
import fr.benco11.javaquarium.options.OptionParseException;
import fr.benco11.javaquarium.options.OptionsParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaQuarium implements Aquarium {
    public static final Random RANDOM = new Random();

    private final Map<Integer, List<Living>> livingsToAddPerRound;

    private List<Kelp> kelps;
    private List<Fish> fishes;
    private int round;

    private JavaQuarium() {
        this.kelps = new ArrayList<>();
        this.fishes = new ArrayList<>();
        this.livingsToAddPerRound = new HashMap<>();
    }

    public JavaQuarium(List<Kelp> kelps, List<Fish> fishes, Map<Integer, List<Living>> livingsToAddPerRound) {
        this.kelps = kelps;
        this.fishes = fishes;
        this.livingsToAddPerRound = livingsToAddPerRound;
    }

    public static void main(String[] args) {
        OptionsParser options = new OptionsParser(args);
        Aquarium aquarium;
        if(options.isPresent("i")) {
            try(AquariumReader reader = new AquariumReader(new FileReader(options.option("i", String.class, new OptionParseException("i"))))) {
                aquarium = reader.readAquarium();
            } catch(IOException e) {
                throw new OptionParseException("Erreur de lecture du fichier de l'argument 'i'", e);
            }
        } else {
            aquarium = new JavaQuarium();
            aquarium.add(new GrouperFish("Baptiste", Fish.Sex.MALE));
            aquarium.add(new GrouperFish("Criquette", Fish.Sex.FEMALE));
            aquarium.add(new TunaFish("Méchant", Fish.Sex.MALE));
            aquarium.add(new TunaFish("Halo", Fish.Sex.MALE));
            aquarium.add(new TunaFish("Hérésie", Fish.Sex.MALE));
            aquarium.add(new TunaFish("Décadence", Fish.Sex.MALE));
            aquarium.add(new KelpBasic());
        }

        int rounds = (options.isPresent("r")) ? options.option("r", Integer.class, new OptionParseException("r")) : 20;
        Optional<File> output = (options.isPresent("o"))
                ? Optional.of(new File(options.option("o", String.class, new OptionParseException("o"))))
                : Optional.empty();

        int outputRound = (options.isPresent("oR")) ? options.option("oR", Integer.class, new OptionParseException("oR")) : rounds;

        for(int round = 1; round <= rounds; round++) {
            aquarium.update();
            if(round == outputRound && output.isPresent()) {
                try(AquariumWriter writer = new AquariumWriter(new FileWriter(output.get()))) {
                    writer.writeAquarium(aquarium);
                    writer.flush();
                } catch(IOException e) {
                    throw new OptionParseException("Erreur d'écriture du fichier de l'argument 'o'", e);
                }
            }
        }

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
        round++;
        List<Living> livingsToAdd = livingsToAddPerRound.getOrDefault(round, new ArrayList<>());
        List<Fish> fishesToAdd = livingsToAdd.stream().filter(Fish.class::isInstance).map(Fish.class::cast).collect(Collectors.toList());
        List<Kelp> kelpsToAdd = livingsToAdd.stream().filter(Kelp.class::isInstance).map(Kelp.class::cast).collect(Collectors.toList());

        fishes.forEach(fish -> {
            tryToEatIfHungry(fish);
            tryToReproduce(fish).ifPresent(fishesToAdd::add);
        });

        kelps.forEach(kelp -> tryToReproduce(kelp).ifPresent(kelpsToAdd::add));

        fishes = Stream.concat(fishes.stream().filter(Fish::tick), fishesToAdd.stream()).toList();
        kelps = Stream.concat(kelps.stream().filter(Kelp::tick), kelpsToAdd.stream()).toList();

        census();
    }

    private void tryToEatIfHungry(Fish fish) {
        if(fish.hungry()) {
            if(fish instanceof Eater.Carnivorous carnivorous) {
                randomFishExcludingOne(true, fish).ifPresent(carnivorous::eat);
            } else if(fish instanceof Eater.Herbivorous herbivorous) {
                randomKelp(true).ifPresent(herbivorous::eat);
            }
        }
    }

    private Optional<Fish> tryToReproduce(Fish fish) {
        Optional<Fish> other = randomFishExcludingOne(true, fish);
        return other.isEmpty() ? Optional.empty() : other.get().reproduce(fish);
    }

    private Optional<Kelp> tryToReproduce(Kelp kelp) {
        return kelp.reproduce();
    }

    public Optional<Fish> randomFishExcludingOne(boolean living, Fish excludeFish) {
        return randomLiving(living, fishes.stream().filter(fish -> fish != excludeFish).toList());
    }

    public Optional<Kelp> randomKelp(boolean living) {
        return randomLiving(living, kelps);
    }

    public <T extends Living> Optional<T> randomLiving(boolean isLiving, List<T> originalList) {
        List<T> livingFiltered = originalList.stream().filter(living -> !isLiving || living.alive()).toList();
        return (livingFiltered.isEmpty()) ? Optional.empty() : Optional.of(livingFiltered.get(RANDOM.nextInt(livingFiltered.size())));
    }

    @Override
    public void census() {
        System.out.println("\n----- Tour "+round+" -----");
        System.out.println("\nIl y a actuellement "+kelps.size()+" algue"+(kelps.size() > 1 ? "s" : "")+" dans l'aquarium");
        System.out.println("Recensement des poissons :\n");
        fishes.forEach(fish -> System.out.println(fish.name()+" est "+fish.sex()+" et agé(e) de "+fish.age()+"ans"));
    }

    @Override
    public List<Kelp> kelps() {
        return kelps;
    }

    @Override
    public List<Fish> fishes() {
        return fishes;
    }

    @Override
    public Map<Integer, List<Living>> remainingLivingsToAdd() {
        return livingsToAddPerRound.entrySet().stream().filter(entry -> entry.getKey() > round).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
