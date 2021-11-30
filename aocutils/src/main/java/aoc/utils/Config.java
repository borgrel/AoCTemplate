package aoc.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum Config {
    ;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String YEAR_KEY = "YEAR";
    private static final String SESSION_COOKIE_KEY = "SESSION_COOKIE";

    private static @NotNull Dotenv requiresInstance() {
        return Objects.requireNonNull(dotenv,
                "Unable to load environment variables. Create a file called " +
                        "'.env' based on the 'example.env' file");
    }
    private static @NotNull String requiresKey(@NotNull final String key) {
        return Objects.requireNonNull(requiresInstance().get(key),
                "Environment Variables do not contain the key: '%s'".formatted(key));

    }

    public static @NotNull String sessionCookie() {
        return requiresKey(SESSION_COOKIE_KEY);
    }
    public static int year() {
        String value = requiresKey(YEAR_KEY);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid value for environment value year: '%s'".formatted(value),e);
        }
    }
}
