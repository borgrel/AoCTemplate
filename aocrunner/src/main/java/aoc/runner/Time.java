package aoc.runner;

import aoc.utils.Config;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.OptionalInt;

public enum Time {
    ;

    private static final ZoneId EASTERN_STANDARD_TIME = ZoneId.of("EST5EDT");
    private static final int DECEMBER = 12;

    public static @NotNull ZonedDateTime aocNowTime() {
        return ZonedDateTime.now(EASTERN_STANDARD_TIME);
    }

    public static @NotNull OptionalInt getCurrentAoCDay() {
        ZonedDateTime now = aocNowTime();
        if (now.get(ChronoField.YEAR) != Config.year() || now.get(ChronoField.MONTH_OF_YEAR) != DECEMBER) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(now.get(ChronoField.DAY_OF_MONTH));
    }

}
