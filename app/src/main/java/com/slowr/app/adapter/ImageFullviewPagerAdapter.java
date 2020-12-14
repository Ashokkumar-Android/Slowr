package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.models.UploadImageModel;

import java.util.ArrayList;

public class ImageFullviewPagerAdapter extends PagerAdapter {

    Callback callback;
    public Context _cotx;
    ArrayList<UploadImageModel> shareImageList;

    public ImageFullviewPagerAdapter(Context ctx, ArrayList<UploadImageModel> shareImageLists) {
        shareImageList = shareImageLists;
        _cotx = ctx;
    }

    @Override
    public int getCount() {

        return shareImageList.size();

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_image_view_item, container, false);
        try {
            ImageView img_full_view = itemView.findViewById(R.id.img_full_view);

            Glide.with(_cotx)
                    .asBitmap()
                    .error(R.drawable.ic_no_image)
                    .placeholder(R.drawable.ic_no_image)
                    .load(shareImageList.get(position).getImgURL())
                    .into(img_full_view);

            container.addView(itemView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onCloseClick(int pos, ViewGroup v);
    }


}
