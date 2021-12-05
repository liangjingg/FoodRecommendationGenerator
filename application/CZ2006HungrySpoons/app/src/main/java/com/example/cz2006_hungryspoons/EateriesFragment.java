package com.example.cz2006_hungryspoons;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import android.widget.Toast;

import com.example.cz2006_hungryspoons.eateries.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EateriesFragment extends Fragment {
    private String TAG = EateriesFragment.class.getSimpleName();
    private TextView tvName,tvAdress;
    private ImageView ivPhotoRef, ivSorry;
    private CheckBox hawker, healthier;
    private int num=0,maxNum=0;
    private boolean both=false;
    private ImageButton bOpen, bNext, bSearch;
    private String formatted_address,name,location,url,hurl;
    String[] arr = { "Raffles Place","Marina","Cecil","Tanjong Pagar","Chinatown","Tiong Bahru",
            "Alexandra","Queenstown","Mount Faber","Telok Blangah","Habourfront","Buona Vista",
            "Pasir Panjang","Clementi","Clarke Quay","City Hall","Bugis","Beach Road","Golden Mile",
            "Little India","Farrer Park","Orchard Road","River Valley","Bukit Timah","Holland","Balmoral",
            "Novena","Newton","Thomson","Toa Payoh","Serangoon","Balestier","Macpherson","Braddell",
            "Geylang","Paya Lebar","Sims","Joo Chiat","Marina Parade","Katong","Bedok","Upper East Coast",
            "Siglap","Changi","Flora","Loyang","Tampines","Pasir Ris","Punggol","Sengkang","Serangoon Gardens",
            "Ang Mo Kio","Bishan","Thomson","Upper Bukit Timah", "Ulu Pandan","Clementi Park","Boon Lay", "Jurong",
            "Tuas","Choa Chu Kang","Tengah", "Woodlands", "Admiralty","Upper Thomson","Mandai","Sembawang","Yishun",
            "Yio Chu Kang","Seletar"};
    private int count=0;
    AutoCompleteTextView autocomplete;
    ArrayList<HashMap<String, String>> foodPlaceList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eateries, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //Stop the bottom navigation from riding up
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //dropdown
        autocomplete = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, arr);
        autocomplete.setThreshold(1);
        autocomplete.setAdapter(adapter);

        bSearch=getView().findViewById(R.id.bSearch);
        tvName=getView().findViewById(R.id.tvName);
        tvAdress=getView().findViewById(R.id.tvAdress);
        ivPhotoRef=getView().findViewById(R.id.ivPhotoRef);
        ivSorry=getView().findViewById(R.id.ivSorry);
        bOpen=getView().findViewById(R.id.bOpen);
        bNext=getView().findViewById(R.id.bNext);
        hawker=getView().findViewById(R.id.hawker);
        healthier=getView().findViewById(R.id.healthier);

        bOpen.setVisibility(View.GONE);
        bNext.setVisibility(View.GONE);
        Arrays.sort(arr);
        //new GetFoodPlaces().execute();

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSearch();
            }
        });
        bOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonOpen();
            }
        });
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNext();
            }
        });
    }

    //LOCATION SEARCH
    public void locationSearch() {
        both = false;
        location = autocomplete.getText().toString();
        if (hawker.isChecked() && healthier.isChecked()) {
            both = true;
            url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=hawker+in+" + location + "&key=AIzaSyCXac8R60AtkW-42AHr9bS-d7uwfrT2ESg";
            hurl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=healthy+food+in+" + location + "&key=AIzaSyCXac8R60AtkW-42AHr9bS-d7uwfrT2ESg";
        } else if (hawker.isChecked()) {
            url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=hawker+in+" + location + "&key=AIzaSyCXac8R60AtkW-42AHr9bS-d7uwfrT2ESg";
        } else if (healthier.isChecked()) {
            url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=healthy+food+in+" + location + "&key=AIzaSyCXac8R60AtkW-42AHr9bS-d7uwfrT2ESg";
        } else {
            url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=food+in+" + location + "&key=AIzaSyCXac8R60AtkW-42AHr9bS-d7uwfrT2ESg";
        }
        new EateriesFragment.GetFoodPlaces().execute();
    }

    //googleAPI
    private class GetFoodPlaces extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray foodPlaces = jsonObj.getJSONArray("results");
                    foodPlaceList = new ArrayList<>();

                    // looping through All foodPlaces
                    for (int i = 0; i < foodPlaces.length(); i++) {
                        JSONObject fp =foodPlaces.getJSONObject(i);

                        //name
                        String name = fp.getString("name");
                        //address
                        String formatted_address = fp.getString("formatted_address");

                        // photos is JSON Array
                        JSONArray photos = fp.getJSONArray("photos");
                        JSONObject photo = photos.getJSONObject(0);
                        String photo_reference = photo.getString("photo_reference");

                        // tmp hash map for single contact
                        //will open in ascending order of alpha/num
                        HashMap<String, String> foodPlace = new HashMap<>();
                        // adding each child node to HashMap key => value
                        foodPlace.put("0name", name);
                        foodPlace.put("1formatted_address", formatted_address);
                        foodPlace.put("2photo_reference", photo_reference);
                        // adding contact to contact list
                        foodPlaceList.add(foodPlace);
                    }
                }catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            if (both == true){
                jsonStr = sh.makeServiceCall(hurl);

                Log.e(TAG, "Response from url: " + jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        // Getting JSON Array node
                        JSONArray foodPlaces = jsonObj.getJSONArray("results");

                        // looping through All foodPlaces
                        for (int i = 0; i < foodPlaces.length(); i++) {
                            JSONObject fp =foodPlaces.getJSONObject(i);

                            //name
                            String name = fp.getString("name");
                            //address
                            String formatted_address = fp.getString("formatted_address");

                            // photos is JSON Array
                            JSONArray photos = fp.getJSONArray("photos");
                            JSONObject photo = photos.getJSONObject(0);
                            String photo_reference = photo.getString("photo_reference");

                            // tmp hash map for single contact
                            //will open in ascending order of alpha/num
                            HashMap<String, String> foodPlace = new HashMap<>();
                            // adding each child node to HashMap key => value
                            foodPlace.put("0name", name);
                            foodPlace.put("1formatted_address", formatted_address);
                            foodPlace.put("2photo_reference", photo_reference);
                            // adding contact to contact list
                            foodPlaceList.add(foodPlace);
                        }
                    }catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }else {
                    Log.e(TAG, "Couldn't get json from server.");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //posting of info
            num=0;
            int j=0;
            super.onPostExecute(result);
            maxNum=foodPlaceList.size();
            ivSorry.setVisibility(View.GONE);
            ivPhotoRef.setVisibility(View.VISIBLE);
            bOpen.setVisibility(View.VISIBLE);
            bNext.setVisibility(View.VISIBLE);
            loopFoodPlace(num,j);
        }
    }

    //BUTTON
    public void buttonNext(){
        int j =0;
        num++;
        if (num<maxNum){
            loopFoodPlace(num,j);
        }else{
            tvName.setText("No more food places to display.");
            tvAdress.setText("");
            ivPhotoRef.setVisibility(View.GONE);
            ivSorry.setVisibility(View.VISIBLE);
            bOpen.setVisibility(View.GONE);
            bNext.setVisibility(View.GONE);
        }
    }
    public void loopFoodPlace(int num, int j){
        if (foodPlaceList.size()!=0){
            HashMap<String,String> foodplace=foodPlaceList.get(num);

            Map<String, String> reversedMap = new TreeMap<String, String>(foodplace);
            //then you just access the reversedMap however you like...
            for (Map.Entry entry : reversedMap.entrySet()) { //will open in ascending order of alpha/num
                Iterator<Map.Entry<String, String>> itr = reversedMap.entrySet().iterator();
                if(j==0){
                    name =entry.getValue().toString();
                    tvName.setText(name);
                }else if(j==1){
                    formatted_address=entry.getValue().toString();
                    tvAdress.setText(formatted_address);
                }else if(j==2){
                    String photo_reference=entry.getValue().toString();
                    String url="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photo_reference+"&key=AIzaSyCXac8R60AtkW-42AHr9bS-d7uwfrT2ESg";
                    Picasso.get().load(url).into(ivPhotoRef);
                }
                j++;
            }
        }else{
            //CHANGES START HERE, thanks bby <3
            tvName.setText("Sorry, no food places to for this location.");
            //CHANGES ENDS HERE
            tvAdress.setText("");
            ivPhotoRef.setVisibility(View.GONE);
            ivSorry.setVisibility(View.VISIBLE);
            bOpen.setVisibility(View.GONE);
            bNext.setVisibility(View.GONE);
        }
    }

    //Button
    public void buttonOpen() {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+name+","+formatted_address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}

