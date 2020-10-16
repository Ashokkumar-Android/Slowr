package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.NotificationItemModel;
import com.slowr.app.utils.FormatterUtil;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    //    private List<CityItemModel> categoryList;
    private List<NotificationItemModel> categoryListFilter;
    Callback callback;
    Context ctx;
    String checkedPos = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_notification_title;
        public TextView txt_date;
        public View view_line;
        public LinearLayout layout_root;

        public MyViewHolder(View view) {
            super(view);
            txt_notification_title = view.findViewById(R.id.txt_notification_title);
            txt_date = view.findViewById(R.id.txt_date);
            layout_root = view.findViewById(R.id.layout_root);
            view_line = view.findViewById(R.id.view_line);
            layout_root.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_root:
                    callback.itemClick(getAdapterPosition());
                    break;

            }

        }
    }

    public NotificationAdapter(List<NotificationItemModel> _categoryList, Context ctx) {
//        this.categoryList = _categoryList;
        this.categoryListFilter = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        NotificationItemModel movie = categoryListFilter.get(position);
        holder.txt_notification_title.setText(movie.getNotificationContent().trim());
        CharSequence date = FormatterUtil.getRelativeTimeSpanStringShort(ctx, movie.getNotificationDate().getTime());
        holder.txt_date.setText(date);
        switch (Integer.valueOf(movie.getNotificationColor())) {
            case 1:
                holder.view_line.setBackgroundColor(ctx.getResources().getColor(R.color.bg_green));
                break;
            case 2:
                holder.view_line.setBackgroundColor(ctx.getResources().getColor(R.color.txt_orange));
                break;
            case 3:
                holder.view_line.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
                break;
        }
//        holder.txt_date.setText(movie.getInvoiceDate());
        if (movie.getIsRead().equals("1")) {
            holder.txt_notification_title.setTextColor(ctx.getResources().getColor(R.color.hint_txt_color));
            holder.txt_date.setTextColor(ctx.getResources().getColor(R.color.hint_txt_color));
        }else {
            holder.txt_notification_title.setTextColor(ctx.getResources().getColor(R.color.color_black));
            holder.txt_date.setTextColor(ctx.getResources().getColor(R.color.color_black));
        }

    }

    @Override
    public int getItemCount() {
        return categoryListFilter.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        public void itemClick(int pos);


    }


}