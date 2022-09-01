package fr.benco11.javaquarium.utils;

import fr.benco11.javaquarium.living.fish.Fish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe utilitaire de <code>String</code>
 */
public final class StringUtils {
    /**
     * Liste des noms de poissons
     */
    private static final List<String> NAMES = new ArrayList<>();

    static {
        // Récupère la liste des noms de poissons depuis le fichier ressource gutenberg.txt contenant tous les mots de la langue française
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(StringUtils.class.getResourceAsStream("/gutenberg.txt"))))) {
            NAMES.addAll(reader.lines()
                               .toList());
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Calcule un nom de poisson depuis un parent
     *
     * @param parent parent du poisson
     * @return le nom
     */
    public static String getFishName(Fish parent) {
        return NAMES.get(Math.abs(parent.name()
                                        .hashCode()%NAMES.size()));
    }

    /**
     * Met au pluriel un mot ou non
     *
     * @param word   le mot
     * @param number le quantificateur du mot
     * @return le mot au pluriel ou non
     */
    public static String plural(String word, long number) {
        return number > 1 ? word + "s" : word;
    }

    /**
     * Met au pluriel un mot et insère le quantificateur au début ou non
     *
     * @param word   le mot
     * @param number le quantificateur du mot
     * @return le quantificateur et le mot au pluriel ou non
     */
    public static String pluralInsert(String word, long number) {
        return number + " " + plural(word, number);
    }

    /**
     * Donne un article indéfini selon un sexe
     *
     * @param sex le sexe
     * @return l'article indéfini
     */
    public static String indefiniteArticle(Fish.Sex sex) {
        return sex == Fish.Sex.MALE ? "un" : "une";
    }

    /**
     * Insère un article indéfini à un mot selon un sexe
     *
     * @param word le mot
     * @param sex  le sexe
     * @return l'article indéfini avec le mot
     */
    public static String indefiniteArticleAppend(String word, Fish.Sex sex) {
        return indefiniteArticle(sex) + " " + word;
    }

    /**
     * Accorde en genre un mot
     *
     * @param word le mot
     * @param sex  le sexe
     * @return le mot accordé
     */
    public static String sex(String word, Fish.Sex sex) {
        return word + ((sex == Fish.Sex.MALE) ? "" : "e");
    }

    /**
     * Renvoie vrai si un <code>String</code> est vide ou <code>null</code>
     *
     * @param string le <code>String</code>
     * @return si <code>string</code> est vide ou <code>null</code>
     */
    public static boolean nullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private StringUtils() {
    }
}
