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
import com.slowr.app.models.ProductChatItemModel;

import java.util.List;

public class ProductChatAdapter extends RecyclerView.Adapter<ProductChatAdapter.MyViewHolder> {

    private List<ProductChatItemModel> chatList;
    Context ctx;
    CallBack callBack;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_ad_title;
        TextView txt_price;
        TextView txt_new_message;
        TextView txt_message_count;
        ImageView img_ad;
        LinearLayout layout_root;

        public MyViewHolder(View view) {
            super(view);
            txt_price = view.findViewById(R.id.txt_price);
            txt_ad_title = view.findViewById(R.id.txt_ad_title);
            txt_new_message = view.findViewById(R.id.txt_new_message);
            txt_message_count = view.findViewById(R.id.txt_message_count);
            img_ad = view.findViewById(R.id.img_ad);
            layout_root = view.findViewById(R.id.layout_root);

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

    public ProductChatAdapter(List<ProductChatItemModel> _categoryList, Context ctx) {
        this.chatList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chat_product_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductChatItemModel chatModel = chatList.get(position);
        holder.txt_ad_title.setText(chatModel.getProductTitle());
        if (chatModel.getRentalFee() != null) {
            holder.txt_price.setVisibility(View.VISIBLE);
            String price = "";
            if (chatModel.getRentalFee().contains(".")) {
                String tempPrice[] = chatModel.getRentalFee().split("\\.");
                price = tempPrice[0];
            } else {
                price = chatModel.getRentalFee();
            }

            if (price.equals("0") || price.equals("") || chatModel.getRentalDuration().equals("Custom")) {
                holder.txt_price.setText(chatModel.getRentalDuration());
            } else {
                holder.txt_price.setText("₹ " + price + " / " + chatModel.getRentalDuration());
            }
        } else {
            holder.txt_price.setVisibility(View.GONE);
        }
        Glide.with(ctx)
                .load(chatModel.getAdImage())
                .error(R.drawable.ic_default_horizontal)
                .placeholder(R.drawable.ic_default_horizontal)
                .into(holder.img_ad);

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