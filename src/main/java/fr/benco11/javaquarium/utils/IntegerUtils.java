package fr.benco11.javaquarium.utils;

import java.util.Optional;

/**
 * Classe utilitaire d'<code>Integer</code>
 */
public final class IntegerUtils {
    /**
     * Donne un <code>Optional</code> contenant ou non un <code>Integer</code> à partir d'un <code>String</code>
     *
     * @param s <code>String</code> contenant ou non un nombre
     * @return l'<code>Optional</code> d'<code>Integer</code>
     */
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
