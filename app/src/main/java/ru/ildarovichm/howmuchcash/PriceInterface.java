package ru.ildarovichm.howmuchcash;

public interface PriceInterface {
    //Геттер и сеттер стоимости за единицу, где
    // newPriceForLift - стоимость за лифт,
    // newPriceForElevator - стоимость за подъемник,
    // newExtraChargeForSkyscraper - надбавка к единице свыше 16 этажей
    int getPriceForUnitLift();
    void setPriceForUnitLift(int priceForUnitLift);

    int getPriceForUnitElevator();
    void setPriceForUnitElevator(int priceForUnitElevator);

    int getExtraChargeForSkyscraper();
    void setExtraChargeForSkyscraper(int extraChargeForSkyscraper);

    //Геттер и сеттер стоимости за ночное дежурство с 18:00 до 9:00
    int getPriceForNightWatch();
    void setPriceForNightWatch(int priceForNightWatch);

    //Геттер и сеттер стоимости за суточное дежурство в выходные дни с 9:00 до 9:00
    int getPriceForWeekendWatch();
    void setPriceForWeekendWatch(int priceForWeekendWatch);

    //Геттер и сеттер коэффициента праздничных дней, default - x2, но указан на случай изменения тарифов
    //ratePublicHoliday - коэффициент праздничных дней х2
    //rateOrdinaryDay - коэффициент обычных дней х1
    int ratePublicHoliday = 2;
    int rateOrdinaryDay = 1;
    int getRatePublicHoliday();
    int getRateOrdinaryDay();
    void setRatePublicHoliday(int ratePublicHoliday);

    /*//геттер и сеттер рабочих смен в месяце
    int getCountWorkShiftInMonth();
    void setCountWorkShiftInMonth(int countWorkShiftInMonth);

    //геттер и сеттер фактически отработанных дней в месяце
    int getCountActualDaysWorked();
    void setCountActualDaysWorked(int countActualDaysWorked);*/
}
