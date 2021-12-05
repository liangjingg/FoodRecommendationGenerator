package com.example.cz2006_hungryspoons.caloriesdiary;

public class CaloriesExercise {
    int exerciseid;
    int met;
    String exercisename;

    public CaloriesExercise(int exerciseid, String exercisename, int met) {
        this.exerciseid = exerciseid;
        this.exercisename = exercisename;
        this.met = met;
    }

    public int getExerciseid() {
        return exerciseid;
    }

    public void setExerciseid(int exerciseid) {
        this.exerciseid = exerciseid;
    }

    public double getMet() {
        return met;
    }

    public void setMet(int met) {
        this.met = met;
    }
    public String getExercisename() {
        return exercisename;
    }

    public void setExercisename(String exercisename) {
        this.exercisename = exercisename;
    }

    public int calculateex(int totalcalories) {
        int duration;
        duration = totalcalories/met;
        //duration = Math.ceil(totalcalories/met);
        return duration;
    }
}
