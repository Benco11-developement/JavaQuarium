package fr.benco11.javaquarium.options;

import fr.benco11.javaquarium.utils.IntegerUtils;

import java.util.HashMap;
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

    private static final Pattern OPTIONS_REGEX = Pattern.compile("-([a-zA-Z]{1,3})( '(([^']|\\\\')*[^\\\\])')?");

    private final Map<String, Object> optionsMap;

    public OptionsParser(String... args) {
        optionsMap = new HashMap<>();
        String optionString = String.join(" ", args);
        Matcher matcher = OPTIONS_REGEX.matcher(optionString);
        while(matcher.find()) {
            String optionId = matcher.group(1);
            if(!OPTIONS_ARGUMENTS_LIST.containsKey(optionId)) throw new OptionParseException(optionId);
            Object value = true;
            if(matcher.groupCount() > 2) {
                Optional<Integer> intValue = IntegerUtils.of(matcher.group(3));
                value = (intValue.isPresent()) ? intValue.get() : matcher.group(3);
            }
            optionsMap.put(optionId, value);
        }
    }

    public Optional<Object> option(String optionId) {
        return Optional.ofNullable(optionsMap.get(optionId));
    }

    public <T> T option(String optionId, Class<T> clazz, RuntimeException toThrow) {
        Optional<Object> option = option(optionId);
        if(option.isEmpty() || !option.get().getClass().equals(clazz)) throw toThrow;
        return clazz.cast(option.get());
    }

    public boolean isPresent(String optionId) {
        return optionsMap.containsKey(optionId);
    }
}
