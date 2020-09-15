package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.models.CategoryItemModel;

import java.util.List;

public class ProductCategoryListAdapter extends RecyclerView.Adapter<ProductCategoryListAdapter.MyViewHolder> {

    private List<CategoryItemModel> categoryList;
    Callback callback;
    Context ctx;

    //TODO:BALA(1/11/2018)
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_category_name;
        public LinearLayout layout_root;
        public ImageView img_category_icon;
        View view_left;
        View view_right;

        public MyViewHolder(View view) {
            super(view);
            txt_category_name = view.findViewById(R.id.txt_category_name);
            layout_root = view.findViewById(R.id.layout_root);
            view_left = view.findViewById(R.id.view_left);
            view_right = view.findViewById(R.id.view_right);
            img_category_icon = view.findViewById(R.id.img_category_icon);
            layout_root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    callback.itemClick(getAdapterPosition());
                    break;
            }

        }
    }

    public ProductCategoryListAdapter(List<CategoryItemModel> _categoryList, Context ctx) {
        this.categoryList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == categoryList.size() - 1) {
            holder.view_right.setVisibility(View.VISIBLE);
        } else {
            holder.view_right.setVisibility(View.GONE);
        }


        CategoryItemModel movie = categoryList.get(position);

        holder.txt_category_name.setText(movie.getName().trim());

        Glide.with(ctx)
                .load(movie.getMobile_icon())
                .placeholder(R.drawable.ic_default_horizontal)
                .error(R.drawable.ic_default_horizontal)
                .into(holder.img_category_icon);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
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
}