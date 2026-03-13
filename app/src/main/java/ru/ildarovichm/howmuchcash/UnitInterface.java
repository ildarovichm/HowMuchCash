package ru.ildarovichm.howmuchcash;

import androidx.annotation.NonNull;

public interface UnitInterface {
    String getAddress();
    String getCity();
    void setCity(String city);
    String getStreet();
    void setStreet(String street);
    String getBuilding();
    void setBuilding(String building);
    int getEntrance();
    void setEntrance(int entrance);
    void setAddress(String newCity, String newStreet, String newBuilding, int newEntrance);
    @NonNull
    String toString();
}
