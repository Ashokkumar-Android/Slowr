package com.slowr.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.slowr.app.R;
import com.slowr.app.models.UploadImageModel;

import java.util.ArrayList;

public class PostImageListAdapter extends RecyclerView.Adapter<PostImageListAdapter.MyViewHolder> {

    public Context _cotx;
    ArrayList<UploadImageModel> shareImageList;
    CallBack callBack;
    int size = 100;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewIcon;
        public ImageView txt_add_image;
        CardView layout_delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            //this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = itemView.findViewById(R.id.img_captured);
            this.txt_add_image = itemView.findViewById(R.id.txt_add_image);
            this.layout_delete = itemView.findViewById(R.id.layout_delete);
        }
    }

    public PostImageListAdapter(Context ctx, ArrayList<UploadImageModel> shareImageLists) {
        this.shareImageList = shareImageLists;
        this._cotx = ctx;
        Log.e("Constructor_Called", String.valueOf(shareImageList.size()));


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.img_multi_select_layout, parent, false);
        //view.setOnClickListener(MainActivity.myOnClickListener);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int listPosition) {
        try{
        UploadImageModel model = shareImageList.get(listPosition);

//        holder.imageViewIcon.setImageURI(UriList.get(listPosition));
        if (model.getImgURL().equals("")) {
            Glide.with(_cotx)
                    .load(model.getUri())
                    .placeholder(R.drawable.ic_no_image)
                    .error(R.drawable.ic_no_image)
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
                    .into(holder.imageViewIcon);
        } else {
            Glide.with(_cotx)
                    .load(model.getImgURL())
                    .placeholder(R.drawable.ic_no_image)
                    .error(R.drawable.ic_no_image)
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
                    .into(holder.imageViewIcon);
        }


        if (listPosition == 0) {
            if (shareImageList.size() > 4) {
                holder.txt_add_image.setVisibility(View.GONE);
            } else {
                holder.txt_add_image.setVisibility(View.VISIBLE);
            }

        } else {
            holder.txt_add_image.setVisibility(View.GONE);
        }

        holder.imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.txt_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.addClick();
            }
        });
        holder.layout_delete.setOnClickListener(new View.OnClickListener() {
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

        void addClick();
    }

}