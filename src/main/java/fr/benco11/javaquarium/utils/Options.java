package fr.benco11.javaquarium.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Options {
    public static <T> boolean filter(T element, Optional<T> filter) {
        return element.equals(filter.orElse(element));
    }

    private final List<Optional<Object>> options;

    public Options() {
        this(new ArrayList<>());
    }

    public Options(List<Optional<Object>> options) {
        this.options = options;
    }

    public Options(Object... options) {
        this(Arrays.stream(options).map(Optional::ofNullable).collect(Collectors.toList()));
    }

    @SafeVarargs
    public Options(Optional<Object>... options) {
        this(new ArrayList<>(List.of(options)));
    }

    public Options add(Optional<Object> option) {
        options.add(option);
        return this;
    }

    public Optional<Object> get(int index) {
        return options.size() >= index || options.get(index) == null ? Optional.empty() : options.get(index);
    }

    public <T> Optional<T> get(int index, Class<T> clazz) {
        Optional<Object> optional = get(index);
        return (optional.isEmpty() || !(optional.get().getClass().isAssignableFrom(clazz))) ? Optional.empty() : optional.map(clazz::cast);
    }
}
