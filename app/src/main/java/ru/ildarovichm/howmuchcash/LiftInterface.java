package ru.ildarovichm.howmuchcash;

public interface LiftInterface {
    //Геттер и сеттер количества лифтов
    int getCountOfLift();
    void setCountOfLift(int countOfLift);

    //Геттер и сеттер типа лифта (грузовой, пассажирский, грузопассажирский)
    String getTypeOfLift();
    void setTypeOfLift(String typeOfLift);

    //Геттер и сеттер количества этажей на лифте
    int getCountNumberOfFloorsLift();
    void setCountNumberOfFloorsLift(int countNumberOfFloorsLift);

    //Геттер и сеттер наличия паркинга
    boolean getParkingAvailability();
    void setParkingAvailability(boolean parkingAvailability);

    //Геттер и сеттер количества этажей паркинга
    int getCountParkingFloors();
    void setCountParkingFloors(int countParkingFloors);
}
