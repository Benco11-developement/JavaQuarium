package fr.benco11.javaquarium.io;

import fr.benco11.javaquarium.Aquarium;
import fr.benco11.javaquarium.living.Living;
import fr.benco11.javaquarium.living.fish.Fish;
import fr.benco11.javaquarium.living.kelp.Kelp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        for(Map.Entry<Integer, List<Living>> toAdd : aquarium.remainingLivingsToAdd().entrySet()) {
            write("===== Tours "+toAdd.getKey()+" =====\n");
            writeFishes(toAdd.getValue().stream().filter(Fish.class::isInstance).map(Fish.class::cast).toList());
            writeKelps(toAdd.getValue().stream().filter(Kelp.class::isInstance).map(Kelp.class::cast).toList());
        }
    }

    public void writeFishes(List<Fish> fishes) throws IOException {
        for(Fish fish : fishes)
            write(fish.name()+", "+Fish.species(fish)+", "+fish.sex().toString()+", "+fish.age()+" ans\n");
    }

    public void writeKelps(List<Kelp> kelps) throws IOException {
        for(Map.Entry<Integer, Long> kelpsByAge : kelps.stream().collect(Collectors.groupingBy(Kelp::age, Collectors.counting())).entrySet())
            write(kelpsByAge.getValue()+" algues "+kelpsByAge.getKey()+" ans\n");
    }
}
