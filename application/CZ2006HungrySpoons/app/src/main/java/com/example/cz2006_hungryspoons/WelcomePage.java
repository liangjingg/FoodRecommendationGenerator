package com.example.cz2006_hungryspoons;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import com.example.cz2006_hungryspoons.user.User;
import com.example.cz2006_hungryspoons.user.UserAdd;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class WelcomePage extends AppCompatActivity {

    Button toMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_welcome_page);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        String string = sharedPreferences.getString("User", "");
        if(string.contains("userName")){
            toMain = findViewById(R.id.enterBtn);
            toMain.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }
            }, 2500);

        }

        else{
            toMain = findViewById(R.id.enterBtn);
            toMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WelcomePage.this, UserAdd.class);
                    startActivity(intent);
                    finish();
                }
            });

        }


      /*  if(string == null && string.equals("")){



        }
        else {

           // Gson gson = new Gson();
            //User user = gson.fromJson(string, User.class);
            toMain = findViewById(R.id.enterBtn);
            toMain.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }
            }, 2500);

        } */











    }
}









