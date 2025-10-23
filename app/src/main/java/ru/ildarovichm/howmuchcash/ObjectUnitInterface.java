package ru.ildarovichm.howmuchcash;

public interface ObjectUnitInterface {
    //геттер и сеттер количества единиц на адресе
    int getCountOfObjectsOnUnit();
    void setCountOfObjectsOnUnit(int countOfObjectsOnUnit);

    //геттер и сеттер типа объекта (лифт или подъемник)
    String getTypeOfObjectUnit();
    void setTypeOfObjectUnit(String typeOfObjectUnit);

    //геттер и сеттер типа объекта
    // (пассажирский, грузовой, грузопассажирский,
    //малый грузовой, гидравлическая грузовая платформа, инвалидная платформа,
    //эскалатор)
    String getTypeOfObject();
    void setTypeOfObject(String typeOfObject);

    Unit getUnit();
    void setUnit(Unit unit);

    //геттер и сеттер количества остановок на лифте
    int getCountNumberOfFloorsOfObject();
    void setCountNumberOfFloorsOfObject(int countNumberOFFloorsOfObject);

    //геттер и сеттер наличия паркинга
    boolean getParkingAvailability();
    void setParkingAvailability(boolean parkingAvailability);

    boolean getTOCheckBoxState();
    void setTOCheckBoxState(boolean toCheckBoxState);

    boolean getDelCheckBoxState();
    void setDelCheckboxState(boolean delCheckboxState);

    String toString();
}
