package com.example.cz2006_hungryspoons.caloriesdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cz2006_hungryspoons.R;

import java.util.ArrayList;
import java.util.List;

public class CaloriesFoodDataAdapter extends ArrayAdapter<CaloriesFood> {
private ArrayList<CaloriesFood> foodListFull;

public CaloriesFoodDataAdapter(Context context, List<CaloriesFood> FoodList) {
        super(context, 0, FoodList);
        foodListFull = new ArrayList<>(FoodList);
        }

@Override
public Filter getFilter() {
        return foodFilter;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(
        R.layout.activity_autocomplete_fooddata, parent, false
        );
        }
        TextView tv1 = convertView.findViewById(R.id.tvServingAC);
        TextView tv2 = convertView.findViewById(R.id.tvFoodNameAC);

        CaloriesFood foodItem = getItem(position);

        if (foodItem != null) {
        tv2.setText(foodItem.getFoodName());
        tv1.setText(foodItem.getServing());
        }
        return convertView;
        }

private Filter foodFilter = new Filter() {
@Override
protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        List<CaloriesFood> suggestions = new ArrayList<>();

        if (constraint == null || constraint.length() == 0) {
        suggestions.addAll(foodListFull);
        } else {
        String filterPattern = constraint.toString().toLowerCase().trim();

        for (CaloriesFood item : foodListFull) {
        if (item.getFoodName().toLowerCase().contains(filterPattern)) {
        suggestions.add(item);
        }
        }
        }
        results.values = suggestions;
        results.count = suggestions.size();

        return results;
        }

@Override
protected void publishResults(CharSequence constraint, FilterResults results) {
        clear();
        addAll((List) results.values);
        notifyDataSetChanged();
        }

@Override
public CharSequence convertResultToString(Object resultValue) {
        return ((CaloriesFood) resultValue).getFoodName();
        }
        };

        }
