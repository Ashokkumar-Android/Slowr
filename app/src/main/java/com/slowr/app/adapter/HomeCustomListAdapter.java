package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.models.HomeAdsModel;

import java.util.List;

public class HomeCustomListAdapter extends RecyclerView.Adapter<HomeCustomListAdapter.MyViewHolder> {

    private List<HomeAdsModel> categoryList;
    Callback callback;
    Context ctx;
    PopularAdListAdapter popularAdListAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_list_title;
        public TextView txt_view_more;
        public RecyclerView rc_home_ad_list;

        public MyViewHolder(View view) {
            super(view);
            txt_list_title = view.findViewById(R.id.txt_list_title);
            rc_home_ad_list = view.findViewById(R.id.rc_home_ad_list);
            txt_view_more = view.findViewById(R.id.txt_view_more);
            LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(ctx, RecyclerView.HORIZONTAL, false);
            rc_home_ad_list.setLayoutManager(linearLayoutManager3);
            rc_home_ad_list.setItemAnimator(new DefaultItemAnimator());
            txt_view_more.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txt_view_more:
                    callback.viewMoreClick(getAdapterPosition());
                    break;
            }

        }
    }

    public HomeCustomListAdapter(List<HomeAdsModel> _categoryList, Context ctx) {
        this.categoryList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_home_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomeAdsModel model = categoryList.get(position);

        holder.txt_list_title.setText(model.getListTitle().trim());
        popularAdListAdapter = new PopularAdListAdapter(model.getHomeAdsList(), ctx);
        holder.rc_home_ad_list.setAdapter(popularAdListAdapter);
        popularAdListAdapter.setCallback(new PopularAdListAdapter.Callback() {
            @Override
            public void itemClick(AdItemModel model) {
                callback.itemClick(model);
            }
        });

        if(model.getHomeAdsList().size()==0){
            holder.txt_view_more.setVisibility(View.GONE);
        }else {
            holder.txt_view_more.setVisibility(View.VISIBLE);
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
        public void itemClick(AdItemModel model);

        void viewMoreClick(int pos);
    }
}