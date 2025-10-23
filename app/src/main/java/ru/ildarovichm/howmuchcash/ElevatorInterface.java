package ru.ildarovichm.howmuchcash;

public interface ElevatorInterface {
    //Геттер и сеттер типа подъемника (гидравлический, барабанный, книжный, больничный, распашной и тд)
    String getTypeOfElevator();
    void setTypeOfElevator(String typeOfElevator);

    //Геттер и сеттер количества подъемников
    int getCountOfElevator();
    void setCountOfElevator(int countOfElevator);

    //Геттер и сеттер количества этажей на подъемнике
    int getCountNumberOfFloorsElevator();
    void setCountNumberOfFloorsElevator(int countNumberOfFloorsElevator);

    String toString();
}
