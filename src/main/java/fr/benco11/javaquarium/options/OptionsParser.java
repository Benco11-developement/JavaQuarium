package fr.benco11.javaquarium.options;

import fr.benco11.javaquarium.utils.IntegerUtils;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OptionsParser {
    public static final Map<String, Boolean> OPTIONS_ARGUMENTS_LIST = Map.of(
            "i", true,
            "o", true,
            "oR", true,
            "r", true
    );

    private static final Pattern OPTIONS_REGEX = Pattern.compile("-([a-zA-Z]{1,3})\\s+\"((\\\\\"|[^\"])+)\"");

    public Options parse(String... args) {
            Options options = new Options();
            String optionString = String.join(" ", args);
            Matcher matcher = OPTIONS_REGEX.matcher(optionString);
            while(matcher.find()) {
                    String optionId = matcher.group(1);
                    if(!OPTIONS_ARGUMENTS_LIST.containsKey(optionId)) throw new OptionParseException(optionId);
                    Optional<?> value = Optional.of(true);
                    if(matcher.groupCount() > 1) {
                            Optional<Integer> intValue;
                            value = (intValue = IntegerUtils.of(matcher.group(2))).isPresent() ? intValue : Optional.of(matcher.group(2));
                    }
                    options.add(optionId, value);
            }
            return options;
    }
}
