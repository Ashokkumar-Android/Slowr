package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.BannerItemModel;

import java.util.List;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.MyViewHolder> {

    private List<BannerItemModel> categoryList;
    Callback callback;
    Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_banner_title;
        public TextView txt_from_date;
        public TextView txt_to_date;
        public TextView txt_active_status;
        public Button btn_edit;
        public Button btn_delete;
        LinearLayout layout_root;

        public MyViewHolder(View view) {
            super(view);
            txt_banner_title = view.findViewById(R.id.txt_banner_title);
            txt_from_date = view.findViewById(R.id.txt_from_date);
            txt_to_date = view.findViewById(R.id.txt_to_date);
            btn_edit = view.findViewById(R.id.btn_edit);
            btn_delete = view.findViewById(R.id.btn_delete);
            txt_active_status = view.findViewById(R.id.txt_active_status);
            layout_root = view.findViewById(R.id.layout_root);
            btn_edit.setOnClickListener(this);
            btn_delete.setOnClickListener(this);
            layout_root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    callback.itemClick(getAdapterPosition());
                    break;

                case R.id.btn_delete:
                    callback.itemDelete(getAdapterPosition());
                    break;
            }

        }
    }

    public BannerListAdapter(List<BannerItemModel> _categoryList, Context ctx) {
        this.categoryList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_banner_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {


            BannerItemModel model = categoryList.get(position);

            holder.txt_banner_title.setText(model.getBannerTitle().trim());
            holder.txt_from_date.setText(" :  " + model.getBannerFromDate().trim());
            holder.txt_to_date.setText(" :  " + model.getBannerToDate().trim());
            if (model.getBannerStatus().equals("0")) {
                holder.txt_active_status.setText(ctx.getString(R.string.txt_created));
                holder.txt_active_status.setBackground(ctx.getResources().getDrawable(R.drawable.bg_orenge_border_color));
                holder.btn_edit.setText(ctx.getString(R.string.txt_edit));
                holder.txt_active_status.setTextColor(ctx.getResources().getColor(R.color.txt_orange));
                holder.btn_edit.setVisibility(View.VISIBLE);
            } else if (model.getBannerStatus().equals("1")) {
                holder.txt_active_status.setText(ctx.getString(R.string.txt_active));
                holder.txt_active_status.setBackground(ctx.getResources().getDrawable(R.drawable.bg_green_border_view));
                holder.txt_active_status.setTextColor(ctx.getResources().getColor(R.color.bg_green));
                holder.btn_edit.setText(ctx.getString(R.string.txt_edit));
                holder.btn_edit.setVisibility(View.VISIBLE);
            } else if (model.getBannerStatus().equals("2")) {
                holder.txt_active_status.setText(ctx.getString(R.string.txt_edited));
                holder.txt_active_status.setBackground(ctx.getResources().getDrawable(R.drawable.bg_orenge_border_color));
                holder.btn_edit.setText(ctx.getString(R.string.txt_edit));
                holder.btn_edit.setVisibility(View.VISIBLE);
                holder.txt_active_status.setTextColor(ctx.getResources().getColor(R.color.txt_orange));

            } else if (model.getBannerStatus().equals("3")) {
                holder.txt_active_status.setText(ctx.getString(R.string.txt_expired));
                holder.btn_edit.setText(ctx.getString(R.string.txt_renew));
                holder.btn_edit.setVisibility(View.VISIBLE);
                holder.txt_active_status.setBackground(ctx.getResources().getDrawable(R.drawable.bg_gray_border_color));
                holder.txt_active_status.setTextColor(ctx.getResources().getColor(R.color.inactive_border));
            } else if (model.getBannerStatus().equals("9")) {
                holder.txt_active_status.setText(ctx.getString(R.string.txt_in_review));
                holder.btn_edit.setVisibility(View.GONE);

                holder.txt_active_status.setBackground(ctx.getResources().getDrawable(R.drawable.bg_blue_border_select));
                holder.txt_active_status.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.txt_active_status.setText(ctx.getString(R.string.txt_in_review));
                holder.btn_edit.setText(ctx.getString(R.string.txt_edit));
                holder.btn_edit.setVisibility(View.GONE);
                holder.txt_active_status.setBackground(ctx.getResources().getDrawable(R.drawable.bg_blue_border_select));
                holder.txt_active_status.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
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
        void itemClick(int pos);

        void itemDelete(int pos);

    }
}