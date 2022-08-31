package fr.benco11.javaquarium.io;

import fr.benco11.javaquarium.Aquarium;
import fr.benco11.javaquarium.JavaQuarium;
import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;
import fr.benco11.javaquarium.living.kelp.KelpBasic;
import fr.benco11.javaquarium.options.Options;
import fr.benco11.javaquarium.utils.IntegerUtils;
import fr.benco11.javaquarium.utils.Pair;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static fr.benco11.javaquarium.options.Options.LivingOption.AGE;
import static fr.benco11.javaquarium.options.Options.LivingOption.AMOUNT;
import static fr.benco11.javaquarium.utils.StringUtils.nullOrEmpty;

public class AquariumParser {
    private static final Pattern PATTERN_KELP = Pattern.compile("(\\d+)\\s*algues?\\s*(\\d+)\\s*ans?");
    private static final Pattern PATTERN_FISH = Pattern.compile("([^,\\s]+)\\s*,\\s*([^,\\s]+)\\s*,\\s*([^,\\s]+)\\s*(,\\s*(\\d+)\\s*ans?\\s*)?(,\\s*(\\d+)\\s*pvs?)?");
    private static final Pattern PATTERN_REMOVE_KELP = Pattern.compile("-\\s*(\\d+)?\\s*algues?\\s*((\\d+)?\\s*ans?)?");
    private static final Pattern PATTERN_REMOVE_FISH_OPTION = Pattern.compile("([a-zA-Z]+):([^,]+)");
    private static final Pattern PATTERN_ROUND = Pattern.compile("=====\\s+Tour\\s+(\\d+)\\s+=====");


    private final BufferedReader in;

    public AquariumParser(BufferedReader in) {
        this.in = in;
    }

    public AquariumParser(Reader in) {
        this(new BufferedReader(in));
    }

    public Aquarium parseAquarium() {
        List<String> lines = in.lines().toList();

        List<Kelp> kelps = new ArrayList<>();
        List<Fish> fishes = new ArrayList<>();

        Map<Integer, List<Living>> livingsToAdd = new HashMap<>();
        Map<Integer, Pair<List<Options>, List<Options>>> removeOptions = new HashMap<>();

        AtomicInteger round = new AtomicInteger(-1);
        AtomicInteger lineIndex = new AtomicInteger(0);

        lines.forEach(line -> {
            line = line.trim();
            lineIndex.addAndGet(1);
            if(line.startsWith("//") || line.equals("")) return;

            Matcher matcherRound = PATTERN_ROUND.matcher(line);
            if(matcherRound.find()) {
                parseRound(matcherRound, round, lineIndex);
                return;
            }

            if(line.startsWith("-")) {
                parseLineRemove(line, removeOptions, round, lineIndex);
                return;
            }

            parseLineAdd(line, kelps, fishes, livingsToAdd, round, lineIndex);
        });

        return new JavaQuarium(kelps, fishes, livingsToAdd, removeOptions);
    }

    private void parseLineRemove(String line, Map<Integer, Pair<List<Options>, List<Options>>> removeOptions, AtomicInteger round, AtomicInteger lineIndex) {
        Matcher matcherKelp = PATTERN_REMOVE_KELP.matcher(line);
        Matcher matcherFishOptions = PATTERN_REMOVE_FISH_OPTION.matcher(line);

        if(matcherKelp.find()) {
            parseKelpRemove(matcherKelp, removeOptions, round, lineIndex);
            return;
        }

        if(line.startsWith("-poisson") && matcherFishOptions.find()) {
            parseFishRemove(matcherFishOptions, removeOptions, round, lineIndex);
            return;
        }

        throw new AquariumReadException(lineIndex.get());

    }

    private void parseKelpRemove(Matcher matcherKelp, Map<Integer, Pair<List<Options>, List<Options>>> removeOptions, AtomicInteger round, AtomicInteger lineIndex) {
        Optional<Integer> count = IntegerUtils.of(matcherKelp.group(1));
        Optional<Integer> age = IntegerUtils.of(matcherKelp.group(3));
        if(count.isEmpty() && age.isEmpty())
            throw new AquariumReadException("Erreur de lecture de(s) l'algue(s) à supprimer à la ligne "+lineIndex.get());
        removeOptions.computeIfAbsent(round.get(), k -> new Pair<>(new ArrayList<>(), new ArrayList<>())).first().add(new Options(Map.of(AMOUNT.id(), count, AGE.id(), age)));
    }

    private void parseFishRemove(Matcher matcherFish, Map<Integer, Pair<List<Options>, List<Options>>> removeOptions, AtomicInteger round, AtomicInteger lineIndex) {
        Options options = new Options();
        do {
            String idString = matcherFish.group(1);
            String valueString = matcherFish.group(2);

            Options.LivingOption id = Options.LivingOption.of(idString).orElseThrow(() -> new AquariumReadException("Erreur de lecture de l'option '"+idString+"' à la ligne "+lineIndex.get()));
            Optional<?> value = switch(id) {
                case AGE, PV -> IntegerUtils.of(valueString.replaceAll("\\D+", ""));
                case NAME, SPECIES -> Optional.of(valueString).filter(n -> !nullOrEmpty(n));
                case SEX -> Fish.Sex.of(valueString);
                case AMOUNT ->
                        throw new AquariumReadException("Erreur de lecture de l'option '"+idString+"' à la ligne "+lineIndex.get());
            };

            if(value.isEmpty())
                throw new AquariumReadException("Erreur de lecture de la valeur de l'option '"+idString+"' à la ligne "+lineIndex.get());
            options.add(id, value);
        } while(matcherFish.find());
        removeOptions.computeIfAbsent(round.get(), k -> new Pair<>(new ArrayList<>(), new ArrayList<>())).second().add(options);
    }

    private void parseLineAdd(String line, List<Kelp> kelps, List<Fish> fishes, Map<Integer, List<Living>> livingsToAdd, AtomicInteger round, AtomicInteger lineIndex) {
        Matcher matcherKelp = PATTERN_KELP.matcher(line);
        Matcher matcherFish = PATTERN_FISH.matcher(line);

        if(matcherKelp.find()) {
            parseKelpAdd(matcherKelp, kelps, livingsToAdd, round, lineIndex);
            return;
        }

        if(matcherFish.find()) {
            parseFishAdd(matcherFish, fishes, livingsToAdd, round, lineIndex);
            return;
        }

        throw new AquariumReadException(lineIndex.get());
    }

    private void parseKelpAdd(Matcher matcherKelp, List<Kelp> kelps, Map<Integer, List<Living>> livingsToAdd, AtomicInteger round, AtomicInteger lineIndex) {
        Optional<Integer> count = IntegerUtils.of(matcherKelp.group(1));
        Optional<Integer> age = IntegerUtils.of(matcherKelp.group(2));
        if(count.isEmpty())
            throw new AquariumReadException("Erreur de lecture du nombres d'algues à la ligne "+lineIndex.get());
        if(age.isEmpty()) throw new AquariumReadException("Erreur de lecture de l'âge à la ligne "+lineIndex.get());
        if(round.get() == -1) IntStream.range(0, count.get()).forEach(i -> kelps.add(new KelpBasic(age.get())));
        else
            livingsToAdd.computeIfAbsent(round.get(), k -> new ArrayList<>()).addAll(IntStream.range(0, count.get()).mapToObj(i -> new KelpBasic(age.get())).toList());
    }

    private void parseFishAdd(Matcher matcherFish, List<Fish> fishes, Map<Integer, List<Living>> livingsToAdd, AtomicInteger round, AtomicInteger lineIndex) {
        String name = matcherFish.group(1);
        if(nullOrEmpty(name))
            throw new AquariumReadException("Erreur de lecture du nom du poisson à la ligne "+lineIndex.get());

        Optional<Fish.Species> species = Optional.of(matcherFish.group(2)).filter(sp -> !nullOrEmpty(sp)).flatMap(Fish.Species::of);
        if(species.isEmpty())
            throw new AquariumReadException("Erreur de lecture de l'espèce du poisson à la ligne "+lineIndex.get());

        Optional<Fish.Sex> sex = Fish.Sex.of(matcherFish.group(3));
        if(sex.isEmpty()) throw new AquariumReadException("Erreur de lecture du sexe à la ligne "+lineIndex.get());

        int age = IntegerUtils.of(matcherFish.group(5)).orElse(0);

        int pv = IntegerUtils.of(matcherFish.group(7)).orElse(Fish.DEFAULT_PV);

        Fish fish = Fish.reInstantiateFish(species.get(), name, sex.get(), age, pv);
        if(round.get() == -1) fishes.add(fish);
        else livingsToAdd.computeIfAbsent(round.get(), k -> new ArrayList<>()).add(fish);
    }

    private void parseRound(Matcher matcherRound, AtomicInteger round, AtomicInteger lineIndex) {
        Optional<Integer> roundTempOptional = IntegerUtils.of(matcherRound.group(1));
        if(roundTempOptional.isEmpty())
            throw new AquariumReadException("Erreur de lecture du numéro de tour à la ligne "+lineIndex.get());
        round.set(roundTempOptional.get());
    }


}
