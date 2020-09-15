package com.slowr.app.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.slowr.app.R;

import java.util.ArrayList;


public class BannerAdapter extends PagerAdapter {


    private LayoutInflater inflater;
    private Context context;


    public BannerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.layout_banner_item, view, false);

        assert imageLayout != null;
        final ImageView img_banner = imageLayout
                .findViewById(R.id.img_banner);
        TextView txt_prosperId = imageLayout.findViewById(R.id.txt_prosperId);
        TextView txt_banner_content = imageLayout.findViewById(R.id.txt_banner_content);
        TextView txt_banner_price = imageLayout.findViewById(R.id.txt_banner_price);
        TextView txt_banner_like = imageLayout.findViewById(R.id.txt_banner_like);


        switch (position) {
            case 0:
                img_banner.setImageResource(R.drawable.ic_banner_one);
                txt_prosperId.setText("AB1234");
                txt_banner_content.setText("Luxury cars for daily rent");
                txt_banner_price.setText("Renting @ ₹1800/day");
                txt_banner_like.setText("4536");
                break;
            case 1:
                img_banner.setImageResource(R.drawable.ic_banner_two);
                txt_prosperId.setText("BC4386");
                txt_banner_content.setText("Cricket kits for rental");
                txt_banner_price.setText("Renting @ ₹500/day");
                txt_banner_like.setText("2864");
                break;
            case 2:
                img_banner.setImageResource(R.drawable.ic_banner_three);
                txt_prosperId.setText("AB5689");
                txt_banner_content.setText("We provide home cleaning service");
                txt_banner_price.setText("Service @ ₹900");
                txt_banner_like.setText("763");
                break;
        }

        view.addView(imageLayout, 0);


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


}
