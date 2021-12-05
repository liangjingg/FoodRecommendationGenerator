package com.example.cz2006_hungryspoons.user;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cz2006_hungryspoons.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class UserFragment extends Fragment {


    EditText tvName;
    EditText tvWeight;
    EditText tvHeight;
    TextView tvBMI;


    Button editButton;
    Button saveButton;

    String name;
    Double weight;
    Double height;



    String mGender;

    int selectedId;
    RadioButton rdbF;
    RadioButton rdbM;
    RadioButton rdBtype;
    RadioGroup rgOption;
    SharedPreferences mPrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


       getTVET();

       saveButton = getView().findViewById(R.id.buttonSubmit);
       editButton = getView().findViewById(R.id.buttonEdit);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String string = sharedPreferences.getString("User", "N/A");

        System.out.println(string);

        Gson gson = new Gson();
        User user = gson.fromJson(string, User.class);
        System.out.println(user.getUserName());
        tvName.setText(user.getUserName());
        tvWeight.setText(Double.toString(user.getUserWeight()));
        tvHeight.setText(Double.toString(user.getUserHeight()));
        tvBMI.setText(Double.toString(user.getUserBMI()));

        rgOption = getView().findViewById(R.id.genders);
        mGender = user.getGender();
        if(mGender.equals("Male"))
            rgOption.check(R.id.rdBtnM);
        else
            rgOption.check(R.id.rdBtnF);




        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTVET();

                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);

                tvName.setEnabled(true);
                // tvBMI.setEnabled(true);
                tvWeight.setEnabled(true);
                tvHeight.setEnabled(true);
                rgOption.setEnabled(true);
            }
        });

       saveButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (tvName.getText().length() == 0 || tvWeight.getText().length() == 0 || tvHeight.getText().length() == 0 || rgOption.getCheckedRadioButtonId() == -1) {

                   Toast myToast = Toast.makeText(getActivity(), "You have missing fields", Toast.LENGTH_SHORT);
                   myToast.show();
               } else {


                   name = tvName.getText().toString();
                   weight = Double.parseDouble(tvWeight.getText().toString()) ;
                   height = Double.parseDouble(tvHeight.getText().toString());

                   Double weightToCal = weight;
                   Double heightToCal = height/100;
                   double printCal = weightToCal / (heightToCal * heightToCal);

                   double displayBMI = printCal;
                   DecimalFormat df = new DecimalFormat("#.##");
                   displayBMI = Double.valueOf(df.format(displayBMI));


                  tvBMI.setText(Double.toString(displayBMI));



                   User user = new User(name, mGender, weight, height, displayBMI);


                   Gson gson = new Gson();

                   String jsonStr = gson.toJson(user, User.class);
                   SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

                   editor.putString("User", jsonStr);
                   editor.apply();




                   tvName.setEnabled(false);
                   tvWeight.setEnabled(false);
                   tvHeight.setEnabled(false);

                   saveButton.setVisibility(View.INVISIBLE);
                   editButton.setVisibility(View.VISIBLE);

               }
           }
       });
    }



    public void getTVET(){
        tvName = getView().findViewById(R.id.tvName);
        tvWeight = getView().findViewById(R.id.tvWeight);
        tvHeight = getView().findViewById(R.id.tvHeight);

        rdBtype = getView().findViewById(selectedId);
        tvBMI = getView().findViewById(R.id.tvBMI);



        name = tvName.getText().toString();
      //  weight = Double.valueOf(tvWeight.getText().toString());
       // height = Double.valueOf(tvHeight.getText().toString());


    }

}

