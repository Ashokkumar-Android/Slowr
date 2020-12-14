package com.slowr.app.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.models.ProductChatItemModel;

import java.util.ArrayList;
import java.util.List;

public class ProductChatAdapter extends RecyclerView.Adapter<ProductChatAdapter.MyViewHolder> implements Filterable {

    private List<ProductChatItemModel> chatList;
    private List<ProductChatItemModel> chatListFilter;
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
                    callBack.onItemClick(chatListFilter.get(getAdapterPosition()));
                    break;
            }

        }
    }

    public ProductChatAdapter(List<ProductChatItemModel> _categoryList, Context ctx) {
        this.chatList = _categoryList;
        this.chatListFilter = _categoryList;
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
        ProductChatItemModel chatModel = chatListFilter.get(position);
        holder.txt_ad_title.setText(chatModel.getProductTitle());
        if (chatModel.getRentalFee() != null) {
            holder.txt_price.setVisibility(View.VISIBLE);
            String price = "";
            if (chatModel.getRentalFee().contains(".")) {
                String[] tempPrice = chatModel.getRentalFee().split("\\.");
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
                .error(R.drawable.ic_no_image)
                .placeholder(R.drawable.ic_no_image)
                .into(holder.img_ad);

        if (chatModel.getUnreadCount().equals("0")) {
            holder.txt_new_message.setText(ctx.getString(R.string.txt_no_new_messages));
            holder.txt_new_message.setTextColor(ctx.getResources().getColor(R.color.hint_txt_color));

            holder.txt_message_count.setText(chatModel.getUnreadCount());
            holder.txt_message_count.setTextColor(ctx.getResources().getColor(R.color.hint_txt_color));
        } else {
            holder.txt_new_message.setText(ctx.getString(R.string.txt_new_messages));
            holder.txt_new_message.setTextColor(ctx.getResources().getColor(R.color.txt_orange));

            holder.txt_message_count.setText(chatModel.getUnreadCount());
            holder.txt_message_count.setTextColor(ctx.getResources().getColor(R.color.txt_orange));
        }

    }

    @Override
    public int getItemCount() {
        return chatListFilter.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void onItemClick(ProductChatItemModel model);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    chatListFilter = chatList;
                } else {
                    List<ProductChatItemModel> filteredList = new ArrayList<>();
                    for (ProductChatItemModel row : chatList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProductTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    chatListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = chatListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                chatListFilter = (ArrayList<ProductChatItemModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}