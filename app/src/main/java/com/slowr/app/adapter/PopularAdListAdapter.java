package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.utils.Function;

import java.util.List;

public class PopularAdListAdapter extends RecyclerView.Adapter<PopularAdListAdapter.MyViewHolder> {

    private List<AdItemModel> categoryList;
    Callback callback;
    Context ctx;
    String url = "";
    int reqPos = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_ad_title;
        public TextView txt_price;
        public CardView layout_root;
        public CardView layout_request_flyer;
        public ImageView img_ad;
        LinearLayout layout_promoted;
        ImageView txt_premium_mark;
        ImageView img_top_page_mark;
        ImageView img_request;

        public MyViewHolder(View view) {
            super(view);
            txt_ad_title = view.findViewById(R.id.txt_ad_title);
            txt_price = view.findViewById(R.id.txt_price);
            layout_root = view.findViewById(R.id.layout_root);
            layout_request_flyer = view.findViewById(R.id.layout_request_flyer);
            img_ad = view.findViewById(R.id.img_ad);
            layout_promoted = view.findViewById(R.id.layout_promoted);
            img_top_page_mark = view.findViewById(R.id.img_top_page_mark);
            txt_premium_mark = view.findViewById(R.id.txt_premium_mark);
            img_request = view.findViewById(R.id.img_request);
            layout_root.setOnClickListener(this);
            layout_request_flyer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    callback.itemClick(categoryList.get(getAdapterPosition()));
                    break;
                case R.id.layout_request_flyer:
                    callback.itemRequestClick(getAdapterPosition());
                    break;

            }

        }
    }

    public PopularAdListAdapter(List<AdItemModel> _categoryList, Context ctx, String url, int pos) {
        this.categoryList = _categoryList;
        this.ctx = ctx;
        this.url = url;
        this.reqPos = pos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_popular_ad_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            AdItemModel movie = categoryList.get(position);

            holder.txt_ad_title.setText(movie.getAdTitle().trim());

            Function.SetRentalPrice(movie.getAdFee(), movie.getAdDuration(), holder.txt_price, movie.getCatGroup(), ctx);
//            if (movie.getAdFee() != null) {
//                holder.txt_price.setVisibility(View.VISIBLE);
//                String price = "";
//                if (movie.getAdFee().contains(".")) {
//                    String[] tempPrice = movie.getAdFee().split("\\.");
//                    price = tempPrice[0];
//                } else {
//                    price = movie.getAdFee();
//                }
//
//                Log.i("Fave", movie.getIsFavorite());
//                if (price.equals("0") || price.equals("") || movie.getAdDuration().equals("Custom")) {
//                    Function.RentalDurationText(holder.txt_price, movie.getCatGroup(), movie.getAdDuration(), ctx);
//                } else {
//                    DecimalFormat formatter = new DecimalFormat("#,###,###");
//                    String formatPrice = formatter.format(Integer.valueOf(price));
//                    holder.txt_price.setText("₹ " + formatPrice + " / " + movie.getAdDuration());
//                }
//            } else {
//                Function.RentalDurationText(holder.txt_price, movie.getCatGroup(), movie.getAdDuration(), ctx);
//            }
            if (movie.getAdPromotion().equals("1")) {
                holder.layout_promoted.setVisibility(View.VISIBLE);
                holder.txt_premium_mark.setVisibility(View.GONE);
                holder.img_top_page_mark.setVisibility(View.VISIBLE);
            } else if (movie.getAdPromotion().equals("2")) {
                holder.layout_promoted.setVisibility(View.VISIBLE);
                holder.txt_premium_mark.setVisibility(View.VISIBLE);
                holder.img_top_page_mark.setVisibility(View.GONE);
            } else {
                holder.layout_promoted.setVisibility(View.INVISIBLE);
            }


            int defu = R.drawable.ic_no_image;
            if (movie.getCatGroup() != null && movie.getCatGroup().equals("1")) {
                if (movie.getAdType().equals("1")) {
                    defu = R.drawable.ic_no_image;
                } else {
                    if (movie.getAdParentId() != null && movie.getAdParentId().equals("1")) {
                        defu = R.drawable.ic_need_space;
                    } else if (movie.getAdParentId() != null && movie.getAdParentId().equals("34")) {
                        defu = R.drawable.ic_need_pet;
                    } else if (movie.getAdParentId() != null && movie.getAdParentId().equals("5")) {
                        defu = R.drawable.ic_need_book;
                    } else {
                        defu = R.drawable.ic_need_product;
                    }
                }
                Glide.with(ctx)
                        .load(movie.getPhotoType())
                        .error(defu)
                        .placeholder(defu)
                        .into(holder.img_ad);
            } else {
                if (movie.getAdType().equals("1")) {
                    defu = R.drawable.ic_service_big;
                } else {
                    defu = R.drawable.ic_need_service;
                }
                Glide.with(ctx)
                        .load(movie.getPhotoType())
                        .circleCrop()
                        .error(defu)
                        .placeholder(defu)
                        .into(holder.img_ad);
            }

            if (reqPos != -1 && !url.equals("") && position == reqPos) {
                holder.layout_request_flyer.setVisibility(View.VISIBLE);
                if (movie.getCatGroup() != null && movie.getCatGroup().equals("1")) {
                    Glide.with(ctx)
                            .load(url)
                            .error(R.drawable.ic_no_image)
                            .placeholder(R.drawable.ic_no_image)
                            .into(holder.img_request);
                }else {
                    Glide.with(ctx)
                            .load(url)
                            .error(R.drawable.ic_service_big)
                            .placeholder(R.drawable.ic_service_big)
                            .into(holder.img_request);
                }
            } else {
                holder.layout_request_flyer.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
        void itemClick(AdItemModel model);

        void itemRequestClick(int pos);


    }


}