package aoc.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class AbstractDay implements Day {
    protected String result1;
    protected String result2;

    @Override
    public @NotNull Optional<String> result1() {
        return Optional.ofNullable(result1);
    }

    @Override
    public @NotNull Optional<String> result2() {
        return Optional.ofNullable(result2);
    }
}
