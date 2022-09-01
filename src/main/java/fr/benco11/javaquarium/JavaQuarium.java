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
import fr.benco11.javaquarium.options.Options;
import fr.benco11.javaquarium.options.OptionsParser;
import fr.benco11.javaquarium.utils.ListUtils;
import fr.benco11.javaquarium.utils.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.benco11.javaquarium.options.Options.AquariumOption.*;
import static fr.benco11.javaquarium.options.Options.LivingOption.AGE;
import static fr.benco11.javaquarium.options.Options.LivingOption.AMOUNT;
import static fr.benco11.javaquarium.options.Options.equalsOrEmpty;
import static fr.benco11.javaquarium.utils.StringUtils.*;

/**
 * JavaQuarium, implémentation par défaut de <code>Aquarium</code>
 */
public class JavaQuarium implements Aquarium {
    /**
     * <code>RANDOM</code> unique du projet
     */
    public static final Random RANDOM = new Random();

    /**
     * Nombre de tours de l'aquarium par défaut
     */
    public static final int DEFAULT_ROUND_NUMBER = 20;

    /**
     * Méthode d'entrée du programme
     *
     * @param args différents arguments (voir {@link fr.benco11.javaquarium.options.Options.AquariumOption})
     */
    public static void main(String[] args) {
        Options options = new OptionsParser().parse(args);

        Aquarium aquarium = loadAquarium(options);

        // Récupère le nombre de rounds et le fichier de sortie
        int rounds = (options.isPresent(ROUNDS))
                     ? options.option(ROUNDS, Integer.class)
                              .orElseThrow(() -> new OptionParseException(ROUNDS))
                     : DEFAULT_ROUND_NUMBER;
        Optional<File> output = options.option(OUTPUT, String.class)
                                       .map(File::new);
        if(options.isPresent(OUTPUT) && output.isEmpty()) throw new OptionParseException(OUTPUT);

        // Récupère le tour de sauvegarde, par défaut le nombre de rounds de simulation
        Optional<Integer> outputRound = options.option(OUTPUT_ROUND, Integer.class);
        if(options.isPresent(OUTPUT_ROUND) && outputRound.isEmpty())
            throw new OptionParseException(OUTPUT_ROUND);

        for(int round = 1; round <= rounds; round++) {
            // À chaque tour, simule un nouveau tour de l'aquarium et si c'est le tour d'enregistrement, enregistre dans le fichier de sortie
            aquarium.update();
            if(round == outputRound.orElse(rounds) && output.isPresent()) {
                try(AquariumWriter writer = new AquariumWriter(new FileWriter(output.get()))) {
                    writer.writeAquarium(aquarium);
                    writer.flush();
                } catch(IOException e) {
                    throw new OptionParseException("Erreur d'écriture du fichier '" + output.get()
                                                                                            .getPath() + "'", e);
                }
            }
        }

    }

    /**
     * Charge l'aquarium à partir des options d'aquarium
     *
     * @param options options d'aquarium
     * @return l'aquarium
     */
    private static Aquarium loadAquarium(Options options) {
        Aquarium aquarium;
        if(options.isPresent(INPUT)) {
            try(FileReader reader = new FileReader(options.option(INPUT, String.class)
                                                          .orElseThrow(() -> new OptionParseException(INPUT)))) {
                AquariumParser parser = new AquariumParser(reader);
                aquarium = parser.parseAquarium();
            } catch(IOException e) {
                throw new OptionParseException("Erreur de lecture du fichier '" + options.option(INPUT)
                                                                                         .orElse(null) + "'", e);
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
        return aquarium;
    }

    private final Map<Integer, List<Living>> livingsToAddPerRound;
    private final Map<Integer, Pair<List<Options>, List<Options>>> removeOptionsPerRound;
    private List<Kelp> kelps;
    private List<Fish> fishes;
    private int round;

    /**
     * Constructeur par défaut
     */
    public JavaQuarium() {
        this(new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>());
    }

    /**
     * Constructeur à partir de la liste des poissons et algues, des êtres à ajouter par tour et des filtres de suppression par tour
     *
     * @param kelps                 liste des algues
     * @param fishes                liste des poissons
     * @param livingsToAddPerRound  liste des êtres vivants à ajouter par tour
     * @param removeOptionsPerRound liste des filtres de suppression par tour
     */
    public JavaQuarium(List<Kelp> kelps, List<Fish> fishes,
            Map<Integer, List<Living>> livingsToAddPerRound,
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
        List<Fish> fishesToAdd = new ArrayList<>(livingsToAdd.stream()
                                                             .filter(Fish.class::isInstance)
                                                             .map(Fish.class::cast)
                                                             .toList());
        List<Kelp> kelpsToAdd = new ArrayList<>(livingsToAdd.stream()
                                                            .filter(Kelp.class::isInstance)
                                                            .map(Kelp.class::cast)
                                                            .toList());

        fishes.forEach(fish -> {
            tryToEatIfHungry(fish);
            tryToReproduce(fish).ifPresent(fishesToAdd::add);
        });

        kelps.forEach(kelp -> tryToReproduce(kelp).ifPresent(kelpsToAdd::add));

        // Concatène les poissons vivants avec les poissons à ajouter (pareil pour les algues)
        fishes = new ArrayList<>(Stream.concat(fishes.stream()
                                                     .filter(Fish::tick), fishesToAdd.stream())
                                       .toList());
        kelps = new ArrayList<>(Stream.concat(kelps.stream()
                                                   .filter(Kelp::tick), kelpsToAdd.stream())
                                      .toList());

        // Applique les filtres de suppression définis dans le fichier d'entrée ou non
        Pair<List<Options>, List<Options>> removeOptions = removeOptionsPerRound.getOrDefault(round, new Pair<>(new ArrayList<>(), new ArrayList<>()));
        removeOptions.first()
                     .forEach(option -> removeKelp(option, kelps));
        removeOptions.second()
                     .forEach(option -> removeFish(option, fishes));

        census();
    }

    @Override
    public void census() {
        System.out.println("\n----- Tour " + round + " -----");
        System.out.println("\nIl y a actuellement " + pluralInsert("algue", kelps.size()) + " dans l'aquarium");
        System.out.println("Il y a actuellement " + pluralInsert("poisson", fishes.size()) + " dans l'aquarium");
        System.out.println("Recensement des poissons :\n");
        fishes.forEach(fish -> System.out.println(fish.name() + " est " + indefiniteArticleAppend(fish.sex()
                                                                                                      .name(), fish.sex()) + " " +
                Fish.Species.of(fish)
                            .species() + " " + sex("âgé", fish.sex()) + " de " + pluralInsert("an", fish.age()) + " avec " + pluralInsert("pv", fish.pv())));
    }

    @Override
    public Map<Integer, List<Living>> remainingLivingsToAdd() {
        return livingsToAddPerRound.entrySet()
                                   .stream()
                                   .filter(entry -> entry.getKey() > round)
                                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<Integer, Pair<List<Options>, List<Options>>> remainingRemoveOptions() {
        return removeOptionsPerRound.entrySet()
                                    .stream()
                                    .filter(entry -> entry.getKey() > round)
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<Kelp> kelps() {
        return kelps;
    }

    @Override
    public List<Fish> fishes() {
        return fishes;
    }

    /**
     * Le poisson essaye de manger s'il a faim
     *
     * @param fish poisson
     */
    private void tryToEatIfHungry(Fish fish) {
        if(fish.hungry()) {
            if(fish instanceof Eater.Carnivorous carnivorous) {
                randomFishExcludingOne(true, fish).ifPresent(carnivorous::eat);
            } else if(fish instanceof Eater.Herbivorous herbivorous) {
                randomKelp(true).ifPresent(herbivorous::eat);
            }
        }
    }

    /**
     * Le poisson essaye de se reproduire avec un autre poisson au hasard
     *
     * @param fish poisson
     * @return un <code>Optional</code> contenant ou non un nouveau poisson
     */
    private Optional<Fish> tryToReproduce(Fish fish) {
        Optional<Fish> other = randomFishExcludingOne(true, fish);
        return other.isEmpty()
               ? Optional.empty()
               : other.get()
                      .reproduce(fish);
    }

    /**
     * L'algue essaye de se reproduire (diviser)
     *
     * @param kelp algue
     * @return un <code>Optional</code> contenant ou non une nouvelle algue
     */
    private Optional<Kelp> tryToReproduce(Kelp kelp) {
        return kelp.reproduce();
    }

    /**
     * Renvoie un poisson au hasard en excluant un
     *
     * @param living      si le poisson tiré doit être vivant
     * @param excludeFish poisson à exclure
     * @return un <code>Optional</code> contenant ou non un poisson
     */
    private Optional<Fish> randomFishExcludingOne(boolean living, Fish excludeFish) {
        return randomLiving(living, fishes.stream()
                                          .filter(fish -> fish != excludeFish)
                                          .toList());
    }

    /**
     * Renvoie une algue au hasard
     *
     * @param living si l'algue tirée doit être vivante
     * @return un <code>Optional</code> contenant ou non une algue
     */
    private Optional<Kelp> randomKelp(boolean living) {
        return randomLiving(living, kelps);
    }

    /**
     * Renvoie un être vivant au hasard
     *
     * @param isLiving     si l'être tiré doit être vivant
     * @param originalList liste des êtres parmis lequel l'être sera tiré
     * @param <T>          type de l'être
     * @return un <code>Optional</code> contenant ou non un être
     */
    private <T extends Living> Optional<T> randomLiving(boolean isLiving, List<T> originalList) {
        List<T> livingFiltered = originalList.stream()
                                             .filter(living -> !isLiving || living.alive())
                                             .toList();
        return (livingFiltered.isEmpty())
               ? Optional.empty()
               : Optional.of(livingFiltered.get(RANDOM.nextInt(livingFiltered.size())));
    }

    /**
     * Filtre les algues en retirant celles qui passent les filtres
     *
     * @param options filtres de suppression d'algues
     * @param kelps   liste des algues
     */
    private void removeKelp(Options options, List<Kelp> kelps) {
        List<Kelp> kelpsFiltered = kelps.stream()
                                        .filter(kelp -> removeFilter(kelp.age(), AGE, options))
                                        .toList();
        kelps.removeAll(ListUtils.pickRandoms(kelpsFiltered, options.option(AMOUNT, Integer.class)
                                                                    .orElse(kelpsFiltered.size())));
    }

    /**
     * Filtre les poissons en retirant ceux qui passent les filtres
     *
     * @param options filtres de suppression de poissons
     * @param fishes  liste des poissons
     */
    private void removeFish(Options options, List<Fish> fishes) {
        fishes.removeIf(fish -> Arrays.stream(Options.LivingOption.values())
                                      .allMatch(o -> removeFilter(
                                              switch(o) {
                                                  case SEX -> fish.sex();
                                                  case PV -> fish.pv();
                                                  case NAME -> fish.name();
                                                  case AGE -> fish.age();
                                                  case SPECIES -> Fish.Species.of(fish)
                                                                              .species();
                                                  case AMOUNT -> null;
                                              }
                                              , o, options)));
    }

    /**
     * Renvoie vrai si l'objet passe dans le filtre de suppression de l'option
     *
     * @param f        objet
     * @param optionId id de l'option
     * @param options  options
     * @return si l'objet est <code>null</code> ou si l'objet correspond à l'option ou si l'option est vide
     */
    private boolean removeFilter(Object f, Options.StandardOption optionId, Options options) {
        return f == null || equalsOrEmpty(f, options.option(optionId));
    }
}
