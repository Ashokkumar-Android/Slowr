package com.slowr.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.ColorCodeItemModel;
import com.slowr.app.utils.Function;

import java.util.ArrayList;

public class ViewColorAdapter extends RecyclerView.Adapter<ViewColorAdapter.MyViewHolder> {

    public Context _cotx;
    ArrayList<ColorCodeItemModel> shareImageList;
    CallBack callBack;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View img_ad_view;
        public ImageView img_ad_border;
        public FrameLayout layout_root;

        public MyViewHolder(View itemView) {
            super(itemView);
            //this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.img_ad_view = itemView.findViewById(R.id.img_ad_view);
            this.img_ad_border = itemView.findViewById(R.id.img_ad_border);
            this.layout_root = itemView.findViewById(R.id.layout_root);
        }

    }

    public ViewColorAdapter(Context ctx, ArrayList<ColorCodeItemModel> shareImageLists) {
        this.shareImageList = shareImageLists;
        this._cotx = ctx;
        Log.e("Constructor_Called", String.valueOf(shareImageList.size()));


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_color_item, parent, false);
        //view.setOnClickListener(MainActivity.myOnClickListener);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int listPosition) {
        try {
            ColorCodeItemModel model = shareImageList.get(listPosition);
//        holder.img_ad_view.setBackgroundColor(Color.parseColor(model.getColorOne()));
            Function.GradientBgSet(holder.img_ad_view, model.getColorOne(), model.getColorTwo());

            if (model.isChange()) {
                holder.img_ad_border.setImageResource(R.drawable.bg_orenge_border_color);
            } else {
                holder.img_ad_border.setImageResource(R.drawable.bg_blue_border_color);
            }

            holder.layout_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.itemClick(listPosition);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return shareImageList.size();

    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void itemClick(int pos);


    }

}