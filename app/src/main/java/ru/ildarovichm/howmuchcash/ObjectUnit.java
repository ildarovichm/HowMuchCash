package ru.ildarovichm.howmuchcash;

import java.io.Serializable;

public class ObjectUnit implements ObjectUnitInterface, Serializable {
    private Unit unit;
    private int countOfObjectsOnUnit;
    private String typeOfObjectUnit;
    private String typeOfObject;
    private boolean parkingAvailability;
    private int countNumberOfFloorsOfObject;
    private boolean toCheckBoxState;
    private boolean delCheckBoxState;

    public ObjectUnit(
            Unit unit,
//            int countOfObjectsOnUnit,
            String typeOfObjectUnit,
            String typeOfObject,
            int countNumberOfFloorsOfObject,
            boolean parkingAvailability,
            boolean toCheckBoxState,
            boolean delCheckBoxState){
        this.unit = unit;
//        this.countOfObjectsOnUnit = countOfObjectsOnUnit;
        this.typeOfObjectUnit = typeOfObjectUnit;
        this.typeOfObject = typeOfObject;
        this.countNumberOfFloorsOfObject = countNumberOfFloorsOfObject;
        this.parkingAvailability = parkingAvailability;
        this.toCheckBoxState = toCheckBoxState;
        this.delCheckBoxState = delCheckBoxState;
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

    @Override
    public boolean getDelCheckBoxState() {
        return this.delCheckBoxState;
    }

    @Override
    public void setDelCheckboxState(boolean delCheckBoxState) {
        this.delCheckBoxState = delCheckBoxState;

    }

    public  String toString(){
        return unit.toString() + "; " +
                countOfObjectsOnUnit + "; " +
                typeOfObjectUnit + "; " +
                typeOfObject + "; " +
                countNumberOfFloorsOfObject + "; " +
                parkingAvailability + "; " +
                toCheckBoxState + "; " +
                delCheckBoxState;
    }
}
