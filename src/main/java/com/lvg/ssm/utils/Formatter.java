package com.lvg.ssm.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Victor Levchenko LVG Corp. on 13.12.2020.
 */
public abstract class Formatter {
    private static final String DATE_TIME_FORMAT_PATTERN = "dd.MM.yyyy";

    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
    }
}
