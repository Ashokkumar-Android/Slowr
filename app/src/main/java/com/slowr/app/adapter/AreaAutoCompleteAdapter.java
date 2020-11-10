package com.slowr.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.slowr.app.R;
import com.slowr.app.models.AreaItemModel;

import java.util.ArrayList;
import java.util.List;

public class AreaAutoCompleteAdapter extends ArrayAdapter<AreaItemModel> {
    private Context context;
    private int resourceId;
    Callback callback;
    private List<AreaItemModel> items, tempItems, suggestions;

    public AreaAutoCompleteAdapter(@NonNull Context context, int resourceId, ArrayList<AreaItemModel> items) {
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
            AreaItemModel fruit = getItem(position);
            TextView name = view.findViewById(R.id.txt_category_name);
            RadioButton rb_select = view.findViewById(R.id.rb_select);
            rb_select.setVisibility(View.GONE);
            name.setText(fruit.getAreaName().trim());
            LinearLayout linearLayout = view.findViewById(R.id.layout_root);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.itemClick(fruit);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public AreaItemModel getItem(int position) {
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

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void itemClick(AreaItemModel model);


    }

    @NonNull
    @Override
    public Filter getFilter() {
        return fruitFilter;
    }

    private Filter fruitFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            AreaItemModel fruit = (AreaItemModel) resultValue;
            return fruit.getAreaName().trim();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (AreaItemModel fruit : tempItems) {
                    if (fruit.getAreaName().toLowerCase().trim().startsWith(charSequence.toString().toLowerCase().trim())) {
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
            ArrayList<AreaItemModel> tempValues = (ArrayList<AreaItemModel>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (AreaItemModel fruitObj : tempValues) {
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
