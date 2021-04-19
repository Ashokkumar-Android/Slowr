package com.slowr.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    boolean isVisible = false;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_banner;
        ImageView defult_one;
        TextView txt_prosperId;
        TextView txt_banner_content;
        TextView txt_banner_price;
        TextView txt_banner_like;
        TextView txt_click_profile;
        LinearLayout layout_root;
        LinearLayout layout_click;
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
            layout_click = view.findViewById(R.id.layout_click);
            txt_click_profile = view.findViewById(R.id.txt_click_profile);

            layout_click.setOnClickListener(this);
            if (isVisible) {
                txt_click_profile.setVisibility(View.VISIBLE);
            } else {
                txt_click_profile.setVisibility(View.GONE);
            }
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
            BannerItemModel bannerItemModel = bannerList.get(position);

            holder.txt_prosperId.setText(bannerItemModel.getProsperId());
            holder.txt_banner_content.setText(bannerItemModel.getBannerTitle().trim());
            holder.txt_banner_price.setText(bannerItemModel.getDescription().trim());
            String[] col = bannerItemModel.getColorCode().split(",");
            Function.GradientBgSet(holder.defult_one, col[0], col[1]);
            if (bannerItemModel.getIsDefault() == 0) {
                holder.img_default_banner.setVisibility(View.VISIBLE);
                holder.layout_root.setVisibility(View.GONE);

                Glide.with(ctx)
                        .load(bannerItemModel.getBannerImage())
                        .error(R.drawable.ic_no_image)
                        .placeholder(R.drawable.ic_no_image)
                        .into(holder.img_default_banner);

            } else {

                holder.layout_root.setVisibility(View.VISIBLE);
                holder.img_default_banner.setVisibility(View.GONE);
                Glide.with(ctx)
                        .load(bannerItemModel.getBannerImage())
                        .error(R.drawable.ic_no_image)
                        .placeholder(R.drawable.ic_no_image)
                        .into(holder.img_banner);

            }
//            holder.layout_click.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    callback.itemClick(bannerItemModel);
//                    Log.i("Click", " click ");
//                }
//            });
            holder.layout_click.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            System.out.println(" pressed ");
                            Log.i("Click", " pressed ");
                            callback.itemClick(bannerItemModel, true);
                            return true;
                        case MotionEvent.ACTION_UP:
                            System.out.println(" released ");
                            Log.i("Click", " released ");
                            callback.itemClick(bannerItemModel, false);
                            return true;
                    }
                    return false;
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
        void itemClick(BannerItemModel model, boolean isClick);

    }

    public void hideText(boolean _isVisible) {
        isVisible = _isVisible;
    }

}