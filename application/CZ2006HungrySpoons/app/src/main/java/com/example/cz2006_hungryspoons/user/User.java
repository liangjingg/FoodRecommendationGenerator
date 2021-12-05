package com.example.cz2006_hungryspoons.user;

public class User {

    private String userName;
    private String gender;
    private double userWeight;
    private double userHeight;
    private double userBMI;

    public User(String userName, String gender, double userWeight, double userHeight, double userBMI) {
        this.userName = userName;
        this.gender = gender;
        this.userWeight = userWeight;
        this.userHeight = userHeight;
        this.userBMI = userBMI;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(double userWeight) {
        this.userWeight = userWeight;
    }

    public double getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(double userHeight) {
        this.userHeight = userHeight;
    }

    public double getUserBMI() {
        return userBMI;
    }

    public void setUserBMI(double userBMI) {
        this.userBMI = userBMI;
    }
}
