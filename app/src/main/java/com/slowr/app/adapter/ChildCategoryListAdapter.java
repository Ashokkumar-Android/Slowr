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
import com.slowr.app.models.SubCategoryChildModel;

import java.util.ArrayList;
import java.util.List;

public class ChildCategoryListAdapter extends RecyclerView.Adapter<ChildCategoryListAdapter.MyViewHolder> implements Filterable {

    private List<SubCategoryChildModel> categoryList;
    private List<SubCategoryChildModel> categoryListFilter;
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
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    rb_select.setChecked(true);
                    callback.itemClick(categoryListFilter.get(getAdapterPosition()));
                    checkedPos = categoryListFilter.get(getAdapterPosition()).getChildCategoryId();
                    break;
                case R.id.rb_select:
                    rb_select.setChecked(true);
                    callback.itemClick(categoryListFilter.get(getAdapterPosition()));
                    checkedPos = categoryListFilter.get(getAdapterPosition()).getChildCategoryId();
                    break;
            }

        }
    }

    public ChildCategoryListAdapter(List<SubCategoryChildModel> _categoryList, Context ctx) {
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
        SubCategoryChildModel movie = categoryListFilter.get(position);

        holder.txt_category_name.setText(movie.getChildCategoryName().trim());
        if (checkedPos.equals("")) {
            holder.rb_select.setChecked(false);
        } else {
            if (checkedPos.equals(movie.getChildCategoryId())) {
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
        void itemClick(SubCategoryChildModel model);
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
                    List<SubCategoryChildModel> filteredList = new ArrayList<>();
                    for (SubCategoryChildModel row : categoryList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getChildCategoryName().toLowerCase().contains(charString.toLowerCase())) {
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
                categoryListFilter = (ArrayList<SubCategoryChildModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}