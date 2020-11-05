package com.slowr.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.slowr.app.R;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.models.BannerItemModel;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.MyViewHolder> {

    ArrayList<BannerItemModel> bannerList;
    Callback callback;
    Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_banner ;
        ImageView defult_one;
        TextView txt_prosperId ;
        TextView txt_banner_content;
        TextView txt_banner_price;
        TextView txt_banner_like;
        LinearLayout layout_root ;

        public MyViewHolder(View view) {
            super(view);
            img_banner = view.findViewById(R.id.img_banner);
            defult_one = view.findViewById(R.id.defult_one);
            txt_prosperId = view.findViewById(R.id.txt_prosperId);
            txt_banner_content = view.findViewById(R.id.txt_banner_content);
            txt_banner_price = view.findViewById(R.id.txt_banner_price);
            txt_banner_like = view.findViewById(R.id.txt_banner_like);
            layout_root = view.findViewById(R.id.layout_root);

            layout_root.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    callback.itemClick(bannerList.get(getAdapterPosition()));
                    break;
                case R.id.img_promote:
                    break;
                case R.id.img_share:
                    break;


            }

        }
    }

    public HomeBannerAdapter(ArrayList<BannerItemModel> _categoryList, Context ctx) {
        this.bannerList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_banner_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BannerItemModel bannerItemModel = bannerList.get(position);

        holder.txt_prosperId.setText(bannerItemModel.getProsperId());
        holder.txt_banner_content.setText(bannerItemModel.getBannerTitle());
        holder.txt_banner_price.setText(bannerItemModel.getDescription());
//        layout_root.setBackgroundColor(Color.parseColor(bannerItemModel.getColorCode()));
        Glide.with(ctx)
                .load(bannerItemModel.getBannerImage())
                .error(R.drawable.ic_default_horizontal)
                .placeholder(R.drawable.ic_default_horizontal)
                .into(holder.img_banner);
        Glide.with(ctx)
                .load(bannerItemModel.getBannerImage())
                .transform(new BlurTransformation())
                .error(R.drawable.ic_default_horizontal)
                .placeholder(R.drawable.ic_default_horizontal)
                .into(holder.defult_one);
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void itemClick(BannerItemModel model);

    }


}