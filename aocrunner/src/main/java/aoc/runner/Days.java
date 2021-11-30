package aoc.runner;

public enum Days {
    DAY01,
    DAY02,
    DAY03,
    DAY04,
    DAY05,
    DAY06,
    DAY07,
    DAY08,
    DAY09,
    DAY10,
    DAY11,
    DAY12,
    DAY13,
    DAY14,
    DAY15,
    DAY16,
    DAY17,
    DAY18,
    DAY19,
    DAY20,
    DAY21,
    DAY22,
    DAY23,
    DAY24,
    DAY25,
    OUTPUT;

    private static final String packageName = "aoc.days";

    public String getFileName() {
        return "/%s.txt".formatted(name().toLowerCase());
    }
    public String getFileNameWithPackage() {
        return "/%s%s".formatted(packageName.replace(".","/"), getFileName());
    }
    public String getClassName() {
        return "%s.%s".formatted(packageName, name().toLowerCase().replaceFirst("d","D"));
    }

    public static Days dayFromInt(int value) {
        return Days.values()[value-1];
    }
}
