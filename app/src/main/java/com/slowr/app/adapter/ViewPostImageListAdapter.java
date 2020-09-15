package com.slowr.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.slowr.app.R;
import com.slowr.app.models.UploadImageModel;

import java.util.ArrayList;

public class ViewPostImageListAdapter extends RecyclerView.Adapter<ViewPostImageListAdapter.MyViewHolder> {

    public Context _cotx;
    ArrayList<UploadImageModel> shareImageList;
    CallBack callBack;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_ad_view;
        public ImageView img_ad_border;
        public FrameLayout layout_root;

        public MyViewHolder(View itemView) {
            super(itemView);
            //this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.img_ad_view = (ImageView) itemView.findViewById(R.id.img_ad_view);
            this.img_ad_border = (ImageView) itemView.findViewById(R.id.img_ad_border);
            this.layout_root = (FrameLayout) itemView.findViewById(R.id.layout_root);
        }

    }

    public ViewPostImageListAdapter(Context ctx, ArrayList<UploadImageModel> shareImageLists) {
        this.shareImageList = shareImageLists;
        this._cotx = ctx;
        Log.e("Constructor_Called", String.valueOf(shareImageList.size()));


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_postview_image_item, parent, false);
        //view.setOnClickListener(MainActivity.myOnClickListener);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int listPosition) {
        UploadImageModel model = shareImageList.get(listPosition);


        Glide.with(_cotx)
                .load(model.getImgURL())
                .placeholder(R.drawable.ic_default_horizontal)
                .error(R.drawable.ic_default_horizontal)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.img_ad_view);
        if (model.isChanged()) {
            holder.img_ad_border.setImageResource(R.drawable.bg_orenge_border_select);
        } else {
            holder.img_ad_border.setImageResource(R.drawable.bg_blue_border_select);
        }

        holder.layout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.itemClick(listPosition);
            }
        });
    }


    @Override
    public int getItemCount() {

        return shareImageList.size();

    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        public void itemClick(int pos);


    }

}