package com.example.cz2006_hungryspoons.caloriesdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cz2006_hungryspoons.R;

import java.util.List;

public class CaloriesEntryAdapter extends ArrayAdapter<CaloriesEntry> {
    private Context mContext;
    private int mResource;

    public CaloriesEntryAdapter(Context context, int resource, List<CaloriesEntry> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String name = getItem(position).getName();
        String perServing = getItem(position).getPerServing();
        String totsCalorie = getItem(position).getTotCalories() + " kcal";
        Integer servingCount = getItem(position).getCount();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = convertView.findViewById(R.id.tvNameFood);
        TextView tvServings = convertView.findViewById(R.id.tvCPS);
        TextView tvTotsCalorie = convertView.findViewById(R.id.tvCals);
        TextView tvCount = convertView.findViewById(R.id.tvServings);

        tvName.setText(name);
        tvServings.setText(perServing);
        tvTotsCalorie.setText(totsCalorie);
        tvCount.setText(servingCount.toString() + " Count");

        return convertView;
    }
}
