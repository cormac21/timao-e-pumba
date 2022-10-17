package com.cormacx.timaoepumba.util;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalendarUtilTest {


    @Test
    public void returnsCorrectFirstDay() {
        Date firstOfOctober2022 = CalendarUtil.getFirstDayOfMonth(2022, 9);
        Calendar rightDay = Calendar.getInstance();
        rightDay.set(2022, 9, 1, 0, 0, 0);

        assertEquals(rightDay.getTime(), firstOfOctober2022);
    }

    @Test
    public void returnsCorrectLastDay() {
        Date lastOfOctober2022 = CalendarUtil.getLastDayOfMonth(2022, 9);
        Calendar rightDay = Calendar.getInstance();
        rightDay.set(2022, 9, 31, 0, 0, 0);

        assertEquals(rightDay.getTime(), lastOfOctober2022);
    }


}
