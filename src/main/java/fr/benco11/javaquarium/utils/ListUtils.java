package fr.benco11.javaquarium.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ListUtils {
    public static <T> List<T> pickRandoms(List<T> list, int n) {
        List<T> copy = new ArrayList<>(list);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    private ListUtils() {
    }
}
