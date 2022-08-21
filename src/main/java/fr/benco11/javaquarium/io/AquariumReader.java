package fr.benco11.javaquarium.io;

import fr.benco11.javaquarium.Aquarium;
import fr.benco11.javaquarium.JavaQuarium;
import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;
import fr.benco11.javaquarium.living.kelp.KelpBasic;
import fr.benco11.javaquarium.utils.IntegerUtils;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class AquariumReader extends BufferedReader {
    private static final Pattern PATTERN_KELP = Pattern.compile("(\\d+) algue(s)? (\\d+) an(s)?");
    private static final Pattern PATTERN_FISH = Pattern.compile("(\\D+), (\\D+), (\\w+), (\\d+) an(s)?");

    private static final Pattern PATTERN_ROUND = Pattern.compile("===== Tour (\\d+) =====");

    public AquariumReader(Reader in) {
        super(in);
    }

    public Aquarium readAquarium() {
        List<String> lines = lines().toList();

        List<Kelp> kelps = new ArrayList<>();
        List<Fish> fishes = new ArrayList<>();

        Map<Integer, List<Living>> livingsToAdd = new HashMap<>();

        AtomicInteger round = new AtomicInteger(-1);
        AtomicInteger lineIndex = new AtomicInteger(0);

        lines.forEach(line -> {
            lineIndex.addAndGet(1);
            if(line.startsWith("//") || line.equals("")) return;

            Matcher matcherKelp = PATTERN_KELP.matcher(line);
            Matcher matcherFish = PATTERN_FISH.matcher(line);
            Matcher matcherRound = PATTERN_ROUND.matcher(line);

            if(matcherKelp.find()) {
                matchKelp(matcherKelp, kelps, livingsToAdd, round, lineIndex);
                return;
            }


            if(matcherFish.find()) {
                matchFish(matcherFish, fishes, livingsToAdd, round, lineIndex);
                return;
            }

            if(matcherRound.find()) {
                matchRound(matcherRound, round, lineIndex);
                return;
            }
            throw new AquariumReadException("Erreur de lecture à la ligne "+lineIndex.get());
        });

        return new JavaQuarium(kelps, fishes, livingsToAdd);
    }

    private void matchKelp(Matcher matcherKelp, List<Kelp> kelps, Map<Integer, List<Living>> livingsToAdd, AtomicInteger round, AtomicInteger lineIndex) {
        Optional<Integer> count = IntegerUtils.of(matcherKelp.group(1));
        Optional<Integer> age = IntegerUtils.of(matcherKelp.group(3));
        if(count.isEmpty())
            throw new AquariumReadException("Erreur de lecture du nombres d'algues à la ligne "+lineIndex.get());
        if(age.isEmpty())
            throw new AquariumReadException("Erreur de lecture de l'âge à la ligne "+lineIndex.get());
        if(round.get() == -1)
            IntStream.range(0, count.get()).forEach(i -> kelps.add(new KelpBasic(age.get())));
        else
            livingsToAdd.computeIfAbsent(round.get(), k -> new ArrayList<>()).addAll(IntStream.range(0, count.get()).mapToObj(i -> new KelpBasic(age.get())).toList());
    }

    private void matchFish(Matcher matcherFish, List<Fish> fishes, Map<Integer, List<Living>> livingsToAdd, AtomicInteger round, AtomicInteger lineIndex) {
        String name = matcherFish.group(1);
        String species = matcherFish.group(2);
        Optional<Fish.Sex> sex = Fish.Sex.of(matcherFish.group(3));
        if(sex.isEmpty())
            throw new AquariumReadException("Erreur de lecture du sexe à la ligne "+lineIndex.get());

        Optional<Integer> age = IntegerUtils.of(matcherFish.group(4));
        if(age.isEmpty())
            throw new AquariumReadException("Erreur de lecture de l'âge à la ligne "+lineIndex.get());

        Fish fish = Fish.reInstantiateFish(species, name, sex.get(), age.get(), new AquariumReadException("Erreur de lecture de l'espèce '"+species+"' à la ligne "+lineIndex.get()));
        if(round.get() == -1)
            fishes.add(fish);
        else
            livingsToAdd.computeIfAbsent(round.get(), k -> new ArrayList<>()).add(fish);
    }

    private void matchRound(Matcher matcherRound, AtomicInteger round, AtomicInteger lineIndex) {
        Optional<Integer> roundTempOptional = IntegerUtils.of(matcherRound.group(1));
        if(roundTempOptional.isEmpty())
            throw new AquariumReadException("Erreur de lecture du numéro de tour à la ligne "+lineIndex.get());
        round.set(roundTempOptional.get());
    }
}
