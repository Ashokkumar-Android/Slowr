package com.slowr.app.chat;

import android.content.Context;
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

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatItemModel> chatList;
    Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_right_message;
        TextView txt_right_time;
        TextView txt_left_message;
        TextView txt_left_time;
        LinearLayout layout_txt_right;
        LinearLayout layout_txt_left;
        ImageView img_right_chat;
        ImageView img_left_chat;

        public MyViewHolder(View view) {
            super(view);
            txt_right_message = view.findViewById(R.id.txt_right_message);
            txt_right_time = view.findViewById(R.id.txt_right_time);
            layout_txt_right = view.findViewById(R.id.layout_txt_right);
            layout_txt_left = view.findViewById(R.id.layout_txt_left);
            txt_left_message = view.findViewById(R.id.txt_left_message);
            txt_left_time = view.findViewById(R.id.txt_left_time);
            img_right_chat = view.findViewById(R.id.img_right_chat);
            img_left_chat = view.findViewById(R.id.img_left_chat);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

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


//        Glide.with(ctx)
//                .load(movie.getPhotoType())
//                .error(R.drawable.ic_default_horizontal)
//                .placeholder(R.drawable.ic_default_horizontal)
//                .into(holder.img_ad);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}