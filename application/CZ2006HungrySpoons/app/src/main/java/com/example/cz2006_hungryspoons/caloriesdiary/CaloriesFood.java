package com.example.cz2006_hungryspoons.caloriesdiary;

public class CaloriesFood {
    private String foodName;
    private String foodGroup;
    private String subGroup;
    private String serving;

    public CaloriesFood(String foodName, String foodGroup, String subGroup, String serving) {
        this.foodName = foodName.toUpperCase();
        this.foodGroup = foodGroup;
        this.subGroup = subGroup;
        this.serving = serving;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(String foodGroup) {
        this.foodGroup = foodGroup;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }
}
