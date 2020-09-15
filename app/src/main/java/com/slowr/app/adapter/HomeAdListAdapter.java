package com.slowr.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.List;

public class HomeAdListAdapter extends RecyclerView.Adapter<HomeAdListAdapter.MyViewHolder> {

    private List<AdItemModel> categoryList;
    Callback callback;
    Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_ad_title;
        public TextView txt_price;
        public TextView txt_location;
        public TextView txt_like_count;
        public LinearLayout layout_root;
        public ImageView img_ad;
        public ImageView img_favorite;
        LinearLayout layout_promoted;
        ImageView txt_premium_mark;
        ImageView img_top_page_mark;

        public MyViewHolder(View view) {
            super(view);
            txt_ad_title = view.findViewById(R.id.txt_ad_title);
            txt_price = view.findViewById(R.id.txt_price);
            layout_root = view.findViewById(R.id.layout_root);
            img_ad = view.findViewById(R.id.img_ad);
            txt_location = view.findViewById(R.id.txt_location);
            txt_like_count = view.findViewById(R.id.txt_like_count);
            img_favorite = view.findViewById(R.id.img_favorite);
            layout_promoted = view.findViewById(R.id.layout_promoted);
            img_top_page_mark = view.findViewById(R.id.img_top_page_mark);
            txt_premium_mark = view.findViewById(R.id.txt_premium_mark);

            layout_root.setOnClickListener(this);
            img_favorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    callback.itemClick(getAdapterPosition());
                    break;
                case R.id.img_favorite:
//                    callback.onFavoriteClick(getAdapterPosition());
//                    Function.bounceAnimateImageView(true, img_favorite);
                    if (!categoryList.get(getAdapterPosition()).getUserId().equals(Sessions.getSession(Constant.UserId, ctx))) {
                        callback.onFavoriteClick(getAdapterPosition());
//                        Function.bounceAnimateImageView(true, img_favorite);
                    } else {
                        Function.CustomMessage((Activity) ctx, ctx.getString(R.string.my_ad_favorite));
                    }
                    break;

            }

        }
    }

    public HomeAdListAdapter(List<AdItemModel> _categoryList, Context ctx) {
        this.categoryList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_home_ad_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AdItemModel movie = categoryList.get(position);

        holder.txt_ad_title.setText(movie.getAdTitle().trim());
        holder.txt_location.setText(movie.getAreaName() + ", " + movie.getCityName());
        if (movie.getLikeCount().equals("0")) {
            holder.txt_like_count.setText("");
        } else {
            holder.txt_like_count.setText(movie.getLikeCount());
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
            holder.layout_promoted.setVisibility(View.GONE);
        }
        if (movie.getAdFee() != null) {
            holder.txt_price.setVisibility(View.VISIBLE);
            String price = "";
            if (movie.getAdFee().contains(".")) {
                String tempPrice[] = movie.getAdFee().split("\\.");
                price = tempPrice[0];
            } else {
                price = movie.getAdFee();
            }

            Log.i("Fave", movie.getIsFavorite());
            if (price.equals("0") || movie.getAdDuration().equals("Custom")) {
                holder.txt_price.setText(movie.getAdDuration());
            } else {
                holder.txt_price.setText("₹ " + price + " / " + movie.getAdDuration());
            }
        }else {
            holder.txt_price.setVisibility(View.GONE);
        }
        if (movie.getIsFavorite().equals("0")) {
            holder.img_favorite.setImageResource(R.drawable.ic_favorite);

        } else {
            holder.img_favorite.setImageResource(R.drawable.ic_fav_select);
        }
        Glide.with(ctx)
                .load(movie.getPhotoType())
                .error(R.drawable.ic_default_horizontal)
                .placeholder(R.drawable.ic_default_horizontal)
                .into(holder.img_ad);

//        if (!movie.getPhotoType().isEmpty()) {
//            JSONArray jsonArray = null;
//            try {
//                jsonArray = new JSONArray(movie.getPhotoType());
//                String[] strArr = new String[jsonArray.length()];
//                String imgPath = "";
//                if (strArr.length != 0) {
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        strArr[i] = jsonArray.getString(i);
//                    }
//                    imgPath = Sessions.getSession(Constant.ImagePath, ctx) + "/" + strArr[0];
//
//
//                    Log.i("ImagePath", imgPath);
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
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

        public void onFavoriteClick(int pos);
    }
}