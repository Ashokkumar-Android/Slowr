package com.slowr.matisse.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.slowr.matisse.R;
import com.slowr.matisse.internal.ui.widget.CropImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CropFragment extends Fragment {
    View rootView;

    public CropFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.viewpager_cropper_layout, container, false);
        final CropImageView imgDisplay = rootView.findViewById(R.id.crop_image_view);
        final ImageView gifView = rootView.findViewById(R.id.gif_view);
        final ImageView org_image = rootView.findViewById(R.id.org_image);
       /* String extension = paths.get(position).substring(paths.get(position).lastIndexOf("."));
        if (extension.equals(".gif") || extension.equals("gif")) {
            gifView.setVisibility(View.VISIBLE);
            imgDisplay.setVisibility(View.GONE);
            final Handler handler = new Handler();
            Glide.with(getActivity())
                    .load(Model.getImageUri())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getActivity())
                                            .load(Model.getImageUri().getPath())
                                            .into(gifView);
                                }
                            });
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(gifView);
        } else {
            gifView.setVisibility(View.GONE);
            imgDisplay.setVisibility(View.VISIBLE);
            imgDisplay.setFocusWidth(Model.getWidth());
            imgDisplay.setFocusHeight(Model.getHeight());
            imgDisplay.setImageURI(Model.getImageUri());
        }*/
        return rootView;
    }

}
