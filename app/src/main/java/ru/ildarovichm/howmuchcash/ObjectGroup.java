package ru.ildarovichm.howmuchcash;

import java.util.ArrayList;
import java.util.Objects;

public class ObjectGroup {
    private String city;
    private String street;
    private String building; // может быть null
    private ArrayList<ObjectUnit> objects = new ArrayList<>();
    private boolean isExpanded = false;

    public ObjectGroup(String city, String street, String building) {
        this.city = city;
        this.street = street;
        this.building = building;
    }

    // Группировка по городу и улице
    public static String getGroupKey(ObjectUnit object, GroupLevel level) {
        switch (level) {
            case CITY:
                return object.getUnit().getCity();
            case CITY_STREET:
                return object.getUnit().getCity() + ", " + object.getUnit().getStreet();
            case CITY_STREET_BUILDING:
                return object.getUnit().getCity() + ", " + object.getUnit().getStreet() + ", " + object.getUnit().getBuilding();
            default:
                throw new IllegalArgumentException("Unknown group level: " + level);
        }
    }

    public enum GroupLevel {
        CITY,
        CITY_STREET,
        CITY_STREET_BUILDING; // ← Добавьте этот уровень
    }

    // Геттеры и сеттеры
    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getBuilding() { return building; }
    public ArrayList<ObjectUnit> getObjects() { return objects; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectGroup that = (ObjectGroup) o;
        return Objects.equals(city, that.city) &&
                Objects.equals(street, that.street) &&
                Objects.equals(building, that.building);
    }
}