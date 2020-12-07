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

import java.util.ArrayList;
import java.util.List;

public class AdListAdapter extends RecyclerView.Adapter<AdListAdapter.MyViewHolder> implements Filterable {

    private List<AdItemModel> categoryList;
    private List<AdItemModel> categoryListFilter;
    Callback callback;
    Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_ad_title;
        public TextView txt_price;
        public TextView txt_location;
        public TextView txt_like_count;
        public LinearLayout layout_root;
        public ImageView img_ad;
        public LikeButton img_favorite;
        public ImageView img_promote;
        public ImageView img_share;
        LinearLayout layout_promote;
        LinearLayout layout_promoted;
        LinearLayout layout_location;
        TextView txt_active_status;
        ImageView txt_premium_mark;
        ImageView img_top_page_mark;
        ImageView img_bg_gradient;

        public MyViewHolder(View view) {
            super(view);
            txt_ad_title = view.findViewById(R.id.txt_ad_title);
            txt_price = view.findViewById(R.id.txt_price);
            layout_root = view.findViewById(R.id.layout_root);
            img_ad = view.findViewById(R.id.img_ad);
            txt_location = view.findViewById(R.id.txt_location);
            txt_like_count = view.findViewById(R.id.txt_like_count);
            img_favorite = view.findViewById(R.id.img_favorite);
            layout_promote = view.findViewById(R.id.layout_promote);
            img_promote = view.findViewById(R.id.img_promote);
            img_share = view.findViewById(R.id.img_share);
            txt_active_status = view.findViewById(R.id.txt_active_status);
            layout_promoted = view.findViewById(R.id.layout_promoted);
            img_top_page_mark = view.findViewById(R.id.img_top_page_mark);
            txt_premium_mark = view.findViewById(R.id.txt_premium_mark);
            img_bg_gradient = view.findViewById(R.id.img_bg_gradient);
            layout_location = view.findViewById(R.id.layout_location);

            layout_root.setOnClickListener(this);
            img_promote.setOnClickListener(this);
            img_share.setOnClickListener(this);
            img_favorite.setVisibility(View.GONE);
            layout_location.setVisibility(View.GONE);
            layout_promote.setVisibility(View.VISIBLE);
            txt_active_status.setVisibility(View.VISIBLE);
            img_bg_gradient.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    callback.itemClick(categoryListFilter.get(getAdapterPosition()));
                    break;
                case R.id.img_promote:
                    callback.onPromoteClick(categoryListFilter.get(getAdapterPosition()));
                    break;
                case R.id.img_share:
                    callback.onShareClick(categoryListFilter.get(getAdapterPosition()));
                    break;


            }

        }
    }

    public AdListAdapter(List<AdItemModel> _categoryList, Context ctx) {
        this.categoryList = _categoryList;
        this.categoryListFilter = _categoryList;
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
        AdItemModel movie = categoryListFilter.get(position);

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
            holder.img_promote.setVisibility(View.GONE);
        } else if (movie.getAdPromotion().equals("2")) {
            holder.layout_promoted.setVisibility(View.VISIBLE);
            holder.txt_premium_mark.setVisibility(View.VISIBLE);
            holder.img_top_page_mark.setVisibility(View.GONE);
            holder.img_promote.setVisibility(View.GONE);
        } else {
            holder.layout_promoted.setVisibility(View.GONE);
            if (movie.getAdType().equals("1")) {
//                holder.img_promote.setVisibility(View.VISIBLE);
                holder.img_promote.setVisibility(View.GONE);
            } else {
                holder.img_promote.setVisibility(View.GONE);
            }
        }
        if (movie.getAdStatus().equals("1")) {
            holder.txt_active_status.setText(ctx.getString(R.string.txt_active));
            holder.txt_active_status.setBackground(ctx.getResources().getDrawable(R.drawable.bg_green_border_view));
            holder.txt_active_status.setTextColor(ctx.getResources().getColor(R.color.bg_green));
            holder.img_share.setVisibility(View.VISIBLE);
        } else if (movie.getAdStatus().equals("2")) {
            holder.txt_active_status.setText(ctx.getString(R.string.txt_in_active));
            holder.img_promote.setVisibility(View.GONE);
            holder.txt_active_status.setBackground(ctx.getResources().getDrawable(R.drawable.bg_gray_border_color));
            holder.txt_active_status.setTextColor(ctx.getResources().getColor(R.color.inactive_border));
            holder.img_share.setVisibility(View.GONE);
        } else {
            holder.txt_active_status.setText(ctx.getString(R.string.txt_in_review));
            holder.img_promote.setVisibility(View.GONE);
            holder.txt_active_status.setBackground(ctx.getResources().getDrawable(R.drawable.bg_orenge_border_color));
            holder.txt_active_status.setTextColor(ctx.getResources().getColor(R.color.txt_orange));
            holder.img_share.setVisibility(View.GONE);
        }
        if (movie.getAdFee() != null) {
            holder.txt_price.setVisibility(View.VISIBLE);
            String price = "";
            if (movie.getAdFee().contains(".")) {
                String[] tempPrice = movie.getAdFee().split("\\.");
                price = tempPrice[0];
            } else {
                price = movie.getAdFee();
            }

            Log.i("Fave", movie.getIsFavorite());
            if (price.equals("0") || price.equals("") || movie.getAdDuration().equals("Custom")) {
                if (movie.getCatGroup().equals("1")) {
                    holder.txt_price.setText(ctx.getString(R.string.custom_rent));
                } else {
                    holder.txt_price.setText(ctx.getString(R.string.custom_hire));
                }
            } else {
                holder.txt_price.setText("₹ " + price + " / " + movie.getAdDuration());
            }
        } else {
            if (movie.getCatGroup().equals("1")) {
                if (movie.getAdDuration().equals("Custom")) {
                    holder.txt_price.setText(ctx.getString(R.string.custom_rent));
                } else if (movie.getAdDuration().equals("Per Hour")) {
                    holder.txt_price.setText(ctx.getString(R.string.hour_rent));
                } else if (movie.getAdDuration().equals("Per Day")) {
                    holder.txt_price.setText(ctx.getString(R.string.day_rent));
                }
            } else {
                if (movie.getAdDuration().equals("Custom")) {
                    holder.txt_price.setText(ctx.getString(R.string.custom_hire));
                } else if (movie.getAdDuration().equals("Per Hour")) {
                    holder.txt_price.setText(ctx.getString(R.string.hour_hire));
                } else if (movie.getAdDuration().equals("Per Day")) {
                    holder.txt_price.setText(ctx.getString(R.string.day_hire));
                }
            }

        }
        Glide.with(ctx)
                .load(movie.getPhotoType())
                .error(R.drawable.ic_default_horizontal)
                .placeholder(R.drawable.ic_default_horizontal)
                .into(holder.img_ad);

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
        void itemClick(AdItemModel model);

        void onShareClick(AdItemModel model);

        void onPromoteClick(AdItemModel model);
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
                    List<AdItemModel> filteredList = new ArrayList<>();
                    for (AdItemModel row : categoryList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAdTitle().toLowerCase().contains(charString.toLowerCase())) {
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
                categoryListFilter = (ArrayList<AdItemModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}