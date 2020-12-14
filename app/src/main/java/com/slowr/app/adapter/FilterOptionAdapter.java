package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.FiltersModel;

import java.util.List;

public class FilterOptionAdapter extends RecyclerView.Adapter<FilterOptionAdapter.MyViewHolder> {

    private List<FiltersModel> categoryListFilter;
    Callback callback;
    Context ctx;
    String checkedPos = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_filter_title;
        public TextView txt_filter_type_content;
        public LinearLayout layout_filter_type;

        public MyViewHolder(View view) {
            super(view);
            txt_filter_title = view.findViewById(R.id.txt_filter_title);
            layout_filter_type = view.findViewById(R.id.layout_filter_type);
            txt_filter_type_content = view.findViewById(R.id.txt_filter_type_content);
            layout_filter_type.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_filter_type:
                    callback.itemClick(getAdapterPosition());
                    break;

            }

        }
    }

    public FilterOptionAdapter(List<FiltersModel> _categoryList, Context ctx) {
//        this.categoryList = _categoryList;
        this.categoryListFilter = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_filter_item_popup, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try{
        FiltersModel movie = categoryListFilter.get(position);

        holder.txt_filter_title.setText(movie.getFilterTitle().trim());
        holder.txt_filter_type_content.setText(movie.getSelectedValue());
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
        void itemClick(int pos);


    }
}