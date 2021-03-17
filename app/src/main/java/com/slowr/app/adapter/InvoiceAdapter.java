package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.InvoiceItemModel;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {

    //    private List<CityItemModel> categoryList;
    private List<InvoiceItemModel> categoryListFilter;
    Callback callback;
    Context ctx;
    String checkedPos = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_ad_title;
        public TextView txt_invoice_date;
        public TextView txt_invoice_no;
        public LinearLayout layout_root;
        private ImageView img_send_mail;

        public MyViewHolder(View view) {
            super(view);
            txt_ad_title = view.findViewById(R.id.txt_ad_title);
            txt_invoice_date = view.findViewById(R.id.txt_invoice_date);
            txt_invoice_no = view.findViewById(R.id.txt_invoice_no);
            layout_root = view.findViewById(R.id.layout_root);
            img_send_mail = view.findViewById(R.id.img_send_mail);
            img_send_mail.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_send_mail:
                    callback.itemClick(getAdapterPosition());
                    break;

            }

        }
    }

    public InvoiceAdapter(List<InvoiceItemModel> _categoryList, Context ctx) {
//        this.categoryList = _categoryList;
        this.categoryListFilter = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_invoice_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            InvoiceItemModel movie = categoryListFilter.get(position);
            if (movie.getInvoiceTitle() != null)
                holder.txt_ad_title.setText(movie.getInvoiceTitle().trim());

            holder.txt_invoice_date.setText(movie.getInvoiceDate());
            holder.txt_invoice_no.setText(movie.getInvoiceNo());

        } catch (Exception e) {
            e.printStackTrace();
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
        void itemClick(int pos);


    }

    public void clearValues() {
        checkedPos = "";
        notifyDataSetChanged();
    }


}