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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cz2006_hungryspoons.eateries.HttpHandler;
import com.example.cz2006_hungryspoons.parks.CustomListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ParkFragment extends Fragment {
    private String TAG = ParkFragment.class.getSimpleName();
    private ImageButton bSearch;
    private ImageView ivSorry;
    private TextView tvSorry;
    private String formatted_address,name,location,url;
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
    ArrayList<HashMap<String, String>> parkPlaceList;
    private ListView list;
    String[] photoUrl,parkName,parkAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_park, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //dropdown
        autocomplete = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, arr);
        autocomplete.setThreshold(1);
        autocomplete.setAdapter(adapter);
        ivSorry=getView().findViewById(R.id.ivSorry);
        bSearch=getView().findViewById(R.id.bSearch);
        tvSorry=getView().findViewById(R.id.tvSorry);

        Arrays.sort(arr);
        list=getView().findViewById(R.id.list);
        //new GetFoodPlaces().execute();


        autocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSearch();
            }
        });
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationSearch();
            }
        });
        //ListView list1 = getListView();
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {

                        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+parkName[position]+","+parkAddress[position]);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }
        );
        /*bOpen.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }

    public void locationSearch() {
        location = autocomplete.getText().toString();
        url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=park+in+" + location + "&key=AIzaSyCXac8R60AtkW-42AHr9bS-d7uwfrT2ESg";
        new ParkFragment.GetParkPlaces().execute();
    }

    //googleAPI
    private class GetParkPlaces extends AsyncTask<Void, Void, Void> {
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
                    JSONArray parkPlaces = jsonObj.getJSONArray("results");
                    parkPlaceList = new ArrayList<>();

                    // looping through All foodPlaces
                    for (int i = 0; i < parkPlaces.length(); i++) {
                        JSONObject pp =parkPlaces.getJSONObject(i);

                        //name
                        String name = pp.getString("name");
                        //address
                        String formatted_address = pp.getString("formatted_address");

                        // photos is JSON Array
                        JSONArray photos = pp.getJSONArray("photos");
                        JSONObject photo = photos.getJSONObject(0);
                        String photo_reference = photo.getString("photo_reference");

                        // tmp hash map for single contact
                        //will open in ascending order of alpha/num
                        HashMap<String, String> parkPlace = new HashMap<>();
                        // adding each child node to HashMap key => value
                        parkPlace.put("0name", name);
                        parkPlace.put("1formatted_address", formatted_address);
                        parkPlace.put("2photo_reference", photo_reference);
                        // adding contact to contact list
                        parkPlaceList.add(parkPlace);
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (parkPlaceList.size()==0){
                //tvName.setText("No food places to display.");
                //tvAdress.setText("");
                ivSorry.setVisibility(View.VISIBLE);
                tvSorry.setVisibility(View.VISIBLE);
            }else{
                ivSorry.setVisibility(View.GONE);
                tvSorry.setVisibility(View.GONE);
            }
            //posting of info
            //num=0;
            super.onPostExecute(result);
            //maxNum=parkPlaceList.size();
            loopFoodPlace();
            //CustomListAdapter whatever = new CustomListAdapter(this, parkName, parkAddress, photoUrl);
            CustomListAdapter whatever = new CustomListAdapter(getActivity(), parkName, parkAddress, photoUrl);
            list = (ListView)getView().findViewById(R.id.list);
            list.setAdapter(whatever);
        }
    }
    /*public void buttonNext(){
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
    }*/
    public void loopFoodPlace(){
        int j=0;
        parkName = new String[parkPlaceList.size()];
        parkAddress = new String[parkPlaceList.size()];
        photoUrl = new String[parkPlaceList.size()];
        for(int num=0;num<parkPlaceList.size();num++){
            j=0;
            if (parkPlaceList.size()!=0){
                HashMap<String,String> parkplace=parkPlaceList.get(num);

                Map<String, String> reversedMap = new TreeMap<String, String>(parkplace);
                //then you just access the reversedMap however you like...
                for (Map.Entry entry : reversedMap.entrySet()) { //will open in ascending order of alpha/num
                    Iterator<Map.Entry<String, String>> itr = reversedMap.entrySet().iterator();
                    if(j==0){
                        name =entry.getValue().toString();
                        parkName[num]=name;
                        //tvName.setText(name);
                    }else if(j==1){
                        formatted_address=entry.getValue().toString();
                        parkAddress[num]=formatted_address;
                        //tvAdress.setText(formatted_address);
                    }else if(j==2){
                        String photo_reference=entry.getValue().toString();
                        String url="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photo_reference+"&key=AIzaSyCXac8R60AtkW-42AHr9bS-d7uwfrT2ESg";
                        photoUrl[num]=url;
                        //Picasso.get().load(url).into(ivPhotoRef);
                    }
                    j++;
                }
            }
        }
    }
    /*public void buttonOpen() {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+name+","+formatted_address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }*/
}

