package aoc.runner;

import aoc.utils.Config;
import aoc.utils.Day;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayRunner {
    static final Logger logger = LoggerFactory.getLogger(DayRunner.class);

    static final String INCOMPLETE = "No solution yet";

    // ----------++- FILE UTILITIES -++---------------------------------------
    public static @NotNull Optional<URI> checkedToURI(@NotNull URL url) {
        try {
            return Optional.of(url.toURI());
        } catch (URISyntaxException e) {
            logger.error("File path {} generated a syntax exception", url, e);
        }
        return Optional.empty();
    }

    public static @NotNull Optional<Path> findFile(Days forDay) {
        return Optional.ofNullable(DayRunner.class.getResource(forDay.getFileNameWithPackage()))
                .or(() -> Optional.ofNullable(DayRunner.class.getResource(forDay.getFileName())))
                .flatMap(DayRunner::checkedToURI)
                .map(Path::of)
                .filter(Files::exists)
                //try to find the file normally if .getResource failed
                .or(() -> Optional.of(Path.of(forDay.getFileNameWithPackage())))
                .filter(Files::exists)
                .or(() -> Optional.of(Path.of(forDay.getFileName())))
                .filter(Files::exists);
    }

    public static @NotNull Optional<Stream<String>> readFile(@NotNull Path path) {
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("Unable to read non-existing file");
        }
        //TODO change to loan pattern
        try {
            return Optional.of(Files.lines(path));
        } catch (IOException e) {
            logger.error("ERROR reading file: {}", path);
        }
        return Optional.empty();
    }

    public static void writeFile(@NotNull Path path, @NotNull Collection<String> input) {
        try {
            Files.write(path, input);
        } catch (IOException e) {
            logger.error("Failed to write to file {}", path.toAbsolutePath(), e);
        }
    }
    public static void writeOrCreateFile(@NotNull Collection<String> input, Days forDay) {
        //TODO find the correct path without needing string manipulation
        Path path1 = findFile(forDay).orElse(Path.of(forDay.getFileNameWithPackage()));
        logger.debug("Path for input file to write is '{}'", path1.toAbsolutePath());
        Path path2 = Path.of(path1.toAbsolutePath().toString().replace("/target/classes/","/src/main/resources/"));
        logger.debug("Path for input file changed to '{}'",path1.toAbsolutePath());

        writeFile(path1,input);
        writeFile(path2,input);
    }

    public static @NotNull Optional<Stream<String>> readInput(int dayIndex) {
        return readInput(Days.dayFromInt(dayIndex));
    }

    public static @NotNull Optional<Stream<String>> readInput(Days forDay) {
        return findFile(forDay)
                .flatMap(DayRunner::readFile);
    }


    // ----------++- DYNAMIC START UTILITIES -++---------------------------------------
    public static Day invokeDay(int value) {
        return invokeDay(Days.dayFromInt(value));
    }

    public static Day invokeDay(Days dayNum) {
        String className = dayNum.getClassName();
        try {
            return invoke(className);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error("Error invoking and instantiating %s via reflection".formatted(className));
            throw new IllegalArgumentException("Unable to locate .class for '%s'".formatted(className), e);
        }
    }

    public static <T> T invoke(String className) throws ClassNotFoundException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        @SuppressWarnings("unchecked")
        T t = (T) DayRunner.class.getClassLoader().loadClass(className).getConstructors()[0].newInstance();
        return t;
    }

    private static @NotNull String printResults(@NotNull Day solution) {
        String title = solution.getClass().getSimpleName() + ":";
        return Stream.of(
                        title,
                        solution.result1().orElse(INCOMPLETE),
                        solution.result2().orElse(""))
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.joining("\n  "));
    }

    public static void run(int forDay) {
        if (logger.isInfoEnabled()) {
            logger.info("Running starter for Day%02d%n".formatted(forDay));
        }

        final Days day = Days.dayFromInt(forDay);
        final Day solution = DayRunner.invokeDay(day);

        Optional<Stream<String>> input = readInput(day);
        if (input.isEmpty()) {
            if (logger.isErrorEnabled())
                logger.error("The Day%02d does not have a text file to read.%n".formatted(forDay));
            return;
        }
        solution.convertInput(input.get());
        solution.part1();
        solution.part2();

        System.out.println(printResults(solution));
    }

    private static OptionalInt tryParseArg(@NotNull String input) {
        try {
            int value = Integer.parseInt(input);
            if (value < 0 || value > 25) {
                if (logger.isErrorEnabled())
                    logger.error("Invalid command line argument: '%d' is not between 0 and 25".formatted(value));
                return OptionalInt.empty();
            }
            return OptionalInt.of(value);
        } catch (NumberFormatException e) {
            logger.error("Unable to parse command line argument: '%s', all command line arguments should be ints between 0 and 25".formatted(input));
            return OptionalInt.empty();
        }
    }
    private static @NotNull int[] parseArgs(final String[] args) {
        return Arrays.stream(args)
                .skip(1)
                .map(DayRunner::tryParseArg)
                .filter(OptionalInt::isPresent)
                .mapToInt(OptionalInt::getAsInt)
                .toArray();
    }
    public static void main(String[] args) {
        start(parseArgs(args));
    }
    public static void start(int... days) {
        logger.info("This AoC project is for year {}", Config.year());
        if (days.length != 0) {
            logger.info("Command line arguments found, running requested days.");
            Arrays.stream(days).forEach(DayRunner::run);
            return;
        }
        OptionalInt today = Time.getCurrentAoCDay();
        if (today.isPresent()) {
            logger.info("Event time detected! Running today.");
            DayRunner.run(today.orElseThrow());
        }
        logger.info("No day specified by command line or current time.");

        //DayRunner.run(1);
    }
}
