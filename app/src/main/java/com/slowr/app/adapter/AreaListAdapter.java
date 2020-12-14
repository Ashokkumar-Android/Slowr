package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.AreaItemModel;

import java.util.ArrayList;
import java.util.List;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.MyViewHolder> implements Filterable {

    private List<AreaItemModel> categoryList;
    private List<AreaItemModel> categoryListFilter;
    Callback callback;
    Context ctx;
    String checkedPos = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_category_name;
        public LinearLayout layout_root;
        public RadioButton rb_select;

        public MyViewHolder(View view) {
            super(view);
            txt_category_name = view.findViewById(R.id.txt_category_name);
            layout_root = view.findViewById(R.id.layout_root);
            rb_select = view.findViewById(R.id.rb_select);
            layout_root.setOnClickListener(this);
            rb_select.setOnClickListener(this);
            rb_select.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    rb_select.setChecked(true);
                    callback.itemClick(categoryListFilter.get(getAdapterPosition()));
                    checkedPos = categoryListFilter.get(getAdapterPosition()).getAreaId();
                    break;
                case R.id.rb_select:
                    rb_select.setChecked(true);
                    callback.itemClick(categoryListFilter.get(getAdapterPosition()));
                    checkedPos = categoryListFilter.get(getAdapterPosition()).getAreaId();
                    break;
            }

        }
    }

    public AreaListAdapter(List<AreaItemModel> _categoryList, Context ctx) {
        this.categoryList = _categoryList;
        this.categoryListFilter = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_sub_category_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {

            AreaItemModel movie = categoryListFilter.get(position);

            holder.txt_category_name.setText(movie.getAreaName().trim());
            if (checkedPos.equals("")) {
                holder.rb_select.setChecked(false);
            } else {
                if (checkedPos.equals(movie.getAreaName())) {
                    holder.rb_select.setChecked(true);
                } else {
                    holder.rb_select.setChecked(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return categoryListFilter.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void itemClick(AreaItemModel model);


    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    categoryListFilter = categoryList;
                } else {
                    List<AreaItemModel> filteredList = new ArrayList<>();
                    for (AreaItemModel row : categoryList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAreaName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    categoryListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = categoryListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categoryListFilter = (ArrayList<AreaItemModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}