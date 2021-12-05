package com.example.cz2006_hungryspoons.caloriesdiary;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cz2006_hungryspoons.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CaloriesFragment extends Fragment {

    private ListView list;
    private TabLayout tabLayout;

    private Button recExBtn;

    private String sDate;
    private CalendarView mCalendarView;

    private CaloriesEntryAdapter adapter;
    private TextView missingTV;
    private String compareDate;
    private String storedFoodsStr = null;
    private List<CaloriesEntry> userFoodObjects = null;
    private List<CaloriesEntry> secondList;
    private String mealTypeFilter = "Breakfast";


    private String dateToFilter;
    private double exerciseCalories = 0;
    SharedPreferences pref;


    private static final String userFoodData = "userFoodDataFile.txt";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_addfood, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_calories, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean id = pref.getBoolean("lockedState", false);

        if (id == false) {
            putNewData();
        }

            tabLayout = getView().findViewById(R.id.tabbed);
            tabLayout.getTabAt(0).select();

            SimpleDateFormat formatter = new SimpleDateFormat("d/MM/YYYY");
            Date date = new Date();
            sDate = formatter.format(date);
            System.out.println("date " + sDate);
            compareDate = sDate;
            dateToFilter = sDate;

            refreshData();
            tabbedAction();
            filterForListView(sDate);

            recExBtn = getView().findViewById(R.id.recExBtn);

            mCalendarView = getView().findViewById(R.id.calendarView2);
            mCalendarView.setMaxDate(System.currentTimeMillis());
            mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView mCalendarView, int year, int month, int dayOfMonth) {
                    String date = (dayOfMonth) + "/" + (month + 1) + "/" + year;

                    System.out.println("date " + sDate);
                    System.out.println("compare Date " + compareDate);
                    System.out.println("cal date " + date);
                    dateToFilter = date;

                    System.out.println(date);
                    System.out.println(sDate);
                    tabLayout = getView().findViewById(R.id.tabbed);
                    tabLayout.getTabAt(0).select();

                    //If the system date and the date you've chosen is the same!
                    if (sDate.equals(date)) {
                        setHasOptionsMenu(true);
                        recExBtn.setVisibility(View.VISIBLE);
                        filterForListView(dateToFilter);

                    }
                    //If different dates : this is more towards my button visibility to add calories and all x.
                    else {
                        setHasOptionsMenu(false);
                        recExBtn.setVisibility(View.INVISIBLE);
                        filterForListView(dateToFilter);


                    }
                }
            });


            //set up to next page first
            Button btnexerciser = getView().findViewById(R.id.recExBtn);
            btnexerciser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int totalcalories = (int) exerciseCalories;
                    Intent extIntent = new Intent(getActivity(), CaloriesExerciseView.class);
                    extIntent.putExtra("totalcalories", totalcalories);
                    startActivity(extIntent);
                }
            });
        }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFood:

                Intent intent = new Intent(getActivity(), CaloriesAddFood.class);
                startActivity(intent);

                //Toast.makeText(getContext(), "Action Refresh selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshData() {
        storedFoodsStr = readData();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CaloriesEntry>>() {
        }.getType();

        missingTV = getView().findViewById(R.id.missingTV);
        list = getView().findViewById(R.id.listViewTry);

        if (storedFoodsStr != null) {
            storedFoodsStr = new StringBuilder(storedFoodsStr).insert(0, "[").toString();
            storedFoodsStr = new StringBuilder(storedFoodsStr).insert(storedFoodsStr.length(), "]").toString();

            userFoodObjects = gson.fromJson(storedFoodsStr, listType);
            adapter = new CaloriesEntryAdapter(getActivity(), R.layout.activity_calories_entry, userFoodObjects);
            list.setAdapter(adapter);
            missingTV.setVisibility(View.GONE);
        } else {
            missingTV.setVisibility(View.VISIBLE);
        }
    }

    public String readData() {
        String data = null;
        try {
            InputStream is = getActivity().openFileInput(userFoodData);
            if (is != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                data = stringBuilder.toString();
                is.close();
            }
        } catch (FileNotFoundException e) {
            Log.d("Unable to find found: ", e.toString());
        } catch (IOException e) {
            Log.d("IOE readData(): ", e.toString());
        }
        return data;
    }

    public void filterForListView(String filterDate) {
        secondList = new ArrayList<>();

        if (userFoodObjects != null && !userFoodObjects.isEmpty()) {
            for (CaloriesEntry a : userFoodObjects) {
                if (a.getMealType().equals(mealTypeFilter) && a.getDate().equals(filterDate)) {
                    secondList.add(a);
                }
            }
            missingTV = getView().findViewById(R.id.missingTV);

            if (secondList != null && !secondList.isEmpty()) {
                adapter = new CaloriesEntryAdapter(getActivity(), R.layout.activity_calories_entry, secondList);
                list.setAdapter(adapter);
                missingTV.setVisibility(View.GONE);
                getCaloriesForEx();
            } else if (secondList != null && secondList.isEmpty()) {
                adapter = new CaloriesEntryAdapter(getActivity(), R.layout.activity_calories_entry, secondList);
                list.setAdapter(adapter);
                missingTV.setText("No " + mealTypeFilter +" entry.");
                missingTV.setVisibility(View.VISIBLE);
                exerciseCalories = 0;
               // Toast myToast = Toast.makeText(getActivity(), "No " + mealTypeFilter + " data found.", Toast.LENGTH_SHORT);
                //myToast.show();
            }
        }
    }

    public void tabbedAction() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    mealTypeFilter = "Breakfast";
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    mealTypeFilter = "Lunch";
                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    mealTypeFilter = "Dinner";
                }

                System.out.println("check this : " + dateToFilter);
                filterForListView(dateToFilter);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    mealTypeFilter = "Breakfast";
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    mealTypeFilter = "Lunch";
                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    mealTypeFilter = "Dinner";
                }
                System.out.println("check this : " + dateToFilter);
                filterForListView(dateToFilter);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.getTabAt(0).select();

        SimpleDateFormat formatter = new SimpleDateFormat("d/MM/YYYY");
        Date date = new Date();
        sDate = formatter.format(date);
        compareDate = sDate;

        exerciseCalories = 0;
        refreshData();
        tabbedAction();
        filterForListView(sDate);
    }

    public void getCaloriesForEx() {
        exerciseCalories = 0;
        for (int i = 0; i < secondList.size(); i++) {
            exerciseCalories += secondList.get(i).getTotCalories();
        }
    }


    public void writeData(String food) {
        if (fileExist()) {
            try {
                FileOutputStream fos = new FileOutputStream(getActivity().getFilesDir().getPath() + "/" + userFoodData, true);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.append(",");
                osw.append(food);
                osw.flush();
                osw.close();
            } catch (FileNotFoundException e) {
                Log.e("Exception", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("Exception", "Unable to append to file: " + e.toString());
            }
        } else {
            try {
                FileOutputStream fos = new FileOutputStream(getActivity().getFilesDir().getPath() + "/" + userFoodData, false);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.write(food);
                osw.flush();
                osw.close();
                Log.i("Information", "Successfully saved data!");
            } catch (IOException e) {
                Log.e("Exception", "Unable to create file: " + e.toString());
            }
        }
    }

    private boolean fileExist() {
        File file = getActivity().getBaseContext().getFileStreamPath(userFoodData);
//        Log.d("File Exists: ", String.valueOf(file.exists()));
        return file.exists();
    }


    //Dummy Data
    public void putNewData(){





        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("lockedState", true);
        edt.commit();


        Gson gson = new Gson();


        CaloriesEntry e1 = new CaloriesEntry("2/11/2019", "Breakfast", "2-in-1 coffee powder, with sugar", "15g", 2, 30);
        CaloriesEntry e2 = new CaloriesEntry("2/11/2019", "Breakfast", "Big Breakfast, McDonalds", "Whole (252g)", 1, 252);
        CaloriesEntry e3 = new CaloriesEntry("4/11/2019", "Lunch", "Big-eye scad", "Whole (310g)", 2, 620);
        CaloriesEntry e4 = new CaloriesEntry("5/11/2019", "Breakfast", "Biscuit, chocolate-coated, cream filled", "Piece (16g)", 6, 96);
        CaloriesEntry e5 = new CaloriesEntry("5/11/2019", "Breakfast", "2-in-1 coffee powder, with sugar", "15g", 2, 30);
        CaloriesEntry e6 = new CaloriesEntry("7/11/2019", "Dinner", "Boiled kampung chicken", "One quarter (148g)", 1, 148);
        CaloriesEntry e7 = new CaloriesEntry("7/11/2019", "Dinner", "Crabmeat", "1 portion (45g)", 2, 90);
        CaloriesEntry e8 = new CaloriesEntry("9/11/2019", "Lunch", "Double Cheeseburger, Burger King", "Whole (225g)", 1, 225);
        CaloriesEntry e9 = new CaloriesEntry("9/11/2019", "Lunch", "Dry chicken feet noodles", "Plate-23cm (409g)", 1, 409);
        CaloriesEntry e10 = new CaloriesEntry("9/11/2019", "Breakfast", "2-in-1 coffee powder, with sugar", "15g", 2, 30);


        String sE1 = gson.toJson(e1);
        String sE2 = gson.toJson(e2);
        String sE3 = gson.toJson(e3);
        String sE4 = gson.toJson(e4);
        String sE5 = gson.toJson(e5);
        String sE6 = gson.toJson(e6);
        String sE7 = gson.toJson(e7);
        String sE8 = gson.toJson(e8);
        String sE9 = gson.toJson(e9);
        String sE10 = gson.toJson(e10);
        // String sE11 = gson.toJson(e1);
        // String sE12 = gson.toJson(e1);









        writeData(sE1);
        writeData(sE2);
        writeData(sE3);
        writeData(sE4);
        writeData(sE5);
        writeData(sE6);
        writeData(sE7);
        writeData(sE8);
        writeData(sE9);
        writeData(sE10);








    }




}


