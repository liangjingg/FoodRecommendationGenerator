package com.example.cz2006_hungryspoons.caloriesdiary;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cz2006_hungryspoons.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class CaloriesAddFood extends AppCompatActivity {


    //Object arrayList.
    ArrayList<CaloriesFood> foodList;

    //AutoComplete for choosing the food.
    AutoCompleteTextView autoTV;

    //used to calculate total servings.
    double calorieDouble;

    //This is calorie per 1 serving.
    String perCalorie;

    //count of servings.
    int count = 1;

    //Since JSON data is(180g) -> perCal is used as the result of using a regex expression.
    String perCal;

    String mealType = null;
    String mealName = null;

    RadioGroup rdBtnGroup;
    RadioButton rdBtnType;

    private static final String userFoodData = "userFoodDataFile.txt";
    private static final String foodListData = "FoodData.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_addfood);

        //Call the JSON method.
        getJSON();


        //Fill the Autofill with these
        autoTV = findViewById(R.id.autoTV);
        CaloriesFoodDataAdapter adapter = new CaloriesFoodDataAdapter(this, foodList);
        autoTV.setAdapter(adapter);

        //When autofill items are called.
        autoTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mealName = autoTV.getText().toString();

                //Set how many calories in the chosen food
                CaloriesFood food = (CaloriesFood) parent.getItemAtPosition(position);
                EditText calorieServing = findViewById(R.id.caloriesEV);
                perCalorie = food.getServing();

                //Set the Calorie in the EditText
                calorieServing.setText(perCalorie);

                //RegEx expression to choose only the last parentheses as well as dropping the 'g'
                Pattern p = Pattern.compile("\\(([^)]*)\\)[^(]*$");
                Matcher m = p.matcher(perCalorie);

                //if there is a match in the regex.
                while (m.find()) {
                    perCal = m.group(1).replaceAll("-?[^\\d.]", "");

                    calorieDouble = Double.parseDouble(perCal);

                    //Set it to the TextEdit -> will delete just wanted to see. **DELETE**
                    TextView calorieText = findViewById(R.id.tvCal);
                    calorieText.setText(Double.toString(calorieDouble));
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(parent.getApplicationWindowToken(), 0);
            }
        });
        showAll();
    }

    //Show all when AutoFillBox is touched.
    public void showAll() {
        autoTV.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                autoTV.showDropDown();
                return false;
            }
        });
    }

    public void getJSON() {
        foodList = new ArrayList<>();
        String json;

        try {
            InputStream is = getAssets().open(foodListData);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);

            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String fName = obj.getString("Food Name");
                    String fGroup = obj.getString("Food Group");
                    String fSubGroup = obj.getString("Food Sub Group");
                    String fServings = obj.getString("Per Serving Household Measure");

                    CaloriesFood food = new CaloriesFood(fName, fGroup, fSubGroup, fServings);

                    foodList.add(food);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addServings(View view) {


        if (!TextUtils.isEmpty(mealName)) {
            count++;
            calorieDouble = Double.parseDouble(perCal) * count;
            //This is just to display that the calories have changed! ** WILL DELETE**
            TextView calorieText = findViewById(R.id.tvCal);
            calorieText.setText(String.valueOf(calorieDouble));
            //Set the count in the EditText. ->Which is uneditable.
            EditText servingCount = findViewById(R.id.servingCount);
            servingCount.setText(String.valueOf(count));
        } else {
            Toast myToast = Toast.makeText(this, "Please Select Food", Toast.LENGTH_SHORT);
            myToast.show();
        }
    }

    public void subServing(View view) {
        //Serving count cannot be less than 1;

        if (!TextUtils.isEmpty(mealName)) {
            if (count > 1) {
                count--;
                //This is to set the number between the - and + signs.
                EditText servingCount = findViewById(R.id.servingCount);
                servingCount.setText(String.valueOf(count));
                //This is just to see the changes in the calories when - and + are clicked. **DELETE**
                TextView calorieText = findViewById(R.id.tvCal);
                calorieDouble = Double.parseDouble(perCal) * count;
                calorieText.setText(String.valueOf(calorieDouble));
            } else {
                //Serving count cannot be lesser than 0, toast appears when user clicks - when it's already 1;
                Toast myToast = Toast.makeText(this, "Servings cannot be less than 0", Toast.LENGTH_SHORT);
                myToast.show();
            }
        } else {
            Toast myToast = Toast.makeText(this, "Please Select Food", Toast.LENGTH_SHORT);
            myToast.show();
        }
    }

    //Save to whatever.
    public void submitBtn(View view) {
        rdBtnGroup = findViewById(R.id.mealTypes);

        int selectedRdBtn = rdBtnGroup.getCheckedRadioButtonId();
        rdBtnType = findViewById(selectedRdBtn);

        if (rdBtnGroup.getCheckedRadioButtonId() == -1 || mealName.isEmpty()) {
            Toast myToast = Toast.makeText(this, "You're missing some fields", Toast.LENGTH_SHORT);
            myToast.show();
        } else {
            mealType = rdBtnType.getText().toString();

            SimpleDateFormat formatter = new SimpleDateFormat("d/MM/yyyy");
            String today = formatter.format(new Date());

            CaloriesEntry sf = new CaloriesEntry(today, mealType, mealName, perCalorie, count, calorieDouble);


            Gson gson = new Gson();
            String foodData = gson.toJson(sf);


            writeData(foodData);

            Toast.makeText(this, "Successfully saved entry!", Toast.LENGTH_SHORT).show();

            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 500);

//            finish();
        }
//        File directory = getFilesDir();
//        Log.d("File", directory.getPath());
//        File file = new File(directory, userFoodData);
//        boolean deleted = file.delete();
//        Log.d("DELETE", String.valueOf(deleted));
    }

    public void writeData(String food) {
        if (fileExist()) {
            try {
                FileOutputStream fos = new FileOutputStream(getFilesDir().getPath() + "/" + userFoodData, true);
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
                FileOutputStream fos = new FileOutputStream(getFilesDir().getPath() + "/" + userFoodData, false);
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
        File file = getBaseContext().getFileStreamPath(userFoodData);
//        Log.d("File Exists: ", String.valueOf(file.exists()));
        return file.exists();
    }


}
