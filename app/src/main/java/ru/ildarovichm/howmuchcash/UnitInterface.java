package ru.ildarovichm.howmuchcash;

import androidx.annotation.NonNull;

public interface UnitInterface {
    String getAddress();
    void setAddress(String newCity, String newStreet, String newBuilding, int newEntrance);
    @NonNull
    String toString();
}
