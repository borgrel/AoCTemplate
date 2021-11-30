package aoc.utils;

import java.util.Arrays;
import java.util.List;

public class Utils {
    public static TextBlockCollector toTextBlock() {
        return new TextBlockCollector();
    }

    // ----------++- CONSOLE UTILITIES -++---------------------------------------
    public static <T> void printArray(T[][] input) {
        Arrays.stream(input)
                .map(Arrays::toString)
                .forEach(System.out::println);
    }
    public static <T> void printArray(T[] input) {
        Arrays.stream(input)
                .forEach(System.out::println);
    }
    public static <T> void printArray(List<T[]> input) {
        input.stream()
                .map(Arrays::toString)
                .forEach(System.out::println);
    }
}
