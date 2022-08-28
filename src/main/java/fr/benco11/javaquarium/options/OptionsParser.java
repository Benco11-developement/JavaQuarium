package fr.benco11.javaquarium.options;

import fr.benco11.javaquarium.utils.IntegerUtils;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OptionsParser {
    public static final Map<String, Boolean> OPTIONS_PARSER_ARGUMENTS_LIST = Map.of(
            Options.INPUT_OPTION, true,
            Options.OUTPUT_OPTION, true,
            Options.OUTPUT_ROUND_OPTION, true,
            Options.ROUNDS_OPTION, true
    );

    private static final Pattern OPTIONS_ID_REGEX = Pattern.compile("-([a-zA-Z]{1,3})");

    public Options parse(String... args) {
            Options options = new Options();
            for(int i = 0; i < args.length; i++) {

                    Matcher matcher = OPTIONS_ID_REGEX.matcher(args[i]);
                    if(!matcher.find()) continue;
                    String id = matcher.group(1);
                    if(!OPTIONS_PARSER_ARGUMENTS_LIST.containsKey(id)) throw new OptionParseException(id);
                    Optional<?> value = (i+1 >= args.length) ? Optional.of(true) : IntegerUtils.of(args[i+1]);
                    if(value.isEmpty()) value = Optional.of(args[i+1]);
                    if(value.get().equals(true) && OPTIONS_PARSER_ARGUMENTS_LIST.get(id)) throw new OptionParseException(id);
                    
                    options.add(id, value);
            }
            return options;
    }
}
