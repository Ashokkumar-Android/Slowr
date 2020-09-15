package com.slowr.matisse.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.slowr.matisse.R;
import com.slowr.matisse.ViewModel;
import com.slowr.matisse.internal.entity.SelectionSpec;
import com.slowr.matisse.internal.ui.widget.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MatisseCropActivity extends AppCompatActivity {
    private static final String TAG = "MatisseCropActivity";
    private CropImageView cropImageView;

    private Context mContext;
    private File cropCacheFolder;
    private SelectionSpec mSpec;
    private CustomViewPager pager;
    ArrayList<Uri> selectedUris = new ArrayList<>();
    ArrayList<Uri> orgUris = new ArrayList<>();
    ArrayList<String> orgPaths = new ArrayList<>();
    ArrayList<String> selectedPaths = new ArrayList<>();
    TextView finish;
    ViewPagerAdapter adapter;
    int showPos = 0;
    ImageView delete_img, crop_portait, crop_rectangle, crop_square;
    ArrayList<ViewModel> ImageList = new ArrayList<>();
    ArrayList<ViewModel> orgList = new ArrayList<>();
    int deviceWidth = 0;
    int deviceHeight = 0;
    DisplayMetrics displayMetrics;
    int ViewType = 0;
    TextView cropImageViewSubmit;
    public boolean isFinish = false;
    int rectHeight = 0;
//    ArrayList<String> cropTyps = new ArrayList<>();
    int portraitHeight = 0;
    int focuswidth = 0;
    int focusheight = 0;
    String cropValues = "";
    TextView previous, Next;
    ImageView org_image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //transparent status bar
        setStatusBarFullTransparent();
        //create layout
        setContentView(R.layout.activity_crop_image);
        mContext = this;

        finish = findViewById(R.id.finish);
        pager = findViewById(R.id.pager);
        pager.disableScroll(true);

        cropImageView = findViewById(R.id.crop_image_view);
        delete_img = findViewById(R.id.delete_img);
        crop_portait = findViewById(R.id.crop_portait);
        crop_rectangle = findViewById(R.id.crop_rectangle);
        crop_square = findViewById(R.id.crop_square);
        previous = findViewById(R.id.previous);
        Next = findViewById(R.id.next);
        mSpec = SelectionSpec.getInstance();

        selectedUris.clear();
        selectedPaths.clear();
        selectedUris = getIntent().getParcelableArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION);
        selectedPaths = getIntent().getStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH);
        orgPaths = getIntent().getStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH);
//        cropTyps = getIntent().getStringArrayListExtra(MatisseActivity.EXTRA_RESULT_CROP_TYPE);
        showPos = getIntent().getIntExtra("pos", 0);
        ViewType = getIntent().getIntExtra("CROPE_VIEW_TYPE", 0);
        Log.e("uris", showPos + " " + selectedUris.toString() + "\n" + selectedPaths + "\n" + "  " + ViewType);
        /*if (selectedUris.size() > 1) {
            finish.setVisibility(View.GONE);
        } else {
            finish.setVisibility(View.VISIBLE);
        }*/

        displayMetrics = new DisplayMetrics();
        getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        double val1 = displayMetrics.widthPixels * 0.70;
        double val23 = displayMetrics.widthPixels * 1.25;
        rectHeight = (int) val1;
        portraitHeight = (int) val23;
        if (ViewType == 1) {
            deviceHeight = convertIntToPixel(250);
            deviceWidth = convertIntToPixel(250);
            delete_img.setVisibility(View.GONE);
        } else {
            deviceHeight = convertIntToPixel(250);
            displayMetrics = new DisplayMetrics();
            getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            deviceWidth = displayMetrics.widthPixels;
            double val2 = displayMetrics.heightPixels ;
            rectHeight = (int) val2;
            Log.e("displayMetrics", deviceWidth + "  " + rectHeight);
        }
        ImageList.clear();

        Uri uri = Uri.fromFile(new File(selectedPaths.get(0)));
        int type = getDropboxIMGSize(uri);
//        Log.e("selectedUrisType", type + "");
//        if (selectedUris.size() == 1) {
//            String extension = selectedPaths.get(pager.getCurrentItem()).substring(selectedPaths.get(pager.getCurrentItem()).lastIndexOf("."));
//
//            Log.e("onPageScrolled", extension);
//            if (!extension.equals(".gif")) {
//                crop_portait.setVisibility(View.VISIBLE);
//                crop_rectangle.setVisibility(View.VISIBLE);
//                crop_square.setVisibility(View.VISIBLE);
//            } else {
//                crop_portait.setVisibility(View.GONE);
//                crop_rectangle.setVisibility(View.GONE);
//                crop_square.setVisibility(View.GONE);
//            }
//            switch (type) {
//                case 1:
//                    crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                    crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_white));
//                    crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                    crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
//                    crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectangle_black));
//                    crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
//
//                    break;
//                case 2:
//                    crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                    crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                    crop_square.setBackground(getDrawable(R.drawable.circule_shape_white));
//                    crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
//                    crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
//                    crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square_black));
//
//                    break;
//                case 3:
//                    crop_portait.setBackground(getDrawable(R.drawable.circule_shape_white));
//                    crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                    crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
//                    crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portrait_black));
//                    crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
//                    crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
//
//                    break;
//            }
//        } else {
//
//            crop_portait.setVisibility(View.GONE);
//            crop_rectangle.setVisibility(View.GONE);
//            crop_square.setVisibility(View.GONE);
//        }

        switch (type) {
            case 1:
                //rectangele
                focuswidth = displayMetrics.widthPixels - (int) Math.round(convertDpToPixel(20.0f, this));
                focusheight = rectHeight;
                break;
            case 2:
                //square
                focuswidth = displayMetrics.widthPixels - (int) Math.round(convertDpToPixel(20.0f, this));
                focusheight = displayMetrics.widthPixels;
                break;
            case 3:
                //portrait
                focuswidth = displayMetrics.widthPixels - (int) Math.round(convertDpToPixel(20.0f, this));
                focusheight = portraitHeight;
                break;
        }
        for (int k = 0; k < selectedUris.size(); k++) {
//            cropTyps.set(k, String.valueOf(type));
            ImageList.add(new ViewModel(selectedUris.get(k), focuswidth, focusheight, type, false));
            orgList.add(new ViewModel(selectedUris.get(k), focuswidth, focusheight, type, false));
            orgUris.add(selectedUris.get(k));
        }

        pager.setOffscreenPageLimit(ImageList.size());
        adapter = new ViewPagerAdapter(MatisseCropActivity.this, ImageList, selectedPaths);
        pager.setAdapter(adapter);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFinish = true;
                if (selectedUris.size() > 0) {
//                    Log.e("VTYPE", cropTyps + "");
                    String extension = selectedPaths.get(pager.getCurrentItem()).substring(selectedPaths.get(pager.getCurrentItem()).lastIndexOf("."));
                    Log.e("onPageScrolled", extension);
                    if (extension.equals(".gif")) {
                        Intent result = new Intent();
                        result.putParcelableArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION, selectedUris);
                        result.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH, selectedPaths);
//                        result.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_CROP_TYPE, cropTyps);
                        setResult(RESULT_OK, result);
                        finish();
                    } else {
                        setCropOption(pager.getCurrentItem());
                    }

                } else {
                    Toast.makeText(mContext, "Kindly Choose Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.e("convertDpToPixel", convertDpToPixel(20.0f, this) + "");
        /*delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("delete_img", ImageList.size() + "");
                if (ImageList.size() > 0) {
                    selectedUris.remove(pager.getCurrentItem());
                    selectedPaths.remove(pager.getCurrentItem());
                    cropTyps.remove(pager.getCurrentItem());
                    ImageList.remove(pager.getCurrentItem());
                    adapter.notifyDataSetChanged();

                    if (ImageList.size() - 1 == pager.getCurrentItem()) {
                        finish.setVisibility(View.VISIBLE);
                        Next.setVisibility(View.GONE);
                    } else {
                        finish.setVisibility(View.GONE);
                        Next.setVisibility(View.VISIBLE);
                    }
                }
                if (ImageList.size() == 0) {
                    Intent result = new Intent();
                    result.putParcelableArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION, selectedUris);
                    result.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH, selectedPaths);
                    result.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_CROP_TYPE, cropTyps);
                    setResult(RESULT_OK, result);
                    finish();
                }
            }
        });*/

//        defaultmode(showPos);
        crop_portait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cropTyps.set(pager.getCurrentItem(),"3");
                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_white));
                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portrait_black));
                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
                ViewModel model = ImageList.get(pager.getCurrentItem());
                model.setWidth(displayMetrics.widthPixels);
                model.setHeight(portraitHeight);
                model.setImageUri(model.getImageUri());
                model.setCropType(3);
                ImageList.set(pager.getCurrentItem(), model);
                adapter.notifyDataSetChanged();
            }
        });
        crop_rectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cropTyps.set(pager.getCurrentItem(),"1");
                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_white));
                crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectangle_black));
                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
                ViewModel model = ImageList.get(pager.getCurrentItem());
                model.setWidth(displayMetrics.widthPixels);
                model.setHeight(rectHeight);
                model.setImageUri(model.getImageUri());
                model.setCropType(1);
                ImageList.set(pager.getCurrentItem(), model);
                adapter.notifyDataSetChanged();
            }
        });
        crop_square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cropTyps.set(pager.getCurrentItem(),"2");
                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_square.setBackground(getDrawable(R.drawable.circule_shape_white));
                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square_black));
                ViewModel model = ImageList.get(pager.getCurrentItem());
                model.setWidth(displayMetrics.widthPixels);
                model.setHeight(displayMetrics.widthPixels);
                model.setImageUri(model.getImageUri());
                model.setCropType(2);
                ImageList.set(pager.getCurrentItem(), model);
                adapter.notifyDataSetChanged();
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                String extension = selectedPaths.get(position).substring(selectedPaths.get(position).lastIndexOf("."));
                Log.e("onPageScrolled", extension);
                if (extension.equals(".gif")) {
                    cropImageViewSubmit.setVisibility(View.GONE);
                } else {
                    cropImageViewSubmit.setVisibility(View.GONE);

                }
            }

            @Override
            public void onPageSelected(int position) {
                String extension = selectedPaths.get(position).substring(selectedPaths.get(position).lastIndexOf("."));
                Log.e("onPageSelected", position + " " + String.valueOf(ImageList.size() - 1));
                if (extension.equals(".gif")) {
                    cropImageViewSubmit.setVisibility(View.GONE);
                } else {
                    cropImageViewSubmit.setVisibility(View.GONE);
                }
                previous.setVisibility(View.VISIBLE);
                if (position == ImageList.size() - 1) {
                    Next.setVisibility(View.GONE);
                    finish.setVisibility(View.VISIBLE);
                    if (position == 0) {
                        previous.setVisibility(View.GONE);
                    } else {
                        previous.setVisibility(View.VISIBLE);
                    }
                } else {
                    Next.setVisibility(View.VISIBLE);
                    finish.setVisibility(View.GONE);
                }
                if (position == 0) {
                    previous.setVisibility(View.GONE);
                } else {
                    previous.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("onPageScrollState", "Called");
            }
        });


        LinearLayout cropImageViewCancel = findViewById(R.id.img_profile_back);
        cropImageViewSubmit = findViewById(R.id.crop_image_view_submit);
        cropImageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        cropImageViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFinish = false;
                /*try {
                    setCropOption(pager.getCurrentItem());
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                ViewModel model = ImageList.get(pager.getCurrentItem());
                Intent cropIntent = new Intent(getApplicationContext(), ImageCropActivity.class);
                cropIntent.putExtra("image_path", selectedPaths.get(pager.getCurrentItem()));
                cropIntent.putExtra("list_pos", pager.getCurrentItem());
                cropIntent.putExtra("image_height", model.getHeight());
                cropIntent.putExtra("image_width", model.getWidth());
                cropIntent.putExtra("cropType", model.getCropType());
                startActivityForResult(cropIntent, 10);
            }
        });
        if (selectedPaths.size() > 0) {
            String extension = selectedPaths.get(showPos).substring(selectedPaths.get(showPos).lastIndexOf("."));
            if (extension.equals(".gif")) {
                cropImageViewSubmit.setVisibility(View.GONE);
            } else {
                cropImageViewSubmit.setVisibility(View.GONE);
            }
        }
        pager.setCurrentItem(showPos);
        if (showPos == 0) {
            previous.setVisibility(View.GONE);
        }
        if (selectedUris.size() == 1) {
            previous.setVisibility(View.GONE);
            Next.setVisibility(View.GONE);
            finish.setVisibility(View.VISIBLE);
        }
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFinish = false;
                String extension = selectedPaths.get(pager.getCurrentItem()).substring(selectedPaths.get(pager.getCurrentItem()).lastIndexOf("."));
                Log.e("Next", extension + "\n" + pager.getCurrentItem());
                if (extension.equals(".gif")) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                } else {
                    setCropOption(pager.getCurrentItem());
                }
            }
        });

    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void defaultmode(int pos) {

        ViewModel model = ImageList.get(pos);
        switch (model.getCropType()) {
            case 1:
                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_white));
                crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectangle_black));
                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
                break;
            case 2:

                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_square.setBackground(getDrawable(R.drawable.circule_shape_white));
                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portarit));
                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square_black));
                break;
            case 3:

                crop_portait.setBackground(getDrawable(R.drawable.circule_shape_white));
                crop_rectangle.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_square.setBackground(getDrawable(R.drawable.circule_shape_orange));
                crop_portait.setImageDrawable(getDrawable(R.drawable.ic_crop_portrait_black));
                crop_rectangle.setImageDrawable(getDrawable(R.drawable.ic_crop_rectable));
                crop_square.setImageDrawable(getDrawable(R.drawable.ic_crop_square));
                break;

        }

    }

    private File getCropCacheFolder(Context context) {
        if (cropCacheFolder == null) {
            cropCacheFolder = new File(context.getExternalFilesDir("image") + "/crop/");
        }
        return cropCacheFolder;
    }

    private void setStatusBarFullTransparent() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void loadorgImage(int position) {
        ViewModel models = orgList.get(position);
        Log.e("org_image", position + " " + models.getImageUri() + " \n " + orgList.get(position).getImageUri() + " " + orgList.size());
        ViewModel newModel = new ViewModel(orgList.get(position).getImageUri(), orgList.get(position).getWidth(),
                orgList.get(position).getHeight(), orgList.get(position).getCropType(), orgList.get(position).isCropped());
        ImageList.set(position, newModel);
        adapter.notifyDataSetChanged();
       /* ImageList.set(position, models);
        selectedUris.set(position, orgUris.get(position));
        selectedPaths.set(position, orgPaths.get(position));
        cropTyps.set(position, String.valueOf(models.getCropType()));
        adapter.notifyDataSetChanged();*/
    }

    public class ViewPagerAdapter extends PagerAdapter {
        Context c;
        private ArrayList<ViewModel> _imagePaths;
        private LayoutInflater inflater;
        HashMap<Integer, ImageView> views = new HashMap();
        ArrayList<String> paths = new ArrayList<>();

        public ViewPagerAdapter(Context c, ArrayList<ViewModel> imagePaths, ArrayList<String> selectedPath) {
            this._imagePaths = imagePaths;
            this.c = c;
            this.paths = selectedPath;
        }

        @Override
        public int getCount() {
            return this._imagePaths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.viewpager_cropper_layout, container,
                    false);
            String extension = "";
            final ViewModel Model = _imagePaths.get(position);
            final CropImageView imgDisplay = viewLayout.findViewById(R.id.crop_image_view);
            final ImageView gifView = viewLayout.findViewById(R.id.gif_view);
            final ImageView org_image = viewLayout.findViewById(R.id.org_image);
            final ImageView delete_img = viewLayout.findViewById(R.id.delete_img);
            if (position < _imagePaths.size()) {
                extension = paths.get(position).substring(paths.get(position).lastIndexOf("."));
            }
            if(_imagePaths.size()==1){
                delete_img.setVisibility(View.GONE);
            }else{
                delete_img.setVisibility(View.VISIBLE);
            }

//          String extension =getMimeType(MatisseCropActivity.this,Model.getImageUri());
            gifView.setVisibility(View.VISIBLE);
            imgDisplay.setVisibility(View.GONE);

            Log.e("instantiateItem", Model.isCropped() + "");
            if (Model.isCropped()) {
                org_image.setVisibility(View.VISIBLE);
            } else {
                org_image.setVisibility(View.GONE);
            }
            org_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.e("org_image", position + "\n" + orgList.get(position).getImageUri() + " " + orgList.size());
                    loadorgImage(pager.getCurrentItem());

                }
            });
            delete_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("delete_img", ImageList.size() + "");
                    deleteList(pager.getCurrentItem());
                }
            });
           /* final Handler handler = new Handler();
            Glide.with(MatisseCropActivity.this)
                    .load(Model.getImageUri())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(MatisseCropActivity.this)
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

            Log.e("extension", extension);*/
            if (extension.equals(".gif") || extension.equals("gif")) {
                gifView.setVisibility(View.VISIBLE);
                imgDisplay.setVisibility(View.GONE);
                final Handler handler = new Handler();
                Glide.with(MatisseCropActivity.this)
                        .load(Model.getImageUri())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(MatisseCropActivity.this)
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
            }
            views.put(position, imgDisplay);
            Log.e("viewLayout", "View" + position);
            viewLayout.setTag("View" + position);
            container.addView(viewLayout);
            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
            views.remove(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }

    public void deleteList(int pos) {
        if (ImageList.size() > 0) {
            selectedUris.remove(pos);
            selectedPaths.remove(pos);
//            cropTyps.remove(pos);
            ImageList.remove(pos);
            orgList.remove(pos);
            orgUris.remove(pos);
//            orgPaths.remove(pos);
            adapter.notifyDataSetChanged();
        }
        if (pos == ImageList.size() - 1) {
            Next.setVisibility(View.GONE);
            finish.setVisibility(View.VISIBLE);
            if (pos == 0) {
                previous.setVisibility(View.GONE);
            } else {
                previous.setVisibility(View.VISIBLE);
            }
        }
        if (ImageList.size() == 0) {
            Intent result = new Intent();
            result.putParcelableArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION, selectedUris);
            result.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH, selectedPaths);
//            result.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_CROP_TYPE, cropTyps);
            setResult(RESULT_OK, result);
            finish();
        }
    }

    public int convertIntToPixel(int val) {
        Resources r = getResources();
        int deviceHeight = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, val, r.getDisplayMetrics()));
        return deviceHeight;
    }

    public void setCropOption(final int pos) {
        Log.e("setCropOption", pos + "  " + selectedUris.size());
        adapter = ((ViewPagerAdapter) pager.getAdapter());
        View myView = pager.findViewWithTag("View" + pos);
        final CropImageView imgDisplay = myView.findViewById(R.id.crop_image_view);
        imgDisplay.saveBitmapToFile(getCropCacheFolder(mContext), imgDisplay.getFocusWidth(), imgDisplay.getFocusHeight(), false);
        imgDisplay.setOnBitmapSaveCompleteListener(new CropImageView.OnBitmapSaveCompleteListener() {
            @Override
            public void onBitmapSaveSuccess(File file) {
                Log.e("onBitmapSaveSuccess", file.toString());
                selectedUris.set(pos, Uri.parse(file.getPath()));
                selectedPaths.set(pos, file.getPath());
                ViewModel model = ImageList.get(pos);
                model.setWidth(model.getWidth());
                model.setHeight(model.getHeight());
                model.setImageUri(Uri.parse(file.getPath()));
                model.setCropType(model.getCropType());
                model.setCropped(true);
                ImageList.set(pos, model);
//                cropTyps.set(pos, String.valueOf(model.getCropType()));
                Toast.makeText(mContext, "Image Cropped", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        if (isFinish) {
                            Intent result = new Intent();
                            result.putParcelableArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION, selectedUris);
                            result.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH, selectedPaths);
//                            result.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_CROP_TYPE, cropTyps);
                            setResult(RESULT_OK, result);
                            finish();

                        } else {
                            adapter.notifyDataSetChanged();
                            pager.setCurrentItem(pager.getCurrentItem() + 1);
                        }

                    }
                }, 200);

            }

            @Override
            public void onBitmapSaveError(File file) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                int updatedPos = data.getIntExtra("pos", 0);
                String path = data.getStringExtra("path");
                int ctype = data.getIntExtra("ctype", 0);
                String cropValue = String.valueOf(ctype);
                Log.e("onActivityResult", ctype + " " + cropValue);
                ViewModel model = ImageList.get(updatedPos);
                ViewModel model1 = new ViewModel(Uri.parse(path), model.getWidth(), model.getHeight(), ctype, false);
                ImageList.set(updatedPos, model1);
                selectedPaths.set(updatedPos, path);
                selectedUris.set(updatedPos, Uri.parse(path));
//                cropTyps.set(updatedPos, cropValue);

                adapter.notifyDataSetChanged();
                pager.setCurrentItem(updatedPos);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    private int getDropboxIMGSize(final Uri uri) {
        Log.e("getDropboxIMGSize", uri + "");
        int ctype = 0;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        Log.e("getDropboxIMGSize", imageHeight + "  " + imageWidth);
        int si = imageWidth - imageHeight;
        if (si < 50 && si > -50) {
            Log.i("ImageShape", "Sqier");
            ctype = 2;
        } else if (si >= 50) {
            Log.i("ImageShape", "Rectangle");
            ctype = 1;
        } else if (si < -50) {
            Log.i("ImageShape", "Potraite");
            ctype = 3;
        } else {
            Log.i("ImageShape", "else");
            ctype = 1;
        }
        return ctype;
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
