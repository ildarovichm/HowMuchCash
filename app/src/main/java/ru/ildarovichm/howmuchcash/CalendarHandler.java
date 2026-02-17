package ru.ildarovichm.howmuchcash;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ListIterator;

import ru.ildarovichm.howmuchcash.WeekendFinder;

public class CalendarHandler {
    static int year;
    static int month;
    static String userDate = "";

    public ArrayList<ArrayList<String>> allDatesList = new ArrayList<>();

    //создается пустой объект класса CalendarHandler
    public CalendarHandler(ArrayList<ArrayList<String>> allDatesList){
        this.allDatesList = allDatesList;
    }

    //данным методом добавляется дата в объект
    public void addDate(String DAY, String MONTH, String YEAR){
        ArrayList<String> dateList = new ArrayList<>();
        userDate = YEAR + "-" + MONTH + "-" + DAY;
//        for (ArrayList<String> dateList1 : allDatesList1) {
//            if (!dateList1.contains(userDate)) {
                dateList.add(userDate);
                allDatesList.add(dateList);
//            }
//        }
    }

    public void clearList() {
        allDatesList.clear();
    }
    public ArrayList<ArrayList<String>> getAllWatchDateList() {
        return allDatesList;
    }
    //метод преобразует список дат в формат  localdate
    public ArrayList<LocalDate> localDateListFromStringList(){
        ArrayList<LocalDate> allLocalDateList = new ArrayList<>();
        int i = 0;
        for (ArrayList<String> dateList : allDatesList) {
            for (String date : dateList) {
                //System.out.println("Дата " + i  + " : " +  date);
                String[] elements = date.split("-");
                i++;
                int y = Integer.parseInt(elements[0]);
                int m = Integer.parseInt(elements[1]);
                int d = Integer.parseInt(elements[2]);
                LocalDate tmpDate = LocalDate.of(y, m, d);
                allLocalDateList.add(tmpDate);
            }
        }
        return allLocalDateList;
    }

    public ArrayList<LocalDate> getWeekendWatchList(int year, int month){
//        this.year = year;
//        this.month = month;
        WeekendFinder weekendFinder = new WeekendFinder(year, month);

        ArrayList<LocalDate> common = new ArrayList<>(weekendFinder.getWeekendList());
        this.localDateListFromStringList();
        common.retainAll(this.localDateListFromStringList());
        Collections.sort(common);
        return common;
    }
    
    public ArrayList<LocalDate> getNightWatchList(int year, int month){
        CalendarHandler.year = year;
        CalendarHandler.month = month;
        int day = 1;
        WeekendFinder weekendFinder = new WeekendFinder(year, month);

        ArrayList<LocalDate> common1 = new ArrayList<>(this.localDateListFromStringList());

        ArrayList<LocalDate> common = new ArrayList<>(weekendFinder.getWeekendList());

        common1.removeIf(date -> common.contains(date));
        Collections.sort(common1);
        return common1;
    }

    public String toString(){
        if (!allDatesList.isEmpty()) {
            return allDatesList.toString();
        } else return "";
    }
}