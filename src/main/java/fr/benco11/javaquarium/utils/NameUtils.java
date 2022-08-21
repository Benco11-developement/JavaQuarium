package fr.benco11.javaquarium.utils;

import fr.benco11.javaquarium.living.fish.Fish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NameUtils {
    private static final List<String> NAMES = new ArrayList<>();

    static {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(NameUtils.class.getResourceAsStream("/gutenberg.txt"))))) {
            NAMES.addAll(reader.lines().toList());
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String getName(Fish parent) {
        return NAMES.get(Math.abs(parent.name().hashCode()%NAMES.size()));
    }

    private NameUtils() {
    }
}
