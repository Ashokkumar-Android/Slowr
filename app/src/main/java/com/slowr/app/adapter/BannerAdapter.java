package com.slowr.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.models.BannerItemModel;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class BannerAdapter extends PagerAdapter {


    private LayoutInflater inflater;
    private Context context;
    ArrayList<BannerItemModel> bannerList;
    CallBack callBack;

    public BannerAdapter(Context context, ArrayList<BannerItemModel> _bannerList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.bannerList = _bannerList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.layout_banner_item, view, false);

        assert imageLayout != null;
        ImageView img_banner = imageLayout.findViewById(R.id.img_banner);
        ImageView defult_one = imageLayout.findViewById(R.id.defult_one);
        TextView txt_prosperId = imageLayout.findViewById(R.id.txt_prosperId);
        TextView txt_banner_content = imageLayout.findViewById(R.id.txt_banner_content);
        TextView txt_banner_price = imageLayout.findViewById(R.id.txt_banner_price);
        TextView txt_banner_like = imageLayout.findViewById(R.id.txt_banner_like);
        LinearLayout layout_root = imageLayout.findViewById(R.id.layout_root);
        BannerItemModel bannerItemModel = bannerList.get(position);
        txt_prosperId.setText(bannerItemModel.getProsperId());
        txt_banner_content.setText(bannerItemModel.getBannerTitle());
        txt_banner_price.setText(bannerItemModel.getDescription());
//        layout_root.setBackgroundColor(Color.parseColor(bannerItemModel.getColorCode()));
        Glide.with(context)
                .load(bannerItemModel.getBannerImage())
                .error(R.drawable.ic_default_horizontal)
                .placeholder(R.drawable.ic_default_horizontal)
                .into(img_banner);
        view.addView(imageLayout, 0);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerItemModel.getProsperId() != null)
                    callBack.onItemClick(position);
            }
        });

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,new int[]{
                Color.parseColor("#8A2387"),
                Color.parseColor("#E94057"),
                Color.parseColor("#F27121")
        });

        // Set the color array to draw gradient
//        gd.setColors(new int[]{
//                Color.RED,
//                Color.GREEN,
//                Color.YELLOW
//        });

        // Set the GradientDrawable gradient type linear gradient
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        // Set GradientDrawable shape is a rectangle
        gd.setShape(GradientDrawable.RECTANGLE);

        // Set 3 pixels width solid blue color border
        gd.setStroke(0, Color.BLUE);

        // Set GradientDrawable width and in pixels
//        gd.setSize(450, 150); // Width 450 pixels and height 150 pixels

        // Set GradientDrawable as ImageView source image
        defult_one.setImageDrawable(gd);
//        Glide.with(context)
//                .load(bannerItemModel.getBannerImage())
//                .transform(new BlurTransformation())
//                .error(R.drawable.ic_default_horizontal)
//                .placeholder(R.drawable.ic_default_horizontal)
//                .into(defult_one);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void onItemClick(int pos);
    }

}
