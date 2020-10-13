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
import com.slowr.app.models.ProductChatItemModel;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private List<ProductChatItemModel> chatList;
    Context ctx;
    CallBack callBack;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_prosperId;
        TextView txt_message_count;
        TextView txt_new_message;
        LinearLayout layout_root;
        ImageView img_profile;

        public MyViewHolder(View view) {
            super(view);
            txt_prosperId = view.findViewById(R.id.txt_prosperId);
            layout_root = view.findViewById(R.id.layout_root);
            img_profile = view.findViewById(R.id.img_profile);
            txt_message_count = view.findViewById(R.id.txt_message_count);
            txt_new_message = view.findViewById(R.id.txt_new_message);

            layout_root.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    callBack.onItemClick(getAdapterPosition());
                    break;
            }

        }
    }

    public ChatListAdapter(List<ProductChatItemModel> _categoryList, Context ctx) {
        this.chatList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chat_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductChatItemModel chatModel = chatList.get(position);
        holder.txt_prosperId.setText(chatModel.getProsperId());

        if (Integer.valueOf(chatModel.getUnreadCount()) == null || Integer.valueOf(chatModel.getUnreadCount()) == 0) {
            holder.txt_message_count.setText(ctx.getString(R.string.txt_no_new_messages));
            holder.txt_message_count.setTextColor(ctx.getResources().getColor(R.color.hint_txt_color));
        } else {
            holder.txt_message_count.setText(chatModel.getUnreadCount() + " " + ctx.getString(R.string.txt_new_messages));
            holder.txt_message_count.setTextColor(ctx.getResources().getColor(R.color.txt_orange));
        }
        if(chatModel.getIsFile()!=null&&chatModel.getIsFile().equals("photo")){
            holder.txt_new_message.setText(chatModel.getIsFile());
        }else {
            holder.txt_new_message.setText(chatModel.getLastMessage());
        }

        Glide.with(ctx)
                .load(chatModel.getAdImage())
                .circleCrop()
                .error(R.drawable.ic_default_profile)
                .placeholder(R.drawable.ic_default_profile)
                .into(holder.img_profile);

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

    public interface CallBack {
        public void onItemClick(int pos);
    }

}