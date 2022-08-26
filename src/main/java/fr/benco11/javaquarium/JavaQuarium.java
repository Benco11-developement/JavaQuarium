package fr.benco11.javaquarium;

import fr.benco11.javaquarium.io.AquariumParser;
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
import fr.benco11.javaquarium.utils.ListUtils;
import fr.benco11.javaquarium.options.Options;
import fr.benco11.javaquarium.utils.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.benco11.javaquarium.options.Options.equalsOrNull;
import static fr.benco11.javaquarium.utils.StringUtils.*;

public class JavaQuarium implements Aquarium {
    public static final Random RANDOM = new Random();
    public static final int DEFAULT_ROUND_NUMBER = 20;

    public static void main(String[] args) {
        Options options = new OptionsParser().parse(args);
        Aquarium aquarium;
        if(options.isPresent("i")) {
            try(FileReader reader = new FileReader(options.option("i", String.class).orElseThrow(() -> new OptionParseException("i")))) {
                AquariumParser parser = new AquariumParser(reader);
                aquarium = parser.parseAquarium();
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

        int rounds = (options.isPresent("r")) ? options.option("r", Integer.class).orElseThrow(() -> new OptionParseException("r")) : DEFAULT_ROUND_NUMBER;
        Optional<File> output = options.option("o", String.class).map(File::new);
        if(options.isPresent("o") && output.isEmpty()) throw new OptionParseException("o");

        Optional<Integer> outputRound = options.option("oR", Integer.class);
        if(options.isPresent("oR") && outputRound.isEmpty()) throw new OptionParseException("oR");

        for(int round = 1; round <= rounds; round++) {
            aquarium.update();
            if(round == outputRound.orElse(rounds) && output.isPresent()) {
                try(AquariumWriter writer = new AquariumWriter(new FileWriter(output.get()))) {
                    writer.writeAquarium(aquarium);
                    writer.flush();
                } catch(IOException e) {
                    throw new OptionParseException("Erreur d'écriture du fichier de l'argument 'o'", e);
                }
            }
        }

    }

    private final Map<Integer, List<Living>> livingsToAddPerRound;
    private List<Kelp> kelps;
    private List<Fish> fishes;
    private int round;
    private final Map<Integer, Pair<List<Options>, List<Options>>> removeOptionsPerRound;

    public JavaQuarium() {
        this(new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>());
    }

    public JavaQuarium(List<Kelp> kelps, List<Fish> fishes, Map<Integer, List<Living>> livingsToAddPerRound,
                       Map<Integer, Pair<List<Options>, List<Options>>> removeOptionsPerRound) {
        this.kelps = kelps;
        this.fishes = fishes;
        this.livingsToAddPerRound = livingsToAddPerRound;
        this.removeOptionsPerRound = removeOptionsPerRound;
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

        // Initialise des listes avec les poissons et algues à ajouter à la fin du tour
        List<Living> livingsToAdd = livingsToAddPerRound.getOrDefault(round, new ArrayList<>());
        List<Fish> fishesToAdd = livingsToAdd.stream().filter(Fish.class::isInstance).map(Fish.class::cast).collect(Collectors.toList());
        List<Kelp> kelpsToAdd = livingsToAdd.stream().filter(Kelp.class::isInstance).map(Kelp.class::cast).collect(Collectors.toList());

        fishes.forEach(fish -> {
            tryToEatIfHungry(fish);
            tryToReproduce(fish).ifPresent(fishesToAdd::add);
        });

        kelps.forEach(kelp -> tryToReproduce(kelp).ifPresent(kelpsToAdd::add));

        // Concatène les poissons vivants avec les poissons à ajouter (pareil pour les algues)
        fishes = new ArrayList<>(Stream.concat(fishes.stream().filter(Fish::tick), fishesToAdd.stream()).toList());
        kelps = new ArrayList<>(Stream.concat(kelps.stream().filter(Kelp::tick), kelpsToAdd.stream()).toList());

        // Applique les filtres de suppression définis dans le fichier d'entrée ou non
        Pair<List<Options>, List<Options>> removeOptions = removeOptionsPerRound.getOrDefault(round, new Pair<>(new ArrayList<>(), new ArrayList<>()));
        removeOptions.first().forEach(option -> removeKelp(option, kelps));
        removeOptions.second().forEach(option -> removeFish(option, fishes));

        census();
    }

    @Override
    public void census() {
        System.out.println("\n----- Tour "+round+" -----");
        System.out.println("\nIl y a actuellement "+pluralInsert("algue", kelps.size())+" dans l'aquarium");
        System.out.println("Il y a actuellement "+pluralInsert("poisson", fishes.size())+" dans l'aquarium");
        System.out.println("Recensement des poissons :\n");
        fishes.forEach(fish -> System.out.println(fish.name()+" est "+indefiniteArticleAppend(fish.sex().name(), fish.sex())+" "+
                Fish.species(fish)+" "+sex("âgé", fish.sex())+" de "+pluralInsert("an", fish.age())+" avec "+pluralInsert("pv", fish.pv())));
    }

    @Override
    public Map<Integer, List<Living>> remainingLivingsToAdd() {
        return livingsToAddPerRound.entrySet().stream().filter(entry -> entry.getKey() > round).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<Integer, Pair<List<Options>, List<Options>>> remainingRemoveOptions() {
        return removeOptionsPerRound.entrySet().stream().filter(entry -> entry.getKey() > round).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<Kelp> kelps() {
        return kelps;
    }

    @Override
    public List<Fish> fishes() {
        return fishes;
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

    private void removeKelp(Options options, List<Kelp> kelps) {
        List<Kelp> kelpsFiltered = kelps.stream().filter(kelp -> equalsOrNull(kelp.age(), options.option("a"))).toList();
        kelps.removeAll(ListUtils.pickRandoms(kelpsFiltered, options.option("amount", Integer.class).orElse(kelpsFiltered.size())));
    }

    private void removeFish(Options options, List<Fish> fishes) {
        fishes.removeIf(fish -> equalsOrNull(fish.name(), options.option("n")) && equalsOrNull(Fish.species(fish), options.option("sp")) && equalsOrNull(fish.sex(), options.option("sx")) && equalsOrNull(fish.age(), options.option("a")));
    }
}
