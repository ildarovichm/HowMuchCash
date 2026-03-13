package ru.ildarovichm.howmuchcash;

import android.os.Build;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WeekendFinder {
    int year;
    int month;
    int day = 1;

    static ArrayList<LocalDate> weekendsList;

    public WeekendFinder(int year, int month){
        weekendsList = new ArrayList<>();
        this.year = year;
        this.month = month;
        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int count1 = 0;
        int count2 = 0;
        boolean found = false;
        for (LocalDate d = date; d.getMonth() == date.getMonth(); d = d.plusDays(found ? 1 : 1)) {
            if (DayOfWeek.SUNDAY == d.getDayOfWeek()) {
                count1++;
                found = true;
                String formattedString = d.format(formatter);
                weekendsList.add(d);
            } else if (DayOfWeek.SATURDAY == d.getDayOfWeek()) {
                count2++;
                found = true;
                String formattedString = d.format(formatter);
                weekendsList.add(d);
            }
        }

    }

    public ArrayList<LocalDate> getWeekendList() {
        return weekendsList;
    }
}
