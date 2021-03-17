package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.GSTListItemModel;

import java.util.List;

public class GSTListAdapter extends RecyclerView.Adapter<GSTListAdapter.MyViewHolder> {

    private List<GSTListItemModel> categoryList;
    Callback callback;
    Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_gst_no;
        public TextView txt_gst_address;

        LinearLayout layout_root;

        public MyViewHolder(View view) {
            super(view);
            txt_gst_no = view.findViewById(R.id.txt_gst_no);
            txt_gst_address = view.findViewById(R.id.txt_gst_address);
            layout_root = view.findViewById(R.id.layout_root);
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

    public GSTListAdapter(List<GSTListItemModel> _categoryList, Context ctx) {
        this.categoryList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_gst_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {


            GSTListItemModel model = categoryList.get(position);
            if (model.getGstNo().equals("")) {
                holder.layout_root.setVisibility(View.GONE);
            }else {
                holder.layout_root.setVisibility(View.VISIBLE);
            }
            holder.txt_gst_no.setText(model.getGstNo().trim());
            holder.txt_gst_address.setText(model.getCompanyName().trim());


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


    }
}