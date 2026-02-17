package ru.ildarovichm.howmuchcash;
import androidx.annotation.NonNull;

import java.io.Serializable;

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
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String getBuilding() {
        return buildings;
    }

    @Override
    public void setBuilding(String building) {
        this.buildings = building;
    }

    @Override
    public int getEntrance() {
        return entrance;
    }

    @Override
    public void setEntrance(int entrance) {
        this.entrance = entrance;
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
