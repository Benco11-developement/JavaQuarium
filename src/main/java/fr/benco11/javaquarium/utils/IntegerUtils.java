package fr.benco11.javaquarium.utils;

import java.util.Optional;

public final class IntegerUtils {
    public static Optional<Integer> of(String s) {
        if(s == null) return Optional.empty();
        try {
            return Optional.of(Integer.parseInt(s));
        } catch(NumberFormatException e) {
            return Optional.empty();
        }
    }

    private IntegerUtils() {
    }
}
