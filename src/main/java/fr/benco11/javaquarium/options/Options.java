package fr.benco11.javaquarium.options;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class Options {
        public static final String SEX_OPTION = "sx";
        public static final String NAME_OPTION = "n";
        public static final String SPECIES_OPTION = "sp";
        public static final String AGE_OPTION = "a";
        public static final String AMOUNT_OPTION = "amount";
        public static final String INPUT_OPTION = "i";
        public static final String OUTPUT_OPTION = "o";
        public static final String ROUNDS_OPTION = "r";
        public static final String OUTPUT_ROUND_OPTION = "oR";

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

        public Options add(String id, Optional<?> option) {
                optionsMap.put(id, option);
                return this;
        }

        public Optional<?> option(String id) {
                return optionsMap.getOrDefault(id, Optional.empty());
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
                return optionsMap.containsKey(id);
        }
}
