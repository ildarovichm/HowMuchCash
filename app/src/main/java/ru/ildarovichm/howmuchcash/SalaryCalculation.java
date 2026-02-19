package ru.ildarovichm.howmuchcash;
import java.util.ArrayList;
import java.util.ListIterator;

import java.time.LocalDate;

public class SalaryCalculation {
    private int countAmountObjectUnit;
    static int countAmountObjectUnitLift;
    static int countAmountObjectUnitElevator;
    static int salaryAmountLift;
    static int salaryAmountElevator;
    private int salaryAmountObjectUnit;
    static int countAmountLiftSkyscraper;
    static int countAmountLiftLowRise;
    private int salaryAmountWatch;
    private final ArrayList<ObjectUnit> amountObjectUnitList;
    private final Price price;
    private final ArrayList<LocalDate> nightWatchList;
    private final ArrayList<LocalDate> weekendWatchList;

    //конструктор объекта со списком дежурств и кол-вом смен
    public SalaryCalculation(ArrayList<LocalDate> nightWatchList,
                             ArrayList<LocalDate> weekendWatchList,
                             Price price,
                             ArrayList<ObjectUnit> amountObjectUnitList) {
        this.nightWatchList = nightWatchList;
        this.weekendWatchList = weekendWatchList;
        this.price = price;
        this.amountObjectUnitList = amountObjectUnitList;
    }

    // Подсчёт общего количества объектов
    public int getCountAmountObjectUnit() {
        return amountObjectUnitList.size();
    }

    // Подсчёт высоток (≥16 этажей)
    public int getCountAmountLiftSkyscraper() {
        int count = 0;
        for (ObjectUnit unit : amountObjectUnitList) {
            if ("Лифт".equals(unit.getTypeOfObjectUnit().trim()) &&
                    unit.getCountNumberOfFloorsOfObject() > 16) {
                count++;
            }
        }
        return count;
    }

    // Подсчёт лифтов
    public int getCountObjectUnitLift() {
        int count = 0;
        ListIterator<ObjectUnit> listIter = amountObjectUnitList.listIterator();
        while (listIter.hasNext()) {
            ObjectUnit unit = listIter.next();
            if ("Лифт".equals(unit.getTypeOfObjectUnit().trim())) {
                count++;
            }
        }
        return count;
    }

    // Подсчёт подъёмников
    public int getCountObjectUnitElevator() {
        int count = 0;
        ListIterator<ObjectUnit> listIter = amountObjectUnitList.listIterator();
        while (listIter.hasNext()) {
            ObjectUnit unit = listIter.next();
            if ("Подъемник".equals(unit.getTypeOfObjectUnit().trim())) {
                count++;
            }
        }
        return count;
    }

    // ЗП за лифты
    public int getSalaryAmountObjectUnitLift() {
        int countLift = getCountObjectUnitLift();
        int countSkyscraper = getCountAmountLiftSkyscraper();
        int countLowRise = countLift - countSkyscraper;

        return (price.getPriceForUnitLift() * countLowRise) +
                ((price.getPriceForUnitLift() + price.getExtraChargeForSkyscraper()) * countSkyscraper);
    }

    // ЗП за подъёмники
    public int getSalaryAmountObjectUnitElevator() {
        return price.getPriceForUnitElevator() * getCountObjectUnitElevator();
    }

    // Общая ЗП за объем
    public int getSalaryAmountObjectUnit() {
        return getSalaryAmountObjectUnitLift() + getSalaryAmountObjectUnitElevator();
    }

    // ЗП за дежурства
    public int getSalaryAmountWatch() {
        int countOrdinary = nightWatchList != null ? nightWatchList.size() : 0;
        int countWeekend = weekendWatchList != null ? weekendWatchList.size() : 0;
        return (countOrdinary * price.getPriceForNightWatch()) +
                (countWeekend * price.getPriceForWeekendWatch());
    }

    // Итоговая ЗП
    public int getSalaryAmountAll(int countWorkShiftInMonth, int countActualDaysWorked) {
        if (countActualDaysWorked == 0) return 0;
        int baseSalary = getSalaryAmountObjectUnit();
        return (baseSalary * countWorkShiftInMonth) / countActualDaysWorked + getSalaryAmountWatch();
    }

}