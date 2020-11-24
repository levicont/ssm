package com.lvg.ssm;

import java.time.LocalDate;

/**
 * Created by Victor Levchenko LVG Corp. on 22.11.2020.
 */
public class OpenOfficeUtils {
    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(1899,12,30);

    public static LocalDate getLocalDateFromDoubleValue(double value){
        return DEFAULT_START_DATE.plusDays((long)value);
    }


}
