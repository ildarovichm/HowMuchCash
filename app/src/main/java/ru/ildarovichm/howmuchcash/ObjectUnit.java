package ru.ildarovichm.howmuchcash;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ObjectUnit implements ObjectUnitInterface, Serializable {
    @SerializedName("unit")
    private Unit unit;
    @SerializedName("countOfObjectsOnUnit")
    private int countOfObjectsOnUnit;
    @SerializedName("typeOfObjectUnit")
    private String typeOfObjectUnit;
    @SerializedName("typeOfObject")
    private String typeOfObject;
    @SerializedName("parkingAvailability")
    private boolean parkingAvailability;
    @SerializedName("countNumberOfFloorsOfObject")
    private int countNumberOfFloorsOfObject;
    @SerializedName("toCheckBoxState")
    private boolean toCheckBoxState;

    public ObjectUnit() {
    }

    public ObjectUnit(
            Unit unit, //Адрес
            String typeOfObjectUnit, //Лифт, Подъемник
            String typeOfObject, //Грузопас-й, Пас-й
            int countNumberOfFloorsOfObject, //Кол-во остановок
            boolean parkingAvailability, //Наличие паркинга
            boolean toCheckBoxState){ //Отметка ТО
        this.unit = unit;
        this.typeOfObjectUnit = typeOfObjectUnit;
        this.typeOfObject = typeOfObject;
        this.countNumberOfFloorsOfObject = countNumberOfFloorsOfObject;
        this.parkingAvailability = parkingAvailability;
        this.toCheckBoxState = toCheckBoxState;
    }

    @Override
    public int getCountOfObjectsOnUnit() {
        return countOfObjectsOnUnit;
    }

    @Override
    public void setCountOfObjectsOnUnit(int countOfObjectsOnUnit) {
        this.countOfObjectsOnUnit = countOfObjectsOnUnit;
    }

    @Override
    public String getTypeOfObjectUnit() {
        return typeOfObjectUnit;
    }

    @Override
    public void setTypeOfObjectUnit(String typeOfObjectUnit) {
        this.typeOfObjectUnit = typeOfObjectUnit;
    }

    @Override
    public String getTypeOfObject() {
        return typeOfObject;
    }

    @Override
    public void setTypeOfObject(String typeOfObject) {
        this.typeOfObject = typeOfObject;
    }

    @Override
    public Unit getUnit() {
        return this.unit;
    }

    @Override
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public int getCountNumberOfFloorsOfObject() {
        return this.countNumberOfFloorsOfObject;
    }

    @Override
    public void setCountNumberOfFloorsOfObject(int countNumberOfFloorsOfObject) {
        this.countNumberOfFloorsOfObject = countNumberOfFloorsOfObject;
    }

    @Override
    public boolean getParkingAvailability() {
        return this.parkingAvailability;
    }

    @Override
    public void setParkingAvailability(boolean parkingAvailability) {
        this.parkingAvailability = parkingAvailability;
    }

    @Override
    public boolean getTOCheckBoxState() {
        return this.toCheckBoxState;
    }

    @Override
    public void setTOCheckBoxState(boolean toCheckBoxState) {
        this.toCheckBoxState = toCheckBoxState;
    }

    public  String toString(){
        return unit.toString() + "; " +
                countOfObjectsOnUnit + "; " +
                typeOfObjectUnit + "; " +
                typeOfObject + "; " +
                countNumberOfFloorsOfObject + "; " +
                parkingAvailability + "; " +
                toCheckBoxState;
    }
}
