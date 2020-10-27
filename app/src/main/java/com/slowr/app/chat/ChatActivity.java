package com.slowr.app.chat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.activity.HomeActivity;
import com.slowr.app.activity.ImageViewActivity;
import com.slowr.app.activity.ReportUsActivity;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.ChatClearModel;
import com.slowr.app.models.ChatHistoryModel;
import com.slowr.app.models.ChatItemModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;
import com.slowr.matisse.Matisse;
import com.slowr.matisse.MimeType;
import com.slowr.matisse.engine.impl.GlideEngine;
import com.slowr.matisse.internal.entity.CaptureStrategy;
import com.slowr.matisse.ui.MatisseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    ImageView img_profile;
    TextView txt_prosperId;
    RecyclerView rc_chat;
    EditText edt_message;
    ImageView img_send;
    ImageView img_menu;
    ImageView img_unverified_user;
    LinearLayout layout_prosper_id;
    boolean isSend = false;
    boolean isUnVerified = false;

    HashMap<String, String> params = new HashMap<String, String>();
    private Function _fun = new Function();
    ArrayList<ChatItemModel> chatList = new ArrayList<>();
    ChatAdapter chatAdapter;

    String adId = "";
    String catId = "";
    String renterId = "";
    String messageId = "";
    String prosperId = "";
    String lastId = "";
    String proUrl = "";
    String PageFrom = "";

    String imgPath = "";
    Uri selectedImage;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 15000;
    PopupWindow spinnerPopup;
    MultipartBody.Part chatImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        doDeclaration();
    }

    private void doDeclaration() {
        catId = getIntent().getStringExtra("CatId");
        adId = getIntent().getStringExtra("AdId");
        renterId = getIntent().getStringExtra("RenterId");
        prosperId = getIntent().getStringExtra("ProsperId");
        proUrl = getIntent().getStringExtra("ProURL");
        lastId = getIntent().getStringExtra("LastId");
        isUnVerified = getIntent().getBooleanExtra("UnVerified", false);
        if (getIntent().hasExtra("PageFrom")) {
            PageFrom = getIntent().getStringExtra("PageFrom");

        }
        if (lastId != null) {
            if (!lastId.equals(""))
                messageId = lastId;
        }
        img_profile = findViewById(R.id.img_profile);
        txt_prosperId = findViewById(R.id.txt_prosperId);
        edt_message = findViewById(R.id.edt_message);
        img_send = findViewById(R.id.img_send);
        rc_chat = findViewById(R.id.rc_chat);
        img_menu = findViewById(R.id.img_menu);
        img_unverified_user = findViewById(R.id.img_unverified_user);
        layout_prosper_id = findViewById(R.id.layout_prosper_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_chat.setLayoutManager(linearLayoutManager);
        rc_chat.setItemAnimator(new DefaultItemAnimator());
        chatAdapter = new ChatAdapter(chatList, this);
        rc_chat.setAdapter(chatAdapter);

        CallBackFunction();
        sendButtonEnable();

        img_send.setOnClickListener(this);
        img_menu.setOnClickListener(this);
        layout_prosper_id.setOnClickListener(this);
        getMessageHistory(true);
        setUserDetails();
        callApiLoop();
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    private void CallBackFunction() {
        chatAdapter.setCallBack(new ChatAdapter.CallBack() {
            @Override
            public void onImageClick(int pos) {
                String imageStringArray = chatList.get(pos).getImgUrl();
                Intent a = new Intent(ChatActivity.this, ImageViewActivity.class);
                a.putExtra("ImgURL", imageStringArray);
                a.putExtra("ImgPos", 0);
                startActivity(a);
            }
        });
    }

    private void setUserDetails() {
        txt_prosperId.setText(prosperId);
        Glide.with(this)
                .load(proUrl)
                .circleCrop()
                .error(R.drawable.ic_default_profile)
                .placeholder(R.drawable.ic_default_profile)
                .into(img_profile);

        if (isUnVerified) {
            img_unverified_user.setVisibility(View.VISIBLE);
        } else {
            img_unverified_user.setVisibility(View.GONE);
        }
    }

    private void callApiLoop() {
//        handler.postDelayed(runnable = new Runnable() {
//            public void run() {
//                handler.postDelayed(runnable, delay);
//                Toast.makeText(ChatActivity.this, "This method is run every 10 seconds",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }, delay);
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                getMessageHistory(false);
            }
        };
        handler.postDelayed(runnable, delay);

//        handler.removeCallbacks(runnable);
//        handler.postDelayed(runnable, delay);

    }

    private void getMessageHistory(final boolean isLoad) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("user_id", renterId);
        params.put("category_id", catId);
        params.put("chat_id", messageId);
        params.put("ads_id", adId);

        Log.i("params", params.toString());

        RetrofitClient.getClient().create(Api.class).chatMessageHistory(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(ChatActivity.this, chatMessageHistoryApi, isLoad));

//        if (_fun.isInternetAvailable(ChatActivity.this)) {
//            RetrofitClient.getClient().create(Api.class).chatMessageHistory(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
//                    .enqueue(new RetrofitCallBack(ChatActivity.this, chatMessageHistoryApi, isLoad));
//        } else {
//            _fun.ShowNoInternetPopup(ChatActivity.this, new Function.NoInternetCallBack() {
//                @Override
//                public void isInternet() {
//                    RetrofitClient.getClient().create(Api.class).chatMessageHistory(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
//                            .enqueue(new RetrofitCallBack(ChatActivity.this, chatMessageHistoryApi, isLoad));
//                }
//            });
//        }
    }

    private void sendButtonEnable() {
        edt_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = edt_message.getText().toString().trim().length();
                if (length != 0) {
                    img_send.setImageResource(R.drawable.ic_send_active);
                    isSend = true;
                } else {
                    img_send.setImageResource(R.drawable.ic_attach);
                    isSend = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_send:
                img_send.setEnabled(false);
                if (isSend) {
                    sendMessage();
                } else {
                    addAttachMent();
                }
                break;
            case R.id.img_menu:
                PopupMenu popup = new PopupMenu(ChatActivity.this, v);
                popup.setOnMenuItemClickListener(ChatActivity.this);
                popup.inflate(R.menu.activity_chat_menu);
                popup.show();
                break;
            case R.id.layout_prosper_id:
                if (isUnVerified) {
                    ShowPopupProsper();
                }
                break;
        }
    }

    private void addAttachMent() {
        if (_fun.checkPermission(ChatActivity.this)) {
            MatisseActivity.PAGE_FROM = 2;
            Matisse.from(ChatActivity.this)
                    .choose(MimeType.of(MimeType.PNG, MimeType.JPEG), true)
//                            .choose(MimeType.of(MimeType.GIF), false)
//                            .choose(MimeType.ofAll())
                    .countable(true)
                    .capture(true)
                    .theme(R.style.Matisse_Dracula)
                    .captureStrategy(
                            new CaptureStrategy(true, "com.slowr.app.provider", "Android/data/com.slowr.app/files/Pictures"))
                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.gride_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .maxSelectable(1)
                    .showSingleMediaType(true)
                    .forResult(50);


        } else {
            img_send.setEnabled(true);
        }
    }

    private void sendMessage() {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("ads_id", adId);
        params.put("category_id", catId);
        params.put("message", edt_message.getText().toString());
        params.put("render_id", renterId);
        params.put("file", "");
        params.put("image", "");
        params.put("chat_id", messageId);
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(ChatActivity.this)) {
            RetrofitClient.getClient().create(Api.class).chatSendMessage(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ChatActivity.this, sendMessageApi, true));
        } else {
            _fun.ShowNoInternetPopup(ChatActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).chatSendMessage(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ChatActivity.this, sendMessageApi, true));
                }
            });
        }
    }

    private void clearMessage() {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("ads_id", adId);
        params.put("category_id", catId);
        params.put("chat_id", messageId);
        params.put("render_id", renterId);
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(ChatActivity.this)) {
            RetrofitClient.getClient().create(Api.class).clearChat(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ChatActivity.this, clearMessageApi, false));
        } else {
            _fun.ShowNoInternetPopup(ChatActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).clearChat(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ChatActivity.this, clearMessageApi, false));
                }
            });
        }
    }

    retrofit2.Callback<ChatHistoryModel> uploadImageApi = new retrofit2.Callback<ChatHistoryModel>() {
        @Override
        public void onResponse(Call<ChatHistoryModel> call, retrofit2.Response<ChatHistoryModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                ChatHistoryModel dr = response.body();
                if (dr.isStatus()) {
                    chatList.addAll(dr.getChatList());
                    chatAdapter.notifyDataSetChanged();
                    img_send.setEnabled(true);
                    if (chatList.size() != 0) {
                        messageId = chatList.get(chatList.size() - 1).getChatId();
                        lastId = chatList.get(chatList.size() - 1).getLastId();
                        rc_chat.smoothScrollToPosition(chatList.size() - 1);
                    }
                } else {
                    img_send.setEnabled(true);
                    Function.CustomMessage(ChatActivity.this, dr.getMessage());
                }
            } catch (Exception e) {
                img_send.setEnabled(true);
                e.printStackTrace();
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };

    retrofit2.Callback<ChatClearModel> clearMessageApi = new retrofit2.Callback<ChatClearModel>() {
        @Override
        public void onResponse(Call<ChatClearModel> call, retrofit2.Response<ChatClearModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                ChatClearModel dr = response.body();
                if (dr.isStatus()) {
                    chatList.clear();
                    chatAdapter.notifyDataSetChanged();
                    lastId = dr.getChatClearItem().getLastName();
                    messageId = dr.getChatClearItem().getLastName();
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, delay);

                } else {
                    Function.CustomMessage(ChatActivity.this, dr.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };

    retrofit2.Callback<ChatHistoryModel> sendMessageApi = new retrofit2.Callback<ChatHistoryModel>() {
        @Override
        public void onResponse(Call<ChatHistoryModel> call, retrofit2.Response<ChatHistoryModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                ChatHistoryModel dr = response.body();
                if (dr.isStatus()) {
                    edt_message.setText("");
                    chatList.addAll(dr.getChatList());
                    chatAdapter.notifyDataSetChanged();
                    img_send.setEnabled(true);
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, delay);
                    if (chatList.size() != 0) {
                        messageId = chatList.get(chatList.size() - 1).getChatId();
                        lastId = chatList.get(chatList.size() - 1).getLastId();
                        rc_chat.smoothScrollToPosition(chatList.size() - 1);
                    }
                } else {
                    img_send.setEnabled(true);
                    Function.CustomMessage(ChatActivity.this, dr.getMessage());
                }
            } catch (Exception e) {
                img_send.setEnabled(true);
                e.printStackTrace();
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
            call.cancel();
            img_send.setEnabled(true);
        }
    };

    retrofit2.Callback<ChatHistoryModel> chatMessageHistoryApi = new retrofit2.Callback<ChatHistoryModel>() {
        @Override
        public void onResponse(Call<ChatHistoryModel> call, retrofit2.Response<ChatHistoryModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                ChatHistoryModel dr = response.body();
                if (dr.isStatus()) {
                    chatList.addAll(dr.getChatList());
                    chatAdapter.notifyDataSetChanged();

                    if (chatList.size() != 0) {
                        messageId = chatList.get(chatList.size() - 1).getChatId();
                        lastId = chatList.get(chatList.size() - 1).getLastId();
                        rc_chat.smoothScrollToPosition(chatList.size() - 1);
                    }
                } else {
                    Function.CustomMessage(ChatActivity.this, dr.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50) {
            if (data != null) {
                List<String> slist = Matisse.obtainPathResult(data);
                List<Uri> slist1 = Matisse.obtainResult(data);

                if (slist.size() > 0) {
                    for (int i = 0; i < slist.size(); i++) {
                        imgPath = slist.get(i);
                        selectedImage = slist1.get(i);
                        ShowPopupImage(slist.get(i));
                    }
                }


            }
            img_send.setEnabled(true);

        }
    }

    public void ShowPopupImage(String url) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_image_view, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();

        ImageView img_profile_pic = view.findViewById(R.id.img_profile_pic);
        ViewPager vp_image_view = view.findViewById(R.id.vp_image_view);
        LinearLayout layout_back = view.findViewById(R.id.layout_back);
        LinearLayout layout_send = view.findViewById(R.id.layout_send);
        layout_send.setVisibility(View.VISIBLE);
        vp_image_view.setVisibility(View.GONE);
        img_profile_pic.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_default_profile)
                .error(R.drawable.ic_default_profile)
                .into(img_profile_pic);
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });
        layout_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
                uploadImage();
            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void uploadImage() {

//creating a file
        File file = new File(imgPath);
        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        chatImage = MultipartBody.Part.createFormData("image_upload", file.getName(), requestFile);
        final RequestBody rbAdId = RequestBody.create(okhttp3.MultipartBody.FORM, adId);
        final RequestBody rbCatId = RequestBody.create(okhttp3.MultipartBody.FORM, catId);
        final RequestBody rbRenterId = RequestBody.create(okhttp3.MultipartBody.FORM, renterId);
        final RequestBody rbMessageId = RequestBody.create(okhttp3.MultipartBody.FORM, messageId);
        final RequestBody rbMessageType = RequestBody.create(okhttp3.MultipartBody.FORM, "1");

        if (_fun.isInternetAvailable(ChatActivity.this)) {
            RetrofitClient.getClient().create(Api.class).uploadChatImage(chatImage, rbAdId, rbCatId, rbRenterId, rbMessageId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ChatActivity.this, uploadImageApi, true));
        } else {
            _fun.ShowNoInternetPopup(ChatActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).uploadChatImage(chatImage, rbAdId, rbCatId, rbRenterId, rbMessageId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ChatActivity.this, uploadImageApi, true));
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        handler = null;
        if (PageFrom.equals("2")) {
            Intent h = new Intent(ChatActivity.this, HomeActivity.class);
            h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(h);
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                clearMessage();
                break;
            case R.id.menu_report:
                Intent ru = new Intent(ChatActivity.this, ReportUsActivity.class);
                startActivity(ru);
                break;
        }
        return true;
    }

    public void ShowPopupProsper() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_popup_unverified_user, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        TextView txt_content_two = view.findViewById(R.id.txt_content_two);
        TextView txt_content = view.findViewById(R.id.txt_content);
        LinearLayout layout_content_one = view.findViewById(R.id.layout_content_one);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        layout_content_one.setVisibility(View.VISIBLE);
        txt_content.setText(getString(R.string.txt_unverified_other));
        txt_content_two.setText(getString(R.string.txt_unverified_other_two));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onRestart() {
        img_send.setEnabled(true);
        super.onRestart();
    }
}
