package com.slowr.app.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.slowr.app.activity.MyPostViewActivity;
import com.slowr.app.activity.PostViewActivity;
import com.slowr.app.activity.ProfileActivity;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Sessions;
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
                            break;
                        case "1":
                            notificationIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            notificationIntent.putExtra("PageFrom", "2");
                            break;
                    }
//                    notificationIntent.putExtra("NotificationBoardId", notificationBoardId);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                    if (notificationManager.getNotificationChannels().size() < 2) {
//                        NotificationChannel groupChannel = new NotificationChannel("bundle_channel_id", "bundle_channel_name", NotificationManager.IMPORTANCE_LOW);
//                        notificationManager.createNotificationChannel(groupChannel);
//                        NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
//                        notificationManager.createNotificationChannel(channel);
////                    }
//                }
//
//                PendingIntent pendingIntent = PendingIntent.getActivity(this, bundleNotificationId, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
//                NotificationCompat.Builder builder = null;
//                NotificationCompat.Builder Summerybuilder = null;
//                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////                media_url = media_url.replace("DefaultProfileUrl", "");
////
//
//                if (singleNotificationId == bundleNotificationId)
//                    singleNotificationId = bundleNotificationId + 1;
//                else
//                    singleNotificationId++;
//
//                PendingIntent singlePendingIntent = PendingIntent.getActivity(this, singleNotificationId, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
//                if (!media_url.equals("") && !media_url.equalsIgnoreCase("null") && media_url != null) {
//                    builder = new NotificationCompat.Builder(this, "bundle_channel_id")
////                            .setStyle(new NotificationCompat.BigTextStyle().bigText(msg_text).setBigContentTitle(msg_title))
//                            .setSmallIcon(R.drawable.ic_slowr_logo)
//                            .setLargeIcon(Picasso.with(this).load(media_url).error(R.drawable.ic_slowr_logo).get())
////                            .setCustomContentView(contentViewSmall)
////                            .setCustomBigContentView(contentViewBig)
//
//                            .setStyle(new NotificationCompat.BigTextStyle()
//                                    .bigText(msg_text))
//                            .setContentTitle(msg_title)
//                            .setContentText(msg_text)
//                            .setPriority(android.app.Notification.PRIORITY_MAX)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setGroup(bundle_notification_id)
//                            .setGroupSummary(true)
//                            .setContentIntent(pendingIntent);
//
//
//                    Summerybuilder = new NotificationCompat.Builder(this, "channel_id")
////                            .setStyle(new NotificationCompat.BigTextStyle().bigText(msg_text).setBigContentTitle(msg_title))
//                            .setSmallIcon(R.drawable.ic_slowr_logo)
//                            .setLargeIcon(Picasso.with(this).load(media_url).error(R.drawable.ic_slowr_logo).get())
////                            .setCustomContentView(contentViewSmall)
////                            .setCustomBigContentView(contentViewBig)
//                            .setStyle(new NotificationCompat.BigTextStyle()
//                                    .bigText(msg_text))
//                            .setContentTitle(msg_title)
//                            .setContentText(msg_text)
//                            .setPriority(android.app.Notification.PRIORITY_MAX)
//                            .setAutoCancel(true)
//                            .setSound(alarmSound)
//                            .setGroup(bundle_notification_id)
//                            .setGroupSummary(false)
//                            .setContentIntent(singlePendingIntent);
//
//                } else {
//                    builder = new NotificationCompat.Builder(this, "bundle_channel_id")
//                            /*  .setStyle(new NotificationCompat.BigTextStyle()
//                                      .bigText(msg_text).
//                                              setBigContentTitle(msg_title))*/
//                            .setSmallIcon(R.drawable.ic_slowr_logo)
//                            .setPriority(android.app.Notification.PRIORITY_MAX)
////                            .setCustomContentView(contentViewSmall)
//
//                            .setStyle(new NotificationCompat.BigTextStyle()
//                                    .bigText(msg_text))
//                            .setContentTitle(msg_title)
//                            .setContentText(msg_text)
//                            .setGroup(bundle_notification_id)
//                            .setGroupSummary(true)
//                            .setSound(alarmSound)
//                            .setContentIntent(pendingIntent);
//                    Summerybuilder = new NotificationCompat.Builder(this, "channel_id")
//                            /*  .setStyle(new NotificationCompat.BigTextStyle()
//                                      .bigText(msg_text).
//                                              setBigContentTitle(msg_title))*/
//                            .setSmallIcon(R.drawable.ic_slowr_logo)
//                            .setPriority(android.app.Notification.PRIORITY_MAX)
////                            .setCustomContentView(contentViewSmall)
//
//                            .setStyle(new NotificationCompat.BigTextStyle()
//                                    .bigText(msg_text))
//                            .setContentTitle(msg_title)
//                            .setContentText(msg_text)
//                            .setGroup(bundle_notification_id)
//                            .setGroupSummary(false)
//                            .setSound(alarmSound)
//                            .setContentIntent(singlePendingIntent);
//                }
//
//                notificationManager.notify(singleNotificationId, Summerybuilder.build());
//                notificationManager.notify(bundleNotificationId, builder.build());


                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, notificationIntent,
                        PendingIntent.FLAG_ONE_SHOT);

                String channelId = getString(R.string.default_notification_channel_id);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
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
                                    .setSound(defaultSoundUri)
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
                                    .setSound(defaultSoundUri)
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