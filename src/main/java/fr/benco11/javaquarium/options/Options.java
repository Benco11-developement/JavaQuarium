package fr.benco11.javaquarium.options;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class Options {
    public static <T> boolean equalsOrNull(T element, Optional<?> filter) {
        return element.equals(filter.isEmpty() ? element : filter.get());
    }

    private final Map<String, Optional<?>> options;

    public Options() {
        this(new HashMap<>());
    }


    public Options(Map<String, Optional<?>> options) {
            this.options = options;
    }

    public Options add(String id, Optional<?> option) {
        options.put(id, option);
        return this;
    }

    public Optional<?> option(String id) {
        return options.getOrDefault(id, Optional.empty());
    }

    public <T> Optional<T> option(String id, Class<T> clazz) {
        Optional<?> optional = option(id);
        return (optional.isEmpty() || !(clazz.isInstance(optional.get()))) ? Optional.empty() : optional.map(clazz::cast);
    }

    public <K, T> T ifPresentOr(String id, Function<K, T> map, Class<K> clazz, T orElse) {
            Optional<K> k = option(id, clazz);
            return k.isEmpty() ? orElse : map.apply(k.get()); 
    }

    public boolean isPresent(String id) {
            return options.containsKey(id);
    }
}
