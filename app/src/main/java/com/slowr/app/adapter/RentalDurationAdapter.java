package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;

import java.util.List;

public class RentalDurationAdapter extends RecyclerView.Adapter<RentalDurationAdapter.MyViewHolder> {

    private List<String> categoryListFilter;
    Callback callback;
    Context ctx;
    int checkedPos = 0;

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
                    callback.itemClick(getAdapterPosition());
                    checkedPos = getAdapterPosition();
                    break;
                case R.id.rb_select:
                    rb_select.setChecked(true);
                    callback.itemClick(getAdapterPosition());
                    checkedPos = getAdapterPosition();
                    break;
            }

        }
    }

    public RentalDurationAdapter(List<String> _categoryList, Context ctx) {
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

        holder.txt_category_name.setText(categoryListFilter.get(position));

        if (position == checkedPos) {
            holder.rb_select.setChecked(true);
        } else {
            holder.rb_select.setChecked(false);
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
        public void itemClick(int pos);


    }

    public void setPosValues(int pos) {
        checkedPos = pos;
        notifyDataSetChanged();
    }
}