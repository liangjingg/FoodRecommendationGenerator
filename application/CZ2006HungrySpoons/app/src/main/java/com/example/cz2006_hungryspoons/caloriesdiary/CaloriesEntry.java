package com.example.cz2006_hungryspoons.caloriesdiary;

public class CaloriesEntry {
    private String date;
    private String mealType;
    private String name;
    private String perServing;
    private int count;
    private double totCalories;

    public CaloriesEntry(String date, String mealType, String name, String perServing, int count, double totCalories) {
        this.date = date;
        this.mealType = mealType;
        this.name = name.toUpperCase();
        this.perServing = perServing;
        this.count = count;
        this.totCalories = totCalories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerServing() {
        return perServing;
    }

    public void setPerServing(String perServing) {
        this.perServing = perServing;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotCalories() {
        return totCalories;
    }

    public void setTotCalories(double totCalories) {
        this.totCalories = totCalories;
    }
}

