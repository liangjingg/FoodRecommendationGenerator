package com.example.cz2006_hungryspoons.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cz2006_hungryspoons.MainActivity;
import com.example.cz2006_hungryspoons.R;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class UserAdd extends AppCompatActivity {


    Double printCal = 0.0;
    RadioGroup rdBtnGroup;
    RadioButton rdBtnType;

    String name;
    Double weight;
    Double height;
    Double heightToCal;
    String genderType;
    int selectedId;

    EditText etWeight;
    EditText etHeight;

    EditText tvName;

    TextView bmi;

    SharedPreferences mPrefs;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);


        bmi = findViewById(R.id.tvBMI);
        etWeight = findViewById(R.id.tvWeight);
        etHeight = findViewById(R.id.tvHeight);
        bmi.setVisibility(View.INVISIBLE);


//createUser();


        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (etWeight.getText().toString().trim().length() != 0) {

                    if (!etHeight.getText().toString().matches("")) {


                        weight = Double.parseDouble(etWeight.getText().toString()) ;
                        height = Double.parseDouble(etHeight.getText().toString()) ;
                                heightToCal = height/100;
                        printCal = weight / (heightToCal * heightToCal);


                        TextView bmi = findViewById(R.id.tvBMI);
                        bmi.setVisibility(View.VISIBLE);


                        double displayBMI = printCal;
                        DecimalFormat df = new DecimalFormat("#.##");
                        displayBMI = Double.valueOf(df.format(displayBMI));

                        bmi.setText(Double.toString(displayBMI));
                    }
                } else if (etHeight.getText().toString().trim().length() == 0) {
                    printCal = 0.0;

                    TextView bmi = findViewById(R.id.tvBMI);

                    bmi.setText(Double.toString(printCal));
                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        etHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

               if(etHeight.getText().toString().trim().length() != 0) {

                    if (!etWeight.getText().toString().matches("")) {


                        weight = Double.parseDouble(etWeight.getText().toString()) ;
                        height = Double.parseDouble(etHeight.getText().toString()) ;
                        heightToCal = height /100;
                        printCal = weight / (heightToCal * heightToCal);

                        TextView bmi = findViewById(R.id.tvBMI);
                        bmi.setVisibility(View.VISIBLE);

                        double displayBMI = printCal;
                        DecimalFormat df = new DecimalFormat("#.##");
                        displayBMI = Double.valueOf(df.format(displayBMI));

                        bmi.setText(Double.toString(displayBMI));
                    }
                } else if (etWeight.getText().toString().trim().length() == 0) {
                    printCal = 0.0;

                    TextView bmi = findViewById(R.id.tvBMI);

                    bmi.setText(Double.toString(printCal));
                }


            }
        });




    }


    public void createUser(View view) {




        tvName = findViewById(R.id.tvName);
         name = tvName.getText().toString();


        rdBtnGroup =  findViewById(R.id.genders);
        selectedId = rdBtnGroup.getCheckedRadioButtonId();
        rdBtnType = findViewById(selectedId);





        if(tvName.getText().length() == 0 || etWeight.getText().length() == 0 || etHeight.getText().length() == 0 || rdBtnGroup.getCheckedRadioButtonId() == -1) {

            Toast myToast = Toast.makeText(this, "You have missing fields", Toast.LENGTH_SHORT);
            myToast.show();
        }

        else {
            double bmi = printCal;
            genderType = rdBtnType.getText().toString();
            DecimalFormat df = new DecimalFormat("#.##");
            bmi = Double.valueOf(df.format(bmi));


            user = new User(name, genderType, weight, height, bmi);


            Gson gson = new Gson();

            String jsonStr = gson.toJson(user, User.class);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

            editor.putString("User", jsonStr);
            editor.apply();


            Intent intent = new Intent(UserAdd.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}












