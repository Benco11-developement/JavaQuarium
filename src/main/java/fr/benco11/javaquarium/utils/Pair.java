package fr.benco11.javaquarium.utils;

/**
 * Tuple de niveau 2
 *
 * @param <A> Type de la première valeur
 * @param <B> Type de la seconde valeur
 */
public final class Pair<A, B> {
    private A a;
    private B b;

    /**
     * Initialise une paire à partir de deux valeurs
     *
     * @param a première valeur de type <code>A</code>
     * @param b seconde valeur de type <code>B</code>
     */
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    /**
     * @return la première valeur
     */
    public A first() {
        return a;
    }

    /**
     * @return la deuxième valeur
     */
    public B second() {
        return b;
    }

    /**
     * Modifie la première valeur
     *
     * @param first nouvelle première valeur
     * @return la paire
     */
    public Pair<A, B> setFirst(A first) {
        this.a = first;
        return this;
    }

    /**
     * Modifie la deuxième valeur
     *
     * @param second nouvelle deuxième valeur
     * @return la paire
     */
    public Pair<A, B> setSecond(B second) {
        this.b = second;
        return this;
    }
}
