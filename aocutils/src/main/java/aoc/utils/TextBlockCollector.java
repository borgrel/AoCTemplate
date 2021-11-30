package aoc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

class TextBlockCollector implements Collector<String, List<List<String>>, Stream<List<String>>> {
        private static final Set<Characteristics> NO_CHARACTERISTICS = Set.of();
        @Override
        public Supplier<List<List<String>>> supplier() {
            //need to wrap in new ArrayList because List.of() is immutable
            return () -> new ArrayList<>(List.of(new ArrayList<>()));
        }
        @Override
        public BiConsumer<List<List<String>>, String> accumulator() {
            return (list, string) -> {
                if (string.isEmpty()) list.add(new ArrayList<>());
                else list.get(list.size()-1).add(string);
            };
        }
        //this should never be called because Characteristics.CONCURRENT is not set
        @Override
        public BinaryOperator<List<List<String>>> combiner() {
            return null;
        }
        @Override
        public Function<List<List<String>>, Stream<List<String>>> finisher() {
            return List::stream;
        }
        @Override
        public Set<Characteristics> characteristics() {
            return NO_CHARACTERISTICS;
        }
}
