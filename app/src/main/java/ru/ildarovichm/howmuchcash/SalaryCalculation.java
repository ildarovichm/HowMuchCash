package ru.ildarovichm.howmuchcash;
import java.util.ArrayList;
import java.util.ListIterator;

import java.time.LocalDate;

public class SalaryCalculation {
    private ArrayList<ObjectUnit> amountObjectUnitList;
    private int countAmountObjectUnit;
    static int countAmountObjectUnitLift;
    static int countAmountObjectUnitElevator;
    static Price price;
    private ArrayList<Price> priceList;
    static int salaryAmountLift;
    static int salaryAmountElevator;
    private int salaryAmountObjectUnit;
    static int countAmountLiftSkyscraper;
    static int countAmountLiftLowRise;
    private int salaryAmountWatch;

    private ArrayList<LocalDate> nightWatchList;
    private ArrayList<LocalDate> weekendWatchList;
    //private ArrayList<ArrayList<LocalDate>> watch;

    //конструктор объекта со списком дежурств и кол-вом смен
    public SalaryCalculation(ArrayList<LocalDate> nightWatchList, ArrayList<LocalDate> weekendWatchList, Price price, ArrayList<ObjectUnit> amountObjectUnitList){
        this.nightWatchList = nightWatchList;
        this.weekendWatchList = weekendWatchList;
        this.price = price;
        this.amountObjectUnitList = amountObjectUnitList;

//        int resCountObjectUnitLift = this.getCountObjectUnitLift();
//        int resCountObjectUnitElevator = this.getCountObjectUnitElevator();
//        int resCountAmountLiftSkyscraper = this.getCountAmountLiftSkyscraper();
//        int resCountAmountObjectUnit = this.getCountAmountObjectUnit();
//
//        int resSalaryAmountObjectUnitLift = this.getSalaryAmountObjectUnitLift();
//        int resSalaryAmountObjectUnitElevator = this.getSalaryAmountObjectUnitElevator();
//        int resSalaryAmountObjectUnit = this.getSalaryAmountObjectUnit();
//        int resSalaryAmountWatch = this.getSalaryAmountWatch();
    }

    public int getCountAmountObjectUnit(){
        ListIterator<ObjectUnit> listIter = amountObjectUnitList.listIterator();
        while(listIter.hasNext()) {
            listIter.next();
            countAmountObjectUnit++;
        }
        return countAmountObjectUnit;
    }

    //метод для подсчета кол-ва лифтов высоток
    public int getCountAmountLiftSkyscraper(){
        int i = 0;
        ListIterator<ObjectUnit> listIter = amountObjectUnitList.listIterator();
        while(listIter.hasNext()) {
            if(listIter.next().getCountNumberOfFloorsOfObject() >= 16){
                countAmountLiftSkyscraper++;
            }
        } return countAmountLiftSkyscraper;
    }

    //метод объекта для подсчета кол-ва лифтов
    public int getCountObjectUnitLift(){
        int i = 0;
        ListIterator<ObjectUnit> listIter = amountObjectUnitList.listIterator();
        while(listIter.hasNext()) {
            if(listIter.next().getTypeOfObjectUnit().equals("Лифт")) {
                countAmountObjectUnitLift++;
            }
        }
        return countAmountObjectUnitLift;
    }

    //метод объекта для подсчета кол-ва подъемников
    public int getCountObjectUnitElevator(){
        int i = 0;
        ListIterator<ObjectUnit> listIter = amountObjectUnitList.listIterator();
        while(listIter.hasNext()) {
            if(listIter.next().getTypeOfObjectUnit().equals("Подъемник")) {
                countAmountObjectUnitElevator++;
            }
        } return countAmountObjectUnitElevator;
    }

    //метод объекта для подсчета зп за лифты
    public int getSalaryAmountObjectUnitLift(){
        salaryAmountObjectUnit = 0;
        countAmountLiftLowRise = countAmountObjectUnitLift - countAmountLiftSkyscraper;
        salaryAmountLift = (price.getPriceForUnitLift() * countAmountLiftLowRise) + ((price.getPriceForUnitLift() + price.getExtraChargeForSkyscraper()) * countAmountLiftSkyscraper);
        return salaryAmountLift;
    }

    //метод объекта для подсчета зп за подъемники
    public int getSalaryAmountObjectUnitElevator(){
        salaryAmountElevator = 0;
        salaryAmountElevator = price.getPriceForUnitElevator() * countAmountObjectUnitElevator;
        return salaryAmountElevator;
    }

    //метод объекта для подсчета общей зп за объем
    public int getSalaryAmountObjectUnit(){
        salaryAmountWatch = 0;
        salaryAmountObjectUnit = salaryAmountLift + salaryAmountElevator;
        return salaryAmountObjectUnit;
    }

    //метод объекта для подсчета зп за дежурства
    public int getSalaryAmountWatch(){
        int countOrdinary = nightWatchList.size();
        int countWeekend = weekendWatchList.size();
        salaryAmountWatch = (countOrdinary * price.getPriceForNightWatch()) + (countWeekend * price.getPriceForWeekendWatch());
        return salaryAmountWatch;
    }

    public int getSalaryAmountAll(int countWorkShiftInMonth, int countActualDaysWorked) {
        return ((salaryAmountObjectUnit * countWorkShiftInMonth) / countActualDaysWorked) + salaryAmountWatch;
    }

}