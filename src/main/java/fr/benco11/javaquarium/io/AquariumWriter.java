package fr.benco11.javaquarium.io;

import fr.benco11.javaquarium.Aquarium;
import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;
import fr.benco11.javaquarium.options.Options;
import fr.benco11.javaquarium.utils.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

import static fr.benco11.javaquarium.options.Options.LivingOption.AGE;
import static fr.benco11.javaquarium.options.Options.LivingOption.AMOUNT;
import static fr.benco11.javaquarium.utils.StringUtils.pluralInsert;

public class AquariumWriter extends BufferedWriter {
    private static final String KELPS_HELP = """
            // Algues
            // Format : [Nombre d'algues] algues [âge] ans (= tours)
            """;

    private static final String FISHES_HELP = """
            // Poissons
            // Format : [nom], [race], [sexe], [âge] ans (= tours)
            """;

    public AquariumWriter(Writer out) {
        super(out);
    }

    public void writeAquarium(Aquarium aquarium) throws IOException {
        write(KELPS_HELP);
        write("\n");
        writeKelps(aquarium.kelps());

        write("\n");
        write(FISHES_HELP);
        write("\n");
        writeFishes(aquarium.fishes());

        Set<Integer> rounds = new HashSet<>(aquarium.remainingLivingsToAdd().keySet());
        rounds.addAll(aquarium.remainingRemoveOptions().keySet());
        for(int round : rounds) {
            write("===== Tour "+round+" =====\n");

            List<Living> toAdd = aquarium.remainingLivingsToAdd().getOrDefault(round, new ArrayList<>());
            writeFishes(toAdd.stream().filter(Fish.class::isInstance).map(Fish.class::cast).toList());
            writeKelps(toAdd.stream().filter(Kelp.class::isInstance).map(Kelp.class::cast).toList());

            Pair<List<Options>, List<Options>> removeOptions = aquarium.remainingRemoveOptions().getOrDefault(round, new Pair<>(new ArrayList<>(), new ArrayList<>()));
            writeRemoveKelps(removeOptions.first());
            writeRemoveFishes(removeOptions.second());
        }
    }

    public void writeFishes(List<Fish> fishes) throws IOException {
        for(Fish fish : fishes)
            write(fish.name()+", "+Fish.species(fish)+", "+fish.sex().toString()+", "+pluralInsert("an", fish.age())+", "+pluralInsert("pv", fish.pv())+"\n");
    }

    public void writeKelps(List<Kelp> kelps) throws IOException {
        for(Map.Entry<Integer, Long> kelpsByAge : kelps.stream().collect(Collectors.groupingBy(Kelp::age, Collectors.counting())).entrySet())
            write(pluralInsert("algue", kelpsByAge.getValue())+" "+pluralInsert("an", kelpsByAge.getKey())+"\n");
    }

    public void writeRemoveFishes(List<Options> options) throws IOException {
        for(Options option : options)
            write("-poisson "+option.optionsMap().entrySet().stream().filter(e -> e.getValue().isPresent())
                    .map(e -> e.getKey()+":"+e.getValue().get()).collect(Collectors.joining(", "))+"\n");
    }

    public void writeRemoveKelps(List<Options> options) throws IOException {
        for(Options option : options)
            write("-"+ifPresentPluralOr(option, AMOUNT, "algue", "", null)+
                    ifPresentPluralOr(option, AGE, "an", " ", "")+"\n");
    }

    private String ifPresentPluralOr(Options option, Options.StandardOption id, String word, String prefix, String or) {
        return option.ifPresentOr(id, v -> prefix+pluralInsert(word, v), Integer.class, (or == null) ? word : or);
    }
}
