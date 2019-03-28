package io.github.logtube.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class Epochs {

    public static long beginningOfTheDay(long epoch) {
        LocalDateTime time = epochToLocalDateTime(epoch);
        LocalDateTime start = time.with(LocalTime.MIN);
        return start.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
    }

    public static long endOfTheDay(long epoch) {
        LocalDateTime time = epochToLocalDateTime(epoch);
        LocalDateTime end = time.with(LocalTime.MAX);
        return end.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
    }

    private static LocalDateTime epochToLocalDateTime(long epoch) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
    }

}
