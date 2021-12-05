package com.example.cz2006_hungryspoons.parks;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cz2006_hungryspoons.R;
import com.squareup.picasso.Picasso;

public class CustomListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;

    //to store the animal images
    private final String[] photoUrl;

    //to store the list of countries
    private final String[] parkName;

    //to store the list of countries
    private final String[] parkAddress;

    public CustomListAdapter(Activity context, String[] parkName, String[] parkAddress, String[] photoUrl){

        super(context, R.layout.listview_row , parkName);
        this.context=context;
        this.parkName = parkName;
        this.parkAddress = parkAddress;
        this.photoUrl = photoUrl;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
        TextView tvAdress = (TextView) rowView.findViewById(R.id.tvAdress);
        ImageView ivPhotoRef = (ImageView) rowView.findViewById(R.id.ivPhotoRef);

        //this code sets the values of the objects to values from the arrays
        tvName.setText(parkName[position]);
        tvAdress.setText(parkAddress[position]);
        Picasso.get().load(photoUrl[position]).into(ivPhotoRef);
        //imageView.setImageResource(imageIDarray[position]);

        return rowView;

    };
}
