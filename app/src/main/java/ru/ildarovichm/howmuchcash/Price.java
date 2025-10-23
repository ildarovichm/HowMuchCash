package ru.ildarovichm.howmuchcash;

import java.io.Serializable;

public class Price implements PriceInterface, Serializable {
    //стоимость за единицу
    private int priceForUnitLift;
    private int priceForUnitElevator;
    private int extraChargeForSkyscraper;

    //стоимость дежурств ночных и суточных
    private int priceForNightWatch;
    private int priceForWeekendWatch;

    //коэффициент - множитель дежурств в праздничные дни. По умолчанию - х2
    private int ratePublicHoliday;
    private int rateOrdinaryDay;

    //количество рабочих смен в месяце и кол-во фактически отработанных смен
//    private int countWorkShiftInMonth;
//    private int countActualDaysWorked;

    //конструктор объекта только cо всеми тарифами
    public Price(
            int priceForUnitLift,
            int priceForUnitElevator,
            int extraChargeForSkyscraper,
            int priceForNightWatch,
            int priceForWeekendWatch,
            int ratePublicHoliday,
            int rateOrdinaryDay
//            int countWorkShiftInMonth,
//            int countActualDaysWorked
    ){
        this.priceForUnitLift = priceForUnitLift;
        this.priceForUnitElevator = priceForUnitElevator;
        this.extraChargeForSkyscraper = extraChargeForSkyscraper;
        this.priceForNightWatch = priceForNightWatch;
        this.priceForWeekendWatch = priceForWeekendWatch;
        this.ratePublicHoliday = ratePublicHoliday;
        this.rateOrdinaryDay = rateOrdinaryDay;
//        this.countWorkShiftInMonth = countWorkShiftInMonth;
//        this.countActualDaysWorked = countActualDaysWorked;
    }

    //конструктор объекта только для учета зп за объем
//    public Price(
//            int priceForUnitLift,
//            int priceForUnitElevator,
//            int extraChargeForSkyscraper
//    ){
//        this.priceForUnitLift = priceForUnitLift;
//        this.priceForUnitElevator = priceForUnitElevator;
//        this.extraChargeForSkyscraper = extraChargeForSkyscraper;
//    }

    //конструктор объекта только для учета зп за дежурства
//    public Price(
//            int priceForNightWatch,
//            int priceForWeekendWatch,
//            int ratePublicHoliday,
//            int rateOrdinaryDay
//    ){
//        this.priceForNightWatch = priceForNightWatch;
//        this.priceForWeekendWatch = priceForWeekendWatch;
//        this.ratePublicHoliday = ratePublicHoliday;
//        this.rateOrdinaryDay = rateOrdinaryDay;
//    }

//    public Price(
//            int countWorkShiftInMonth,
//            int countActualDaysWorked
//    ){
//        this.countWorkShiftInMonth = countWorkShiftInMonth;
//        this.countActualDaysWorked = countActualDaysWorked;
//    }


    @Override
    public int getPriceForUnitLift() {
        return priceForUnitLift;
    }

    @Override
    public void setPriceForUnitLift(int priceForUnitLift) {
        this.priceForUnitLift = priceForUnitLift;
    }

    @Override
    public int getPriceForUnitElevator() {
        return priceForUnitElevator;
    }

    @Override
    public void setPriceForUnitElevator(int priceForUnitElevator) {
        this.priceForUnitElevator = priceForUnitElevator;
    }

    @Override
    public int getExtraChargeForSkyscraper() { return extraChargeForSkyscraper; }

    @Override
    public void setExtraChargeForSkyscraper(int extraChargeForSkyscraper) {
        this.extraChargeForSkyscraper = extraChargeForSkyscraper;
    }

    @Override
    public int getPriceForNightWatch() {
        return this.priceForNightWatch;
    }

    @Override
    public void setPriceForNightWatch(int priceForNightWatch) {
        this.priceForNightWatch = priceForNightWatch;
    }

    @Override
    public int getPriceForWeekendWatch() {
        return this.priceForWeekendWatch;
    }

    @Override
    public void setPriceForWeekendWatch(int priceForWeekendWatch) {
        this.priceForWeekendWatch = priceForWeekendWatch;
    }

    @Override
    public int getRatePublicHoliday() {
        return this.ratePublicHoliday;
    }

    @Override
    public int getRateOrdinaryDay() {
        return this.rateOrdinaryDay;
    }

    @Override
    public void setRatePublicHoliday(int ratePublicHoliday) {
        this.ratePublicHoliday = ratePublicHoliday;
    }
    /*
    @Override
    public int getCountWorkShiftInMonth() {
        return this.countWorkShiftInMonth;
    }

    @Override
    public void setCountWorkShiftInMonth(int countWorkShiftInMonth) {
        this.countWorkShiftInMonth = countWorkShiftInMonth;
    }

    @Override
    public int getCountActualDaysWorked() {
        return this.countActualDaysWorked;
    }

    @Override
    public void setCountActualDaysWorked(int countActualDaysWorked) {
        this.countActualDaysWorked = countActualDaysWorked;
    }
    */
    @Override
    public String toString(){
//        if (
//                priceForUnitLift != 0 &
//                priceForUnitElevator != 0 &
//                extraChargeForSkyscraper != 0 &
//                        priceForNightWatch != 0 &
//                        priceForWeekendWatch != 0 &
//                        ratePublicHoliday != 0 &
//                        rateOrdinaryDay != 0
////                        countWorkShiftInMonth != 0 &
////                        countActualDaysWorked != 0
//        ){
//            return priceForUnitLift + ", " +
//                    priceForUnitElevator + ", " +
//                    extraChargeForSkyscraper + ", " +
//                    priceForNightWatch + ", " +
//                    priceForWeekendWatch + ", " +
//                    ratePublicHoliday + ", " +
//                    rateOrdinaryDay;
////                    countWorkShiftInMonth + ", " +
////                    countActualDaysWorked;
//        } else if (priceForUnitLift != 0 || priceForUnitElevator != 0 || extraChargeForSkyscraper != 0) {
//            return priceForUnitLift + ", " +
//                    priceForUnitElevator + ", " +
//                    extraChargeForSkyscraper;
//        } else if(priceForNightWatch != 0 || priceForWeekendWatch != 0 || ratePublicHoliday != 0 || rateOrdinaryDay != 0) {
//            return priceForNightWatch + ", " +
//                    priceForWeekendWatch + ", " +
//                    ratePublicHoliday + ", " +
//                    rateOrdinaryDay;
//
////        } else if (countWorkShiftInMonth != 0 || countActualDaysWorked != 0) {
////            return countWorkShiftInMonth + ", " + countActualDaysWorked;
//        } return "fault";
        return priceForUnitLift + ", " +
                    priceForUnitElevator + ", " +
                    extraChargeForSkyscraper + ", " +
                    priceForNightWatch + ", " +
                    priceForWeekendWatch + ", " +
                    ratePublicHoliday + ", " +
                    rateOrdinaryDay;
    }
}
