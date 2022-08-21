package fr.benco11.javaquarium.utils;

import java.util.Optional;

public class IntegerUtils {
    private IntegerUtils() {
    }

    public static Optional<Integer> of(String s) {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch(NumberFormatException e) {
            return Optional.empty();
        }
    }
}
