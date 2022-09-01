package fr.benco11.javaquarium.options;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Un <code>Options</code> représente une <code>Map</code> d'options avec en clé un <code>String</code> et en valeur un <code>Optional</code>
 */
public class Options {
    /**
     * Renvoie vrai si un objet correspond à un filtre <code>Optional</code> ou si l'<code>Optional</code> est vide
     *
     * @param element objet
     * @param filter  filtre
     * @param <T>     type de l'objet
     * @return si l'objet correspond à <code>filter</code> ou si <code>filter</code> est vide
     */
    public static <T> boolean equalsOrEmpty(T element, Optional<?> filter) {
        return element.equals(filter.isEmpty() ? element : filter.get());
    }

    private final Map<String, Optional<?>> optionsMap;

    /**
     * Constructeur par défaut créant une <code>Map</code> vide
     */
    public Options() {
        this(new HashMap<>());
    }

    /**
     * Construit à partir d'une <code>Map</code> d'options
     *
     * @param optionsMap <code>Map</code> d'options avec comme clé l'id de l'option et comme valeur un <code>Optional</code>
     */
    public Options(Map<String, Optional<?>> optionsMap) {
        this.optionsMap = optionsMap;
    }

    /**
     * Ajoute une option à la liste des options
     *
     * @param id     id de l'option
     * @param option valeur de l'option
     * @return l'<code>Options</code>
     */
    public Options add(StandardOption id, Optional<?> option) {
        return add(id.id(), option);
    }

    /**
     * Ajoute une option à la liste des options
     *
     * @param id     de l'option
     * @param option valeur de l'option
     * @return l'<code>Options</code>
     */
    public Options add(String id, Optional<?> option) {
        optionsMap.put(id, option);
        return this;
    }

    /**
     * Récupère une option
     *
     * @param id id de l'option
     * @return l'<code>Optional</code> de l'option (vide si l'option n'existe pas)
     */
    public Optional<?> option(String id) {
        return optionsMap.getOrDefault(id, Optional.empty());
    }

    /**
     * Récupère une option
     *
     * @param id id de l'option
     * @return l'<code>Optional</code> de l'option (vide si l'option n'existe pas)
     */
    public Optional<?> option(StandardOption id) {
        return option(id.id());
    }

    /**
     * Récupère une option id et la cast
     *
     * @param id    id de l'option
     * @param clazz classe de l'option
     * @param <T>   type que représente <code>clazz</code>
     * @return l'<code>Optional</code> de l'option cast en <code>clazz</code> (vide si l'option n'existe pas)
     */
    public <T> Optional<T> option(String id, Class<T> clazz) {
        Optional<?> optional = option(id);
        return (optional.isEmpty() || !(clazz.isInstance(optional.get())))
               ? Optional.empty()
               : optional.map(clazz::cast);
    }

    /**
     * Récupère une option et la cast ou renvoie une autre valeur si l'option n'existe pas
     *
     * @param id     id de l'option
     * @param map    <code>Function</code> qui à partir de la valeur de l'option renvoie une valeur du type <code>T</code>
     * @param clazz  classe de la valeur de l'option
     * @param orElse valeur à renvoyer si l'option n'existe pas
     * @param <K>    type de la valeur de l'option
     * @param <T>    type de <code>orElse</code> et du résultat de <code>map</code>
     * @return le résultat de <code>map</code> ou <code>orElse</code>
     */
    public <K, T> T ifPresentOr(String id, Function<K, T> map, Class<K> clazz, T orElse) {
        Optional<K> k = option(id, clazz);
        return k.isEmpty() ? orElse : map.apply(k.get());
    }

    /**
     * Récupère une option et la cast
     *
     * @param id    id de l'option
     * @param clazz classe de l'option
     * @param <T>   type que représente <code>clazz</code>
     * @return l'<code>Optional</code> de l'option cast en <code>clazz</code> (vide si l'option n'existe pas)
     */
    public <T> Optional<T> option(StandardOption id, Class<T> clazz) {
        return option(id.id(), clazz);
    }

    /**
     * Récupère une option et la cast ou renvoie une autre valeur si l'option n'existe pas
     *
     * @param id     id de l'option
     * @param map    <code>Function</code> qui à partir de la valeur de l'option renvoie une valeur du type <code>T</code>
     * @param clazz  classe de la valeur de l'option
     * @param orElse valeur à renvoyer si l'option n'existe pas
     * @param <K>    type de la valeur de l'option
     * @param <T>    type de <code>orElse</code> et du résultat de <code>map</code>
     * @return le résultat de <code>map</code> ou <code>orElse</code>
     */
    public <K, T> T ifPresentOr(StandardOption id, Function<K, T> map, Class<K> clazz, T orElse) {
        return ifPresentOr(id.id(), map, clazz, orElse);
    }

    /**
     * Renvoie vrai si l'option est présente
     *
     * @param id id de l'option
     * @return si l'option est présente
     */
    public boolean isPresent(String id) {
        return optionsMap.containsKey(id);
    }

    /**
     * Renvoie vrai si l'option est présente
     *
     * @param id id de l'option
     * @return si l'option est présente
     */
    public boolean isPresent(StandardOption id) {
        return isPresent(id.id());
    }

    /**
     * Renvoie les options
     *
     * @return une <code>Map</code> avec pour clé l'id de l'option et en valeur un <code>Optional</code> de l'option
     */
    public Map<String, Optional<?>> optionsMap() {
        return optionsMap;
    }

    /**
     * Énumération des options concernant les êtres vivants
     */
    public enum LivingOption implements StandardOption {
        /**
         * Sexe
         */
        SEX("sx"),
        /**
         * Nom
         */
        NAME("n"),
        /**
         * Espèce
         */
        SPECIES("sp"),
        /**
         * Âge
         */
        AGE("a"),
        /**
         * Quantité
         */
        AMOUNT("amount"),
        /**
         * Nombre de pvs
         */
        PV("pv");

        /**
         * Récupère une <code>LivingOption</code>
         *
         * @param id id de l'option
         * @return un <code>Optional</code> contenant ou non une <code>LivingOption</code>
         */
        public static Optional<LivingOption> of(String id) {
            return Arrays.stream(values())
                         .filter(o -> o.id()
                                       .equals(id))
                         .findAny();
        }

        private final String id;

        LivingOption(String id) {
            this.id = id;
        }

        @Override
        public String id() {
            return id;
        }
    }

    /**
     * Énumération des options concernant les arguments de l'aquarium
     */
    public enum AquariumOption implements StandardOption {
        /**
         * Fichier d'entrée
         */
        INPUT("i"),
        /**
         * Fichier de sortie
         */
        OUTPUT("o"),
        /**
         * Nombre de tours
         */
        ROUNDS("r"),
        /**
         * Tour de sauvegarde
         */
        OUTPUT_ROUND("oR");

        /**
         * Récupère une <code>AquariumOption</code>
         *
         * @param id id de l'option
         * @return un <code>Optional</code> contenant ou non une <code>AquariumOption</code>
         */
        public static Optional<AquariumOption> of(String id) {
            return Arrays.stream(values())
                         .filter(o -> o.id()
                                       .equals(id))
                         .findAny();
        }

        private final String id;

        AquariumOption(String id) {
            this.id = id;
        }

        @Override
        public String id() {
            return id;
        }
    }

    /**
     * Option standard
     */
    public sealed interface StandardOption permits LivingOption, AquariumOption {
        /**
         * Renvoie l'id de l'option
         *
         * @return id de l'option
         */
        String id();
    }
}
