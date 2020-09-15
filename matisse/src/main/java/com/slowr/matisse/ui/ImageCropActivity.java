package com.slowr.matisse.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.slowr.matisse.R;
import com.slowr.matisse.internal.ui.widget.CropImageView;

import java.io.File;

public class ImageCropActivity extends AppCompatActivity {

    TextView DoneText;
    CropImageView crpImage;
    LinearLayout img_profile_back;
    int width;
    int height;
    int pos;
    String path;
    private File cropCacheFolder;
    ImageView delete_img, crop_portait, crop_rectangle, crop_square;
    DisplayMetrics displayMetrics;
    Uri myUri;
    int ctype = 0;
    int cropType = 0;
    int rectHeight = 0;
    int portraitHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        crpImage = findViewById(R.id.crop_image_view);
        DoneText = findViewById(R.id.crop_image_view_submit);
        img_profile_back = findViewById(R.id.img_profile_back);
        path = getIntent().getStringExtra("image_path");
        pos = getIntent().getIntExtra("list_pos", 0);
        height = getIntent().getIntExtra("image_height", 250);
        width = getIntent().getIntExtra("image_width", 250);
        cropType = getIntent().getIntExtra("cropType", 250);
        Log.e("ctype", cropType + "");
        /*crpImage.setFocusWidth(width);
        crpImage.setFocusHeight(height);*/
        myUri = Uri.parse(path);
//        crpImage.setImageURI(myUri);
        getDropboxIMGSize(myUri);
        crop_portait = findViewById(R.id.crop_portait);
        crop_rectangle = findViewById(R.id.crop_rectangle);
        crop_square = findViewById(R.id.crop_square);
        displayMetrics = new DisplayMetrics();
        getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        double val1 = displayMetrics.widthPixels ;
        double val2 = displayMetrics.heightPixels ;
        rectHeight = (int) val1;
        portraitHeight = (int) val2;
        img_profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crpImage.saveBitmapToFile(getCropCacheFolder(ImageCropActivity.this), crpImage.getFocusWidth(), crpImage.getFocusHeight(), false);
            }
        });
        crpImage.setOnBitmapSaveCompleteListener(new CropImageView.OnBitmapSaveCompleteListener() {
            @Override
            public void onBitmapSaveSuccess(File file) {
                Intent data = new Intent();
                data.putExtra("pos", pos);
                data.putExtra("path", file.getPath());
                data.putExtra("ctype", ctype);
                setResult(Activity.RESULT_OK, data);
                finish();
            }

            @Override
            public void onBitmapSaveError(File file) {
            }
        });
//        defaultmode();

//        switch (cropType) {
//
//            case 1:
//                ctype = 1;
//                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_white));
//                crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
//                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectangle_black));
//                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
//                crpImage.setFocusWidth(width);
//                crpImage.setFocusHeight(height);
//                crpImage.setImageURI(myUri);
//                break;
//            case 2:
//                ctype = 2;
//                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                crop_square.setBackground(getDrawable(R.drawable.circule_shape_white));
//                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
//                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
//                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square_black));
//                crpImage.setFocusWidth(width);
//                crpImage.setFocusHeight(height);
//                crpImage.setImageURI(myUri);
//                break;
//            case 3:
//                ctype = 3;
//                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_white));
//                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portrait_black));
//                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
//                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
//                crpImage.setFocusWidth(width);
//                crpImage.setFocusHeight(height);
//                crpImage.setImageURI(myUri);
//                break;
//        }
        crop_portait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Vertical
                ctype = 3;
                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_white));
                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portrait_black));
                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
                crpImage.setFocusWidth(displayMetrics.widthPixels);
                crpImage.setFocusHeight(portraitHeight);
                crpImage.setImageURI(myUri);

            }
        });
        crop_rectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rectangle
                ctype = 1;
                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_white));
                crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectangle_black));
                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
                crpImage.setFocusWidth(displayMetrics.widthPixels);
                crpImage.setFocusHeight(rectHeight);
                crpImage.setImageURI(myUri);

            }
        });
        crop_square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //square
                ctype = 2;
                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_square.setBackground(getDrawable(R.drawable.circule_shape_white));
                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square_black));
                crpImage.setFocusWidth(displayMetrics.widthPixels);
                crpImage.setFocusHeight(displayMetrics.widthPixels);
                crpImage.setImageURI(myUri);

            }
        });
    }

    private File getCropCacheFolder(Context context) {
        if (cropCacheFolder == null) {
            cropCacheFolder = new File(context.getExternalFilesDir("image") + "/crop/");
        }
        return cropCacheFolder;
    }

    private void defaultmode() {
        crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
        crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_white));
        crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
        crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
        crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectangle_black));
        crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
    }

    public int convertIntToPixel(int val) {
        Resources r = getResources();
        int deviceHeight = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, val, r.getDisplayMetrics()));
        return deviceHeight;
    }

    private void getDropboxIMGSize(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        Log.e("getDropboxIMGSize", imageHeight + "  " + imageWidth);
        int si = imageWidth - imageHeight;
        if (si < 50 && si > -50) {
            Log.i("ImageShape", "Sqier");
        } else if (si > 50) {
            Log.i("ImageShape", "Rectangle");
        } else if (si < -50) {
            Log.i("ImageShape", "Potraite");
        } else {
            Log.i("ImageShape", "else");
        }

    }
}
