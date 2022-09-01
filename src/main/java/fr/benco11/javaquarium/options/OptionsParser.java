package fr.benco11.javaquarium.options;

import fr.benco11.javaquarium.utils.IntegerUtils;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.benco11.javaquarium.options.Options.AquariumOption.*;

/**
 * Un <code>OptionsParser</code> permet de parse les arguments de programme
 */
public class OptionsParser {
    /**
     * <code>Map</code> des arguments de programme et si l'argument nécessite une valeur
     */
    public static final Map<String, Boolean> OPTIONS_PARSER_ARGUMENTS_LIST = Map.of(
            INPUT.id(), true,
            OUTPUT.id(), true,
            OUTPUT_ROUND.id(), true,
            ROUNDS.id(), true);

    /**
     * Regex pour détecter les ids d'arguments
     */
    private static final Pattern OPTIONS_ID_REGEX = Pattern.compile("-([a-zA-Z]{1,3})");

    /**
     * Parser un tableau d'arguments en un objet <code>Options</code>
     *
     * @param args arguments
     * @return options
     */
    public Options parse(String... args) {
        Options options = new Options();
        for(int i = 0; i < args.length; i++) {

            Matcher matcher = OPTIONS_ID_REGEX.matcher(args[i]);
            if(!matcher.find()) continue;
            String id = matcher.group(1);
            if(!OPTIONS_PARSER_ARGUMENTS_LIST.containsKey(id)) throw new OptionParseException(id);
            Optional<?> value = (i + 1 >= args.length)
                                ? Optional.of(true)
                                : IntegerUtils.of(args[i + 1]);
            if(value.isEmpty()) value = Optional.of(args[i + 1]);

            // Lève une exception si l'argument nécessite une valeur qui n'a pas été trouvée
            if(value.get()
                    .equals(true) && Boolean.TRUE.equals(OPTIONS_PARSER_ARGUMENTS_LIST.get(id)))
                throw new OptionParseException(id);

            options.add(id, value);
        }
        return options;
    }
}
