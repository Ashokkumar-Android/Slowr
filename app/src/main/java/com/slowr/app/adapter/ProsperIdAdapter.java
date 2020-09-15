package com.slowr.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.ProsperIdItemModel;

import java.util.List;

public class ProsperIdAdapter extends RecyclerView.Adapter<ProsperIdAdapter.MyViewHolder> {

    //    private List<CityItemModel> categoryList;
    private List<ProsperIdItemModel> categoryListFilter;
    Callback callback;
    Context ctx;
    String checkedPos = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_prosperId_popup;
        public TextView txt_price;
        public LinearLayout layout_root;

        public MyViewHolder(View view) {
            super(view);
            txt_prosperId_popup = view.findViewById(R.id.txt_prosperId_popup);
            txt_price = view.findViewById(R.id.txt_price);
            layout_root = view.findViewById(R.id.layout_root);
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

    public ProsperIdAdapter(List<ProsperIdItemModel> _categoryList, Context ctx) {
//        this.categoryList = _categoryList;
        this.categoryListFilter = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_prosper_id_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ProsperIdItemModel movie = categoryListFilter.get(position);

        holder.txt_prosperId_popup.setText(movie.getProsperId());
        holder.txt_price.setText(ctx.getString(R.string.txt_rupee_simpal)+" "+movie.getProsPrice());


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

    public void clearValues() {
        checkedPos = "";
        notifyDataSetChanged();
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                String charString = charSequence.toString();
//                if (charString.isEmpty()) {
//                    categoryListFilter = categoryList;
//                } else {
//                    List<CityItemModel> filteredList = new ArrayList<>();
//                    for (CityItemModel row : categoryList) {
//
//                        // name match condition. this might differ depending on your requirement
//                        // here we are looking for name or phone number match
//                        if (row.getCityName().toLowerCase().contains(charString.toLowerCase())) {
//                            filteredList.add(row);
//                        }
//                    }
//
//                    categoryListFilter = filteredList;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = categoryListFilter;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                categoryListFilter = (ArrayList<CityItemModel>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }
}