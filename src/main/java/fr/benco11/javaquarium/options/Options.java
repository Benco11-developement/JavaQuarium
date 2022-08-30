package fr.benco11.javaquarium.options;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Options {
    public static <T> boolean equalsOrEmpty(T element, Optional<?> filter) {
        return element.equals(filter.isEmpty() ? element : filter.get());
    }

    private final Map<String, Optional<?>> optionsMap;

    public Options() {
        this(new HashMap<>());
    }

    public Options(Map<String, Optional<?>> optionsMap) {
        this.optionsMap = optionsMap;
    }

    public Options add(StandardOption id, Optional<?> option) {
        return add(id.id(), option);
    }

    public Options add(String id, Optional<?> option) {
        optionsMap.put(id, option);
        return this;
    }

    public Optional<?> option(String id) {
        return optionsMap.getOrDefault(id, Optional.empty());
    }

    public Optional<?> option(StandardOption id) {
        return option(id.id());
    }

    public <T> Optional<T> option(String id, Class<T> clazz) {
        Optional<?> optional = option(id);
        return (optional.isEmpty() || !(clazz.isInstance(optional.get()))) ? Optional.empty() : optional.map(clazz::cast);
    }

    public <K, T> T ifPresentOr(String id, Function<K, T> map, Class<K> clazz, T orElse) {
        Optional<K> k = option(id, clazz);
        return k.isEmpty() ? orElse : map.apply(k.get());
    }

    public <T> Optional<T> option(StandardOption id, Class<T> clazz) {
        return option(id.id(), clazz);
    }

    public <K, T> T ifPresentOr(StandardOption id, Function<K, T> map, Class<K> clazz, T orElse) {
        return ifPresentOr(id.id(), map, clazz, orElse);
    }

    public boolean isPresent(String id) {
        return optionsMap.containsKey(id);
    }

    public boolean isPresent(StandardOption id) {
        return isPresent(id.id());
    }

    public Map<String, Optional<?>> optionsMap() {
        return optionsMap;
    }

    public enum LivingOption implements StandardOption {
        SEX("sx"), NAME("n"), SPECIES("sp"), AGE("a"), AMOUNT("amount"), PV("pv");

        public static Optional<LivingOption> of(String id) {
            return Arrays.stream(values()).filter(o -> o.id().equals(id)).findAny();
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

    public enum AquariumOption implements StandardOption {
        INPUT("i"), OUTPUT("o"), ROUNDS("r"), OUTPUT_ROUND("oR");

        public static Optional<AquariumOption> of(String id) {
            return Arrays.stream(values()).filter(o -> o.id().equals(id)).findAny();
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

    public sealed interface StandardOption permits LivingOption, AquariumOption {
        String id();
    }
}
