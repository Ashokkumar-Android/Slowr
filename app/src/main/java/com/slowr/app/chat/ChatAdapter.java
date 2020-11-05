package com.slowr.app.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.models.ChatItemModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Sessions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatItemModel> chatList;
    Context ctx;
    CallBack callBack;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_right_message;
        TextView txt_right_time;
        TextView txt_left_message;
        TextView txt_left_time;
        TextView date_time;
        TextView txt_chat_warning;
        LinearLayout layout_txt_right;
        LinearLayout layout_txt_left;
        LinearLayout date_container;
        ImageView img_right_chat;
        ImageView img_left_chat;

        public MyViewHolder(View view) {
            super(view);
            txt_right_message = view.findViewById(R.id.txt_right_message);
            txt_right_time = view.findViewById(R.id.txt_right_time);
            layout_txt_right = view.findViewById(R.id.layout_txt_right);
            layout_txt_left = view.findViewById(R.id.layout_txt_left);
            date_container = view.findViewById(R.id.date_container);
            txt_left_message = view.findViewById(R.id.txt_left_message);
            txt_left_time = view.findViewById(R.id.txt_left_time);
            img_right_chat = view.findViewById(R.id.img_right_chat);
            img_left_chat = view.findViewById(R.id.img_left_chat);
            date_time = view.findViewById(R.id.date_time);
            txt_chat_warning = view.findViewById(R.id.txt_chat_warning);
            img_right_chat.setOnClickListener(this);
            img_left_chat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_right_chat:
                    callBack.onImageClick(getAdapterPosition());
                    break;
                case R.id.img_left_chat:
                    callBack.onImageClick(getAdapterPosition());
                    break;
            }

        }
    }

    public ChatAdapter(List<ChatItemModel> _categoryList, Context ctx) {
        this.chatList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chat_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatItemModel chatModel = chatList.get(position);
        if (position == 0) {
            holder.txt_chat_warning.setVisibility(View.VISIBLE);
        } else {
            holder.txt_chat_warning.setVisibility(View.GONE);
        }
        String userId = Sessions.getSession(Constant.UserId, ctx);
        if (userId.equals(chatModel.getUserId())) {
            holder.layout_txt_right.setVisibility(View.VISIBLE);
            holder.layout_txt_left.setVisibility(View.GONE);
            holder.txt_right_message.setText(chatModel.getChatMessage());
            holder.txt_right_time.setText(chatModel.getChatTime());
            if (chatModel.getImgUrl() != null) {
                if (!chatModel.getImgUrl().equals("")) {
                    holder.txt_right_message.setVisibility(View.GONE);
                    holder.img_right_chat.setVisibility(View.VISIBLE);
                    Glide.with(ctx)
                            .load(chatModel.getImgUrl())
                            .placeholder(R.drawable.ic_default_horizontal)
                            .error(R.drawable.ic_default_horizontal)
                            .into(holder.img_right_chat);
                }
            } else {
                holder.txt_right_message.setVisibility(View.VISIBLE);
                holder.img_right_chat.setVisibility(View.GONE);
            }
        } else {
            holder.layout_txt_right.setVisibility(View.GONE);
            holder.layout_txt_left.setVisibility(View.VISIBLE);
            holder.txt_left_message.setText(chatModel.getChatMessage());
            holder.txt_left_time.setText(chatModel.getChatTime());
            if (chatModel.getImgUrl() != null) {
                if (!chatModel.getImgUrl().equals("")) {
                    holder.txt_left_message.setVisibility(View.GONE);
                    holder.img_left_chat.setVisibility(View.VISIBLE);
                    Glide.with(ctx)
                            .load(chatModel.getImgUrl())
                            .placeholder(R.drawable.ic_default_horizontal)
                            .error(R.drawable.ic_default_horizontal)
                            .into(holder.img_left_chat);
                }
            } else {
                holder.txt_left_message.setVisibility(View.VISIBLE);
                holder.img_left_chat.setVisibility(View.GONE);
            }
        }
        if (position > 0) {
            if (chatModel.getChatDate().equalsIgnoreCase(chatList.get(position - 1).getChatDate())) {
                holder.date_container.setVisibility(View.GONE);
            } else {
                holder.date_container.setVisibility(View.VISIBLE);
                setDate(chatModel.getChatDate(), holder.date_time);
            }
        } else {
            holder.date_container.setVisibility(View.VISIBLE);
            setDate(chatModel.getChatDate(), holder.date_time);
        }

//        Glide.with(ctx)
//                .load(movie.getPhotoType())
//                .error(R.drawable.ic_default_horizontal)
//                .placeholder(R.drawable.ic_default_horizontal)
//                .into(holder.img_ad);

    }

    private void setDate(String chatDate, TextView datetxt) {
        Log.e("setDate", chatDate);
        DateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        SimpleDateFormat dFormat2 = new SimpleDateFormat("dd mm yyyy");
        String[] dataTime = chatDate.split("-");
        Log.e("setDate", dataTime.toString());
//        Date c_date = null;
//        try {
//            c_date = new SimpleDateFormat("mm", Locale.ENGLISH).parse(dataTime[1]);
//        } catch (ParseException e1) {
//            e1.printStackTrace();
//        }
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(c_date);
//        int month = cal.get(Calendar.MONTH);

        Calendar c = Calendar.getInstance();
        Calendar n = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dataTime[0]));
        c.set(Calendar.MONTH, Integer.valueOf(dataTime[1]) - 1);
        c.set(Calendar.YEAR, Integer.valueOf(dataTime[2]));
        String da = "";
        Log.e("DATE_CHECK", c.get(Calendar.DAY_OF_MONTH) + " - " + n.get(Calendar.DAY_OF_MONTH) + "\n" +
                c.get(Calendar.MONTH) + " - " + n.get(Calendar.MONTH) + "\n" + c.get(Calendar.YEAR) + " - " + n.get(Calendar.YEAR));
        if (c.get(Calendar.DAY_OF_MONTH) == n.get(Calendar.DAY_OF_MONTH) && c.get(Calendar.MONTH) == n.get(Calendar.MONTH)
                && c.get(Calendar.YEAR) == n.get(Calendar.YEAR)) {
            da = "Today";
            datetxt.setText(da);
        } else if (c.get(Calendar.DAY_OF_MONTH) == n.get(Calendar.DAY_OF_MONTH) - 1 && c.get(Calendar.MONTH) == n.get(Calendar.MONTH)
                && c.get(Calendar.YEAR) == n.get(Calendar.YEAR)) {
            da = "Yesterday";
            datetxt.setText(da);
        } else {
            dFormat2.format(c.getTime());
            datetxt.setText(chatDate);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    interface CallBack {
        void onImageClick(int pos);
    }
}