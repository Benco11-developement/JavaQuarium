package fr.benco11.javaquarium.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe utilitaire de listes
 */
public final class ListUtils {
    /**
     * Donne une nouvelle liste de taille n composée d'éléments pris au hasard d'une liste
     *
     * @param list liste d'entrée
     * @param n    taille de la nouvelle liste
     * @param <T>  type de la liste
     * @return la nouvelle liste
     */
    public static <T> List<T> pickRandoms(List<T> list, int n) {
        List<T> copy = new ArrayList<>(list);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    private ListUtils() {
    }
}
