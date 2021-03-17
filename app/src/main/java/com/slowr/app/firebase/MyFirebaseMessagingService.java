package com.slowr.app.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.slowr.app.R;
import com.slowr.app.activity.BannerActivity;
import com.slowr.app.activity.BaseActivity;
import com.slowr.app.activity.MyPostViewActivity;
import com.slowr.app.activity.ProfileActivity;
import com.slowr.app.activity.TransactionActivity;
import com.slowr.app.chat.ChatActivity;
import com.squareup.picasso.Picasso;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";
    private Gson gson = new Gson();
    private String notificationType = "";
    int notifyID = 1;
    Intent notificationIntent;
    String CHANNEL_ID = "my_channel_01";// The id of the channel.
    CharSequence name = "Slowr";
    NotificationManager notificationManager;

    int importance = NotificationManager.IMPORTANCE_HIGH;

    private String userCurrentScreenId = "0";
    public static final String INTENT_FILTER = "INTENT_FILTER";
    RemoteViews contentViewBig, contentViewSmall;
    String channelId = "8608";
    private int notificationId = 1;
    int bundleNotificationId = 100;
    int singleNotificationId = 100;
    NotificationCompat.Builder summaryNotificationBuilder;
    NotificationCompat.Builder notificationBuilder;
    String bundle_notification_id = "";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.e("onMessageReceived", remoteMessage.getData().toString());

            if (remoteMessage.getData() != null) {
                String msg_text = "", msg_title = "Slowr", media_url = "", notificationBoardId = "";
                try {
                    String type = remoteMessage.getData().get("type");
                    if (remoteMessage.getData().containsKey("image")) {
                        media_url = remoteMessage.getData().get("image");
                    }
                    msg_text = remoteMessage.getData().get("body");
                    msg_title = remoteMessage.getData().get("title");
//                    notificationId = Integer.valueOf("1");
                    notificationId = Integer.valueOf(type);
                    bundleNotificationId = notificationId;

                    bundle_notification_id = "bundle_notification_" + bundleNotificationId;
                    singleNotificationId++;
                    switch (type) {
                        case "2":
                            //Post
//                            String userId = remoteMessage.getData().get("user_id");
//                            if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
                            notificationIntent = new Intent(getApplicationContext(), MyPostViewActivity.class);
//                            } else {
//                                notificationIntent = new Intent(getApplicationContext(), PostViewActivity.class);
//                            }
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            notificationIntent.putExtra("PageFrom", "2");
                            notificationIntent.putExtra("CatId", remoteMessage.getData().get("category_id"));
                            notificationIntent.putExtra("AdId", remoteMessage.getData().get("ads_id"));
                            notificationIntent.putExtra("NotificationId", remoteMessage.getData().get("notification_id"));
                            break;
                        case "1":
                            notificationIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            notificationIntent.putExtra("PageFrom", "2");
                            notificationIntent.putExtra("NotificationId", remoteMessage.getData().get("notification_id"));
                            break;
                        case "3":
                            notificationIntent = new Intent(getApplicationContext(), ChatActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            notificationIntent.putExtra("PageFrom", "2");
                            notificationIntent.putExtra("CatId", remoteMessage.getData().get("category_id"));
                            notificationIntent.putExtra("AdId", remoteMessage.getData().get("ads_id"));
                            notificationIntent.putExtra("RenterId", remoteMessage.getData().get("user_id"));
                            notificationIntent.putExtra("ProsperId", remoteMessage.getData().get("prosper_id"));
                            notificationIntent.putExtra("ProURL", remoteMessage.getData().get("profile_image"));
                            notificationIntent.putExtra("LastId", remoteMessage.getData().get("last_id"));
                            media_url = remoteMessage.getData().get("profile_image");
                            if (remoteMessage.getData().get("is_verified").equals("0")) {
                                notificationIntent.putExtra("UnVerified", true);
                            } else {
                                notificationIntent.putExtra("UnVerified", false);
                            }

                            break;
                        case "4":
                            notificationIntent = new Intent(getApplicationContext(), TransactionActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            notificationIntent.putExtra("PageFrom", "2");
                            notificationIntent.putExtra("NotificationId", remoteMessage.getData().get("notification_id"));
                            break;
                        case "5":
                            notificationIntent = new Intent(getApplicationContext(), BannerActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            notificationIntent.putExtra("PageFrom", "2");
                            notificationIntent.putExtra("NotificationId", remoteMessage.getData().get("notification_id"));
                            break;
                    }
//                    notificationIntent.putExtra("NotificationBoardId", notificationBoardId);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }



                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, notificationIntent,
                        PendingIntent.FLAG_ONE_SHOT);

                String channelId = getString(R.string.default_notification_channel_id);
                Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.slowr_tone);
//                Uri defaultSoundUri = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/slowr_tone.mp3" ) ;

                if (!media_url.equals("") && !media_url.equalsIgnoreCase("null") && media_url != null) {
                    notificationBuilder =
                            new NotificationCompat.Builder(this, channelId)
                                    .setSmallIcon(R.drawable.ic_slowr_logo)
                                    .setLargeIcon(Picasso.with(this).load(media_url).error(R.drawable.ic_slowr_logo).get())
//                            .setCustomContentView(contentViewSmall)
//                            .setCustomBigContentView(contentViewBig)

                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(msg_text))
                                    .setContentTitle(msg_title)
                                    .setContentText(msg_text)
                                    .setAutoCancel(true)
                                    .setContentIntent(pendingIntent);
                } else {
                    notificationBuilder =
                            new NotificationCompat.Builder(this, channelId)
                                    .setSmallIcon(R.drawable.ic_slowr_logo)
                                    .setLargeIcon(Picasso.with(this).load(R.drawable.ic_slowr_logo).error(R.drawable.ic_slowr_logo).get())
//                            .setCustomContentView(contentViewSmall)
//                            .setCustomBigContentView(contentViewBig)

                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(msg_text))
                                    .setContentTitle(msg_title)
                                    .setContentText(msg_text)
                                    .setAutoCancel(true)
                                    .setContentIntent(pendingIntent);
                }
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify((int) (System.currentTimeMillis()) /* ID of notification */, notificationBuilder.build());
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), defaultSoundUri);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (BaseActivity.instance != null) {
                    BaseActivity.instance.callUnreadCount();
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}