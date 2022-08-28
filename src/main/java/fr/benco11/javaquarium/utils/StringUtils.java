package fr.benco11.javaquarium.utils;

import fr.benco11.javaquarium.living.fish.Fish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StringUtils {
    private static final List<String> NAMES = new ArrayList<>();

    static {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(StringUtils.class.getResourceAsStream("/gutenberg.txt"))))) {
            NAMES.addAll(reader.lines().toList());
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String getFishName(Fish parent) {
        return NAMES.get(Math.abs(parent.name().hashCode()%NAMES.size()));
    }

    public static String plural(String word, long nb) {
        return nb > 1 ? word+"s" : word;
    }

    public static String pluralInsert(String word, long nb) {
        return nb+" "+plural(word, nb);
    }

    public static String indefiniteArticle(Fish.Sex sex) {
        return sex == Fish.Sex.MALE ? "un" : "une";
    }

    public static String indefiniteArticleAppend(String word, Fish.Sex sex) {
        return indefiniteArticle(sex)+" "+word;
    }

    public static String sex(String word, Fish.Sex sex) {
        return word+((sex == Fish.Sex.MALE) ? "" : "e");
    }

    public static boolean nullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private StringUtils() {
    }
}
