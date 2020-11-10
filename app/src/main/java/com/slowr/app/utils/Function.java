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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

/**
 * Created by Admin on 05-01-2018.
 */

public class Function {

    private Gson gson = new Gson();
    public static PopupWindow showImagePopup;
    private Typeface normaltext = null, boldtext = null;
    NoInternetCallBack noInternetCallBack;
    private static final int ANIMATION_DURATION = 300;
    boolean result = false;
    public String shareMessage = "";

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

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
        return imageUri;
    }

    public static String getBase64String(String imagename) {

        Bitmap bm = BitmapFactory.decodeFile(imagename);
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
                (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
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
        this.noInternetCallBack = _noInternetCallBack;

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = null;
        Typeface normaltext = null, boldtext = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.layout_no_internet, null);
            Button btn_retry;
            btn_retry = view.findViewById(R.id.btn_try_again);
            btn_retry.setTypeface(normaltext);


            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isInternetAvailable(ctx)) {
                        showImagePopup.dismiss();
                        showImagePopup = null;
                        noInternetCallBack.isInternet();
                    } else {

                    }
                }
            });

            showImagePopup = new PopupWindow(view,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            showImagePopup.setOutsideTouchable(true);
            showImagePopup.showAtLocation(view, Gravity.CENTER, 0, 0);

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

    public static void CallNow(Activity act, String phoneNum) {
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

    public static void ShareLink(Activity ctx, String catId, String adId, String adTitle, String catGroup) {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.slowr.in/" + catId + "/" + adId))
                .setDomainUriPrefix("https://devlink.slowr.in")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.slowr.ios.beta").build())
                .buildShortDynamicLink()
                .addOnCompleteListener(ctx, new OnCompleteListener<ShortDynamicLink>() {


                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
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
                            CustomMessage(ctx, "Oops.Try again...");
                        }
                    }
                });


    }
}
