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
import com.slowr.app.components.carouselview.CarouselAdapter;
import com.slowr.app.models.BannerItemModel;
import com.slowr.app.utils.Function;

import java.util.ArrayList;

public class HomeBannerAdapter extends CarouselAdapter<HomeBannerAdapter.MyViewHolder> {

    ArrayList<BannerItemModel> bannerList;
    Callback callback;
    Context ctx;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_banner;
        ImageView defult_one;
        TextView txt_prosperId;
        TextView txt_banner_content;
        TextView txt_banner_price;
        TextView txt_banner_like;
        LinearLayout layout_root;
        ImageView img_default_banner;

        public MyViewHolder(View view) {
            super(view);
            img_banner = view.findViewById(R.id.img_banner);
            defult_one = view.findViewById(R.id.defult_one);
            txt_prosperId = view.findViewById(R.id.txt_prosperId);
            txt_banner_content = view.findViewById(R.id.txt_banner_content);
            txt_banner_price = view.findViewById(R.id.txt_banner_price);
            txt_banner_like = view.findViewById(R.id.txt_banner_like);
            layout_root = view.findViewById(R.id.layout_root);
            img_default_banner = view.findViewById(R.id.img_default_banner);

            layout_root.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.layout_root:
//
//                    break;
//                case R.id.img_promote:
//                    break;
//                case R.id.img_share:
//                    break;
//
//
//            }

        }
    }

    public HomeBannerAdapter(ArrayList<BannerItemModel> _categoryList, Context ctx) {
        this.bannerList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreatePageViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_banner_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindPageViewHolder(MyViewHolder holder, int position) {
        try {
            BannerItemModel  bannerItemModel = bannerList.get(position);

            holder.txt_prosperId.setText(bannerItemModel.getProsperId());
            holder.txt_banner_content.setText(bannerItemModel.getBannerTitle());
            holder.txt_banner_price.setText(bannerItemModel.getDescription());
            String[] col = bannerItemModel.getColorCode().split(",");
            Function.GradientBgSet(holder.defult_one, col[0], col[1]);
            if (bannerItemModel.getIsDefault() == 1) {
                holder.layout_root.setVisibility(View.VISIBLE);
                holder.img_default_banner.setVisibility(View.GONE);
                Glide.with(ctx)
                        .load(bannerItemModel.getBannerImage())
                        .error(R.drawable.ic_no_image)
                        .placeholder(R.drawable.ic_no_image)
                        .into(holder.img_banner);

            } else {
                holder.img_default_banner.setVisibility(View.VISIBLE);
                holder.layout_root.setVisibility(View.GONE);

                Glide.with(ctx)
                        .load(bannerItemModel.getBannerImage())
                        .error(R.drawable.ic_no_image)
                        .placeholder(R.drawable.ic_no_image)
                        .into(holder.img_default_banner);

            }
            holder.layout_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.itemClick(bannerItemModel);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getPageCount() {
        return bannerList.size();
    }

//    @Override
//    public MyViewHolder onCreatePageViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }

//    @Override
//    public void onBindPageViewHolder(@NonNull MyViewHolder holder, int position) {
//
//    }

//    @Override
//    public int getPageCount() {
//        return 0;
//    }

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