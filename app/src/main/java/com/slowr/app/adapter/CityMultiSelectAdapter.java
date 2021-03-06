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
import com.slowr.app.models.CityItemModel;

import java.util.ArrayList;
import java.util.List;

public class CityMultiSelectAdapter extends RecyclerView.Adapter<CityMultiSelectAdapter.MyViewHolder> implements Filterable {

    private List<CityItemModel> categoryList;
    private List<CityItemModel> categoryListFilter;
    //    Callback callback;
    Context ctx;
    String checkedPos = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_category_name;
        public TextView txt_price;
        public LinearLayout layout_root;
        public RadioButton rb_select;

        public MyViewHolder(View view) {
            super(view);
            txt_category_name = view.findViewById(R.id.txt_category_name);
            layout_root = view.findViewById(R.id.layout_root);
            rb_select = view.findViewById(R.id.rb_select);
            txt_price = view.findViewById(R.id.txt_price);
            layout_root.setOnClickListener(this);
            rb_select.setOnClickListener(this);
            txt_price.setVisibility(View.VISIBLE);
            rb_select.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    if (categoryListFilter.get(getAdapterPosition()).isSelect()) {
                        rb_select.setChecked(false);
                        categoryListFilter.get(getAdapterPosition()).setSelect(false);
                    } else {
                        rb_select.setChecked(true);
                        categoryListFilter.get(getAdapterPosition()).setSelect(true);
                    }
                    notifyItemChanged(getAdapterPosition());
//                    callback.itemClick(categoryListFilter.get(getAdapterPosition()));
//                    checkedPos = categoryListFilter.get(getAdapterPosition()).getCityId();
                    break;
                case R.id.rb_select:
                    if (categoryListFilter.get(getAdapterPosition()).isSelect()) {
                        rb_select.setChecked(false);
                        categoryListFilter.get(getAdapterPosition()).setSelect(false);
                    } else {
                        rb_select.setChecked(true);
                        categoryListFilter.get(getAdapterPosition()).setSelect(true);
                    }
                    notifyItemChanged(getAdapterPosition());
//                    callback.itemClick(categoryListFilter.get(getAdapterPosition()));
//                    checkedPos = categoryListFilter.get(getAdapterPosition()).getCityId();
                    break;
            }

        }
    }

    public CityMultiSelectAdapter(List<CityItemModel> _categoryList, Context ctx) {
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
        try{
        CityItemModel movie = categoryListFilter.get(position);

        holder.txt_category_name.setText(movie.getCityName().trim());
        holder.txt_price.setText(ctx.getString(R.string.txt_rupee_simpal)+" "+movie.getCityPrice()+"/Per Day");
        if (movie.isSelect()) {
            holder.rb_select.setChecked(true);
        } else {
            holder.rb_select.setChecked(false);
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

//    public void setCallback(Callback callback) {
//        this.callback = callback;
//    }
//
//    public interface Callback {
//        public void itemClick(CityItemModel model);
//
//
//    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    categoryListFilter = categoryList;
                } else {
                    List<CityItemModel> filteredList = new ArrayList<>();
                    for (CityItemModel row : categoryList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCityName().toLowerCase().contains(charString.toLowerCase())) {
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
                categoryListFilter = (ArrayList<CityItemModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}