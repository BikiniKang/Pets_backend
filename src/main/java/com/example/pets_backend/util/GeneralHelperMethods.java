package com.example.pets_backend.util;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class GeneralHelperMethods {

    public static List<String> getDateSpanOfMonth(String month) {
        LocalDate beginDate = LocalDate.parse(month + "-01");
        LocalDate endDate = beginDate.with(lastDayOfMonth());
        return getDateSpan(beginDate, endDate);
    }

    public static List<String> getDateSpan(String from, String to) {
        if (from.equals(to)) { // if the start and the end are in the same day, just return this day
            return List.of(from);
        }
        LocalDate dateFrom = LocalDate.parse(from);
        LocalDate dateTo = LocalDate.parse(to).plusDays(1); // plus 1 day to make sure that dateTo is included in the list
        return dateFrom.datesUntil(dateTo)
                .map(LocalDate::toString)
                .toList();
    }

    public static List<String> getDateSpan(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom.equals(dateTo)) {
            return List.of(dateFrom.toString());
        }
        return dateFrom.datesUntil(dateTo.plusDays(1)).map(LocalDate::toString).toList();
    }

    public static List<String> getDateSpan(String from, String to, String month) {
        if (from.substring(0, 7).compareTo(month) <= 0 && to.substring(0, 7).compareTo(month) >= 0) {
            if (from.equals(to)) { // if the start and the end are in the same day, just return this day
                return List.of(from);
            }
            LocalDate dateFrom = LocalDate.parse(from);
            LocalDate dateTo = LocalDate.parse(to).plusDays(1); // plus 1 day to make sure that dateTo is included in the list
            return dateFrom.datesUntil(dateTo)
                    .map(LocalDate::toString)
                    .filter(s -> s.startsWith(month))
                    .toList();
        } else {
            return new ArrayList<>();
        }

    }
}
