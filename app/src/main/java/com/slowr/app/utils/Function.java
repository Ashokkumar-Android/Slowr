package com.slowr.app.utils;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.slowr.app.R;
import com.slowr.app.components.ViewDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

public class Function {

    private Gson gson = new Gson();
    public static PopupWindow showImagePopup;
    private Typeface normaltext = null, boldtext = null;
    NoInternetCallBack noInternetCallBack;
    private static final int ANIMATION_DURATION = 300;
    boolean result = false;
    public String shareMessage = "";

    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;

    public boolean isInternetAvailable(Context cont) {
        boolean isInt = false;
//        ConnectivityManager connectivityManager = (ConnectivityManager) cont.getSystemService(cont.getApplicationContext().CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        ConnectivityManager cm = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                isInt = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                isInt = true;      // connected to mobile data
            }
        } else {
            isInt = false; // not connected to the internet
        }
        return isInt;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public boolean checkPermission(Activity activity) {
        boolean result = false;
        for (int i = 0; i < Constant.Permissions.length; i++) {
            int result1 = ContextCompat.checkSelfPermission(activity, Constant.Permissions[i]);
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        if (!result) {
            ActivityCompat.requestPermissions(activity, Constant.Permissions, Constant.PERMISSION_REQUEST_CODE);
        }
        return result;
    }

    public boolean checkPermission2(Activity activity) {
        boolean result = false;
        for (int i = 0; i < Constant.Permissions2.length; i++) {
            int result1 = ContextCompat.checkSelfPermission(activity, Constant.Permissions2[i]);
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        if (!result) {
            ActivityCompat.requestPermissions(activity, Constant.Permissions2, Constant.PERMISSION_REQUEST_CODE);

        }
        return result;
    }

    public boolean checkPermissionLocation(Activity activity) {
        boolean result = false;
        for (int i = 0; i < Constant.LocationPermissions.length; i++) {
            int result1 = ContextCompat.checkSelfPermission(activity, Constant.LocationPermissions[i]);
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        if (!result) {
            ActivityCompat.requestPermissions(activity, Constant.LocationPermissions, Constant.PERMISSION_REQUEST_CODE);

        }
        return result;
    }

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
        return imageUri;
    }

    public static String getBase64String(String imagename) {
        String file = compressImage(imagename);

        Bitmap bm = BitmapFactory.decodeFile(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object

        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Date getDateTime(String dateTime) {
        Date d = null;
//        2020-08-15T04:32:40.000000Z
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
        try {
            d = sdf.parse(dateTime);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        return d;
    }

    public static void hideSoftKeyboard(Activity activity, View v) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                v.getWindowToken(), 0);
    }

    public static void openSoftKeyboard(Activity activity, View v) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                v.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

//    public static void applyFontToMenuItem(MenuItem mi, Typeface ty) {
//        SpannableString mNewTitle = new SpannableString(mi.getTitle());
//        mNewTitle.setSpan(new CustomTypeFaceSpan("", ty, Color.BLACK), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mi.setTitle(mNewTitle);
//    }


    public void hideSoftKeyboard(EditText view, Activity activity) {

        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), HIDE_NOT_ALWAYS);
            }
        }

    }

    public void showSoftKeyboard(EditText view, Context ctx) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public static void bounceAnimateImageView(final boolean lik, final ImageView img_feed_like) {
        try {
            AnimatorSet animatorSet = new AnimatorSet();

            ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(img_feed_like, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(ANIMATION_DURATION);
            bounceAnimX.setInterpolator(new BounceInterpolator());

            ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(img_feed_like, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(ANIMATION_DURATION);
            bounceAnimY.setInterpolator(new BounceInterpolator());
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    img_feed_like.setImageResource(!lik ? R.drawable.ic_favorite
                            : R.drawable.ic_favorite);

                }
            });

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                }
            });

            animatorSet.play(bounceAnimX).with(bounceAnimY);
            animatorSet.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowNoInternetPopup(final Activity ctx, NoInternetCallBack _noInternetCallBack) {
        try {


            this.noInternetCallBack = _noInternetCallBack;
            CustomMessageInternet(ctx, ctx.getString(R.string.txt_no_connection));
//        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        View view = null;
//        Typeface normaltext = null, boldtext = null;
//        if (inflater != null) {
//            view = inflater.inflate(R.layout.layout_no_internet, null);
//            Button btn_retry;
//            btn_retry = view.findViewById(R.id.btn_try_again);
//            btn_retry.setTypeface(normaltext);
//
//
//            btn_retry.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isInternetAvailable(ctx)) {
//                        showImagePopup.dismiss();
//                        showImagePopup = null;
//                        noInternetCallBack.isInternet();
//                    } else {
//
//                    }
//                }
//            });
//
//            showImagePopup = new PopupWindow(view,
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT);
//            showImagePopup.setOutsideTouchable(true);
//            showImagePopup.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNoInternetCallBack(NoInternetCallBack noInternetCallBack) {
        this.noInternetCallBack = noInternetCallBack;
    }

    public interface NoInternetCallBack {
        void isInternet();
    }

    public static void CustomMessage(Activity ctx, String message) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_custom_toast, ctx.findViewById(R.id.custom_toast_layout));
        TextView tv = layout.findViewById(R.id.txt_toast_message);
        tv.setText(message);
        Toast toast = new Toast(ctx);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void CustomMessageInternet(Activity ctx, String message) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_custom_toast_internet, ctx.findViewById(R.id.custom_toast_layout));
        TextView tv = layout.findViewById(R.id.txt_toast_message);
        tv.setTextSize(20);
        tv.setText(message);
        Toast toast = new Toast(ctx);
        toast.setGravity(Gravity.BOTTOM, 0, 500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void CallNow(Activity act, String phoneNum) {
        if (phoneNum != null && !phoneNum.equals("")) {
            String uri = "tel:" + phoneNum.trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            act.startActivity(intent);
        }
    }

    public static void GradientBgSet(View vi, String colorOne, String colorTwo) {
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{
                Color.parseColor(colorOne),
                Color.parseColor(colorTwo)
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
        vi.setBackground(gd);
    }

    public static void ShareLink(Activity ctx, String adId, String adTitle, String catGroup, String imgUrl) {
        ViewDialog viewDialog;
        viewDialog = new ViewDialog(ctx);
        viewDialog.showDialog();

        DynamicLink.SocialMetaTagParameters.Builder builder = new DynamicLink.SocialMetaTagParameters.Builder();
        builder.setTitle("www.slowr.com");
        builder.setDescription("Rent Anything Hire Anybody \"Temporarily\"");
        builder.setImageUrl(Uri.parse(imgUrl));

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setSocialMetaTagParameters(builder.build())
                .setLink(Uri.parse("https://www.slowr.com/ad-details/" + adId))
                .setDomainUriPrefix("https://appslowr.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.app.slowr.dev").build())
                .buildShortDynamicLink()
                .addOnCompleteListener(ctx, new OnCompleteListener<ShortDynamicLink>() {

                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            viewDialog.hideDialog();
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.i("Share Link", String.valueOf(shortLink));
                            String shareMessage;
                            try {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Slowr");
                                if (catGroup.equals("1")) {
                                    shareMessage = "\n" + ctx.getString(R.string.txt_rent_temp) + "\n" + adTitle + "\n";
                                } else {
                                    shareMessage = "\n" + ctx.getString(R.string.txt_hire_temp) + "\n" + adTitle + "\n";
                                }
                                shareMessage = shareMessage + shortLink;
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                ctx.startActivity(Intent.createChooser(shareIntent, "choose one"));
                            } catch (Exception e) {
                                //e.toString();
                            }
                        } else {
                            viewDialog.hideDialog();
                            CustomMessage(ctx, "Oops.Try again...");
                        }
                    }
                });


    }

    public static void ShareProfileLink(Activity ctx, String prosperId, String imgUrl) {
        ViewDialog viewDialog;
        viewDialog = new ViewDialog(ctx);
        viewDialog.showDialog();

        DynamicLink.SocialMetaTagParameters.Builder builder = new DynamicLink.SocialMetaTagParameters.Builder();
        builder.setTitle("www.slowr.com");
        builder.setDescription("Rent Anything Hire Anybody \"Temporarily\"");
        builder.setImageUrl(Uri.parse(imgUrl));

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setSocialMetaTagParameters(builder.build())
                .setLink(Uri.parse("https://www.slowr.com/" + prosperId))
                .setDomainUriPrefix("https://slowrprofile.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.app.slowr.dev").build())
                .buildShortDynamicLink()
                .addOnCompleteListener(ctx, new OnCompleteListener<ShortDynamicLink>() {

                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            viewDialog.hideDialog();
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.i("Share Link", String.valueOf(shortLink));
                            String shareMessage;
                            try {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Slowr");

                                shareMessage = "\n" + ctx.getString(R.string.txt_profile_share_content) + " " + prosperId + "\n";

                                shareMessage = shareMessage + shortLink;
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                ctx.startActivity(Intent.createChooser(shareIntent, "choose one"));
                            } catch (Exception e) {
                                //e.toString();
                            }
                        } else {
                            viewDialog.hideDialog();
                            CustomMessage(ctx, "Oops.Try again...");
                        }
                    }
                });


    }

    public static String compressImage(String imagePath) {
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
//        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bmp, middleX - (float) bmp.getWidth() / 2.0f, middleY - (float) bmp.getHeight() / 2.0f, new Paint(Paint.FILTER_BITMAP_FLAG));
        if (bmp != null) {
            bmp.recycle();
        }
        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filepath = getFilename();
        try {
            //new File(imageFilePath).delete();
            out = new FileOutputStream(filepath);

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filepath;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static String getFilename() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Slowr/Images");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + System.currentTimeMillis() + ".jpg";
        String uriString = (mediaStorageDir.getAbsolutePath() + "/" + mImageName);
        return uriString;
    }

    public static int getAppVersionCode(Context myactivity) {
        int versioncode = 1;
        PackageManager manager = myactivity.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    myactivity.getPackageName(), 0);
            versioncode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versioncode;
    }

    public static String getAppVersionName(Context myactivity) {
        String versionname = "";
        PackageManager manager = myactivity.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    myactivity.getPackageName(), 0);
            versionname = info.versionName + "(" + info.versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionname;
    }

    public static void SetRentalPrice(String adFee, String adRentalDuration, TextView txt_price, String catGroup, Context ctx) {
        if (adFee != null) {
            txt_price.setVisibility(View.VISIBLE);
            String price = "";
            if (adFee.contains(".")) {
                String[] tempPrice = adFee.split("\\.");
                price = tempPrice[0];
            } else {
                price = adFee;
            }

            if (price.equals("0") || price.equals("") || adRentalDuration.equals("Custom")) {
                Function.RentalDurationText(txt_price, catGroup, adRentalDuration, ctx);
            } else {
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String formatPrice = formatter.format(Integer.valueOf(price));

                if (adRentalDuration.equals("Per Hour")) {
                    txt_price.setText("₹ " + formatPrice + " / Hour");
                } else if (adRentalDuration.equals("Per Day")) {
                    txt_price.setText("₹ " + formatPrice + " / Day");
                } else if (adRentalDuration.equals("Per Week")) {
                    txt_price.setText("₹ " + formatPrice + " / Week");

                } else if (adRentalDuration.equals("Per Month")) {
                    txt_price.setText("₹ " + formatPrice + " / Month");
                } else if (adRentalDuration.equals("Per Ride")) {
                    txt_price.setText("₹ " + formatPrice + " / Ride");
                } else {
                    txt_price.setText("₹ " + formatPrice + " / Hour");
                }

            }
        } else {
            Function.RentalDurationText(txt_price, catGroup, adRentalDuration, ctx);
        }
    }

    public static void RentalDurationText(TextView txtRental, String catGroup, String rentalDuration, Context ctx) {
        if (catGroup.equals("1")) {
            if (rentalDuration.equals("Custom")) {
                txtRental.setText(ctx.getString(R.string.custom_rent));
            } else if (rentalDuration.equals("Per Hour")) {
                txtRental.setText(ctx.getString(R.string.hour_rent));
            } else if (rentalDuration.equals("Per Day")) {
                txtRental.setText(ctx.getString(R.string.day_rent));
            } else if (rentalDuration.equals("Per Week")) {
                txtRental.setText(ctx.getString(R.string.week_rent));
            } else if (rentalDuration.equals("Per Month")) {
                txtRental.setText(ctx.getString(R.string.month_rent));
            } else if (rentalDuration.equals("Per Ride")) {
                txtRental.setText(ctx.getString(R.string.ride_rent));
            }
        } else {
            if (rentalDuration.equals("Custom")) {
                txtRental.setText(ctx.getString(R.string.custom_hire));
            } else if (rentalDuration.equals("Per Hour")) {
                txtRental.setText(ctx.getString(R.string.hour_hire));
            } else if (rentalDuration.equals("Per Day")) {
                txtRental.setText(ctx.getString(R.string.day_hire));
            } else if (rentalDuration.equals("Per Week")) {
                txtRental.setText(ctx.getString(R.string.week_hire));
            } else if (rentalDuration.equals("Per Month")) {
                txtRental.setText(ctx.getString(R.string.month_hire));
            } else if (rentalDuration.equals("Per Ride")) {
                txtRental.setText(ctx.getString(R.string.ride_hire));
            }
        }
    }


    public static void CoinTone(Activity ctx) {
        try {
            Uri defaultSoundUri = Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.raw.slowr_tone);
            Ringtone r = RingtoneManager.getRingtone(ctx, defaultSoundUri);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean GSTNoValidation(String val) {
        String gstNoPattern = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
        Pattern p = Pattern.compile(gstNoPattern);
        Matcher m = p.matcher(val);

        return m.matches();
    }

    public static boolean GSTNameValidation(String val) {
        String gstNoPattern = "^(s?.?[a-zA-Z]+.+[^,@])+$";
        Pattern p = Pattern.compile(gstNoPattern);
        Matcher m = p.matcher(val);

        return m.matches();
    }

    public static boolean ProsperIdValidation(String val) {
        String gstNoPattern = "[a-zA-Z]{2}\\d{4}";
        Pattern p = Pattern.compile(gstNoPattern);
        Matcher m = p.matcher(val);

        return m.matches();
    }
}
