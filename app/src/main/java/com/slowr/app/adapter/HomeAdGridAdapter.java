package com.slowr.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.slowr.app.R;
import com.slowr.app.activity.LoginActivity;
import com.slowr.app.activity.UserProfileActivity;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.text.DecimalFormat;
import java.util.List;

public class HomeAdGridAdapter extends RecyclerView.Adapter<HomeAdGridAdapter.MyViewHolder> {

    private List<AdItemModel> categoryList;
    Callback callback;
    Activity ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnLikeListener {
        public TextView txt_ad_title;
        public TextView txt_price;
        public TextView txt_location;
        public TextView txt_like_count;
        public TextView txt_service_count;
        public LinearLayout layout_root;
        public ImageView img_ad;
        public LikeButton img_favorite;
        public ImageView img_share;
        LinearLayout layout_promoted;
        ImageView txt_premium_mark;
        ImageView img_top_page_mark;

        public MyViewHolder(View view) {
            super(view);
            txt_ad_title = view.findViewById(R.id.txt_ad_title);
            txt_price = view.findViewById(R.id.txt_price);
            layout_root = view.findViewById(R.id.layout_root);
            img_ad = view.findViewById(R.id.img_ad);
            img_favorite = view.findViewById(R.id.img_favorite);
            txt_location = view.findViewById(R.id.txt_location);
            txt_like_count = view.findViewById(R.id.txt_like_count);
            layout_promoted = view.findViewById(R.id.layout_promoted);
            img_top_page_mark = view.findViewById(R.id.img_top_page_mark);
            txt_premium_mark = view.findViewById(R.id.txt_premium_mark);
            img_share = view.findViewById(R.id.img_share);
            txt_service_count = view.findViewById(R.id.txt_service_count);

            layout_root.setOnClickListener(this);
//            img_favorite.setOnClickListener(this);
            img_share.setOnClickListener(this);
            txt_service_count.setOnClickListener(this);
            img_favorite.setOnLikeListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    callback.itemClick(getAdapterPosition());
                    break;
                case R.id.img_favorite:
                    if (!categoryList.get(getAdapterPosition()).getUserId().equals(Sessions.getSession(Constant.UserId, ctx))) {
                        callback.onFavoriteClick(getAdapterPosition());
//                        Function.bounceAnimateImageView(true, img_favorite);
                    } else {
                        Function.CustomMessage(ctx, ctx.getString(R.string.my_ad_favorite));
                        img_favorite.setLiked(false);
                    }
                    break;
                case R.id.img_share:
                    callback.onShareClick(getAdapterPosition());
                    break;
                case R.id.txt_service_count:
                    Intent i = new Intent(ctx, UserProfileActivity.class);
                    i.putExtra("prosperId", categoryList.get(getAdapterPosition()).getProsperId());
                    ctx.startActivity(i);
                    break;

            }

        }

        @Override
        public void liked(LikeButton likeButton) {
            if (Sessions.getSessionBool(Constant.LoginFlag, ctx)) {
                if (!categoryList.get(getAdapterPosition()).getUserId().equals(Sessions.getSession(Constant.UserId, ctx))) {
                    callback.onFavoriteClick(getAdapterPosition());
//                        Function.bounceAnimateImageView(true, img_favorite);
                } else {
                    Function.CustomMessage(ctx, ctx.getString(R.string.my_ad_favorite));
                    likeButton.setLiked(false);
                }
            } else {
                Intent l = new Intent(ctx, LoginActivity.class);
                ctx.startActivity(l);
                likeButton.setLiked(false);
            }
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            if (Sessions.getSessionBool(Constant.LoginFlag, ctx)) {
                if (!categoryList.get(getAdapterPosition()).getUserId().equals(Sessions.getSession(Constant.UserId, ctx))) {
                    callback.onFavoriteClick(getAdapterPosition());
//                        Function.bounceAnimateImageView(true, img_favorite);
                } else {
                    Function.CustomMessage(ctx, ctx.getString(R.string.my_ad_favorite));
                    likeButton.setLiked(false);
                }
            } else {
                Intent l = new Intent(ctx, LoginActivity.class);
                ctx.startActivity(l);
                likeButton.setLiked(false);
            }
        }
    }

    public HomeAdGridAdapter(List<AdItemModel> _categoryList, Activity ctx) {
        this.categoryList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_grid_ad_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            AdItemModel movie = categoryList.get(position);

            holder.txt_ad_title.setText(movie.getAdTitle().trim());
            holder.txt_location.setText(movie.getAreaName() + ", " + movie.getCityName());
            if (movie.getLikeCount().equals("0")) {
                holder.txt_like_count.setText("");
            } else {
                holder.txt_like_count.setText(movie.getLikeCount());
            }
            if (movie.getIsFavorite().equals("0")) {
                holder.img_favorite.setLiked(false);

            } else {
                holder.img_favorite.setLiked(true);
            }
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
            Log.i("Fave", movie.getIsFavorite());
            if (movie.getAdFee() != null) {
                holder.txt_price.setVisibility(View.VISIBLE);
                String price = "";
                if (movie.getAdFee().contains(".")) {
                    String[] tempPrice = movie.getAdFee().split("\\.");
                    price = tempPrice[0];
                } else {
                    price = movie.getAdFee();
                }

                if (price.equals("0") || price.equals("") || movie.getAdDuration().equals("Custom")) {
                    Function.RentalDurationText(holder.txt_price, movie.getCatGroup(), movie.getAdDuration(), ctx);
                } else {
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formatPrice = formatter.format(Integer.valueOf(price));
                    holder.txt_price.setText("₹ " + formatPrice + " / " + movie.getAdDuration());
                }
            } else {
                Function.RentalDurationText(holder.txt_price, movie.getCatGroup(), movie.getAdDuration(), ctx);

            }

            int defu = R.drawable.ic_no_image;
            if (movie.getCatGroup() != null && movie.getCatGroup().equals("1")) {
                if (movie.getAdType().equals("1")) {
                    defu = R.drawable.ic_no_image;
                } else {
                    if (movie.getAdParentId() != null && movie.getAdParentId().equals("1")) {
                        defu = R.drawable.ic_need_space;
                    } else if (movie.getAdParentId() != null && movie.getAdParentId().equals("1306")) {
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
            if (movie.getAdType().equals("1") && movie.getCatGroup().equals("2") && movie.getServiceAdCount() > 1) {
                holder.txt_service_count.setVisibility(View.VISIBLE);
                if (movie.getServiceAdCount() - 1 == 1) {
                    holder.txt_service_count.setText("+" + (movie.getServiceAdCount() - 1) + " Service");
                } else {
                    holder.txt_service_count.setText("+" + (movie.getServiceAdCount() - 1) + " Services");
                }
            } else {
                holder.txt_service_count.setVisibility(View.INVISIBLE);
            }
//        if (!movie.getUserId().equals(Sessions.getSession(Constant.UserId, ctx))) {
//            holder.img_favorite.setEnabled(true);
//        }else {
//            holder.img_favorite.setEnabled(false);
//        }
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
        void itemClick(int pos);

        void onFavoriteClick(int pos);

        void onShareClick(int pos);
    }
}