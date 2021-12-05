package com.example.cz2006_hungryspoons.caloriesdiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cz2006_hungryspoons.R;
import com.example.cz2006_hungryspoons.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;

public class CaloriesExerciseView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);




        setContentView(R.layout.content_calories_recexercise_view);

        //create reference to all the label
        TextView lbltotalcalories = findViewById(R.id.lbltotalcalories);
        TextView lblbwduration = findViewById(R.id.lblbwduration);
        TextView lblyogaduration = findViewById(R.id.lblyogaduration);
        TextView lblswimduration = findViewById(R.id.lblswimduration);
        TextView lblrunduration = findViewById(R.id.lblrunduration);
        TextView lblcycduration = findViewById(R.id.lblcycduration);

        //all other labels (hardcoded stuffs)
        TextView caloriesdesc = findViewById(R.id.lblCaloriesDesc);
        TextView lblexsummary = findViewById(R.id.lblexsummary);
        TextView lblvw = findViewById(R.id.lblbriskwalk);
        TextView lblyoga = findViewById(R.id.lblYoga);
        TextView lblrun = findViewById(R.id.lblrun);
        TextView lblswim = findViewById(R.id.lblswim);
        TextView lblcyc = findViewById(R.id.lblcyc);

        TextView min = findViewById(R.id.lblmin0);
        TextView min2 = findViewById(R.id.lblmin1);
        TextView min3 = findViewById(R.id.lblmin2);
        TextView min4 = findViewById(R.id.lblmin3);
        TextView min5 = findViewById(R.id.lblmin4);

        //object creation
        CaloriesExercise briskwalk = new CaloriesExercise(0,"briskwalk",2);
        CaloriesExercise yoga = new CaloriesExercise(1,"yoga",3);
        CaloriesExercise run = new CaloriesExercise(2,"running",6);
        CaloriesExercise swim = new CaloriesExercise(3,"swimming",5);
        CaloriesExercise cycle = new CaloriesExercise(4,"cycling",7);

        //getting display duration
        int excrec = 0;
        int totalcalories = 0;
        Intent exIntent = getIntent();
        totalcalories = exIntent.getIntExtra("totalcalories", 0);

        lbltotalcalories.setText(String.valueOf(totalcalories));
        excrec = briskwalk.calculateex(totalcalories);
        lblbwduration.setText(String.valueOf(excrec));
        excrec = yoga.calculateex(totalcalories);
        lblyogaduration.setText(String.valueOf(excrec));
        excrec = run.calculateex(totalcalories);
        lblrunduration.setText(String.valueOf(excrec));
        excrec = swim.calculateex(totalcalories);
        lblswimduration.setText(String.valueOf(excrec));
        excrec = cycle.calculateex(totalcalories);
        lblcycduration.setText(String.valueOf(excrec));

    }





}
