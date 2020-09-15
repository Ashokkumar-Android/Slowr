package com.slowr.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.slowr.app.R;
import com.slowr.app.models.SubCategoryChildModel;

import java.util.ArrayList;
import java.util.List;

public class ProductAutoCompleteAdapter extends ArrayAdapter<SubCategoryChildModel> {
    private Context context;
    private int resourceId;
    private List<SubCategoryChildModel> items, tempItems, suggestions;

    public ProductAutoCompleteAdapter(@NonNull Context context, int resourceId, ArrayList<SubCategoryChildModel> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            SubCategoryChildModel fruit = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.txt_category_name);
            name.setText(fruit.getChildCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public SubCategoryChildModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return fruitFilter;
    }

    private Filter fruitFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            SubCategoryChildModel fruit = (SubCategoryChildModel) resultValue;
            return fruit.getChildCategoryName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (SubCategoryChildModel fruit : tempItems) {
                    if (fruit.getChildCategoryName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(fruit);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<SubCategoryChildModel> tempValues = (ArrayList<SubCategoryChildModel>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (SubCategoryChildModel fruitObj : tempValues) {
                    add(fruitObj);
                }
                notifyDataSetChanged();
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}
