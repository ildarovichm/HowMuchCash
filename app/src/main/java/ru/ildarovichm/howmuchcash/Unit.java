package ru.ildarovichm.howmuchcash;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Unit implements UnitInterface, Serializable {
    private String address;
    private String city;
    private String street;
    private String buildings;
    private int entrance;

    public Unit(){
    }

    public Unit(
            String city,
            String street,
            String buildings,
            int entrance
    ){
        this.city = city;
        this.street = street;
        this.buildings = buildings;
        this.entrance = entrance;
    }

    @Override
    public String getAddress() {
        return city + ", " + street + ", " + buildings + ", " + entrance;
    }

    @Override
    public void setAddress(String city, String street, String buildings, int entrance) {
        this.city = city;
        this.street = street;
        this.buildings = buildings;
        this.entrance = entrance;
    }

    @NonNull
    public String toString(){

        return city + ", " + street + ", " + buildings + ", " + entrance;
    }
}
