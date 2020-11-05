package com.slowr.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.CityChipModel;
import com.slowr.chips.Chip;
import com.slowr.chips.ChipDataSource;


class CityChipAdapter
        extends RecyclerView.Adapter<CityChipAdapter.Holder>
        implements ChipDataSource.ChangeObserver {
    private final OnContactClickListener listener;
    private ChipDataSource chipDataSource;


    CityChipAdapter(OnContactClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return chipDataSource == null ? 0 : chipDataSource.getFilteredChips().size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_sub_category_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Chip chip = chipDataSource.getFilteredChips().get(position);
        holder.txt_category_name.setText(chip.getCityName());
        holder.txt_price.setText(chip.getCityPrice());

    }

    @Override
    public void onChipDataSourceChanged() {
        notifyDataSetChanged();
    }

    void setChipDataSource(ChipDataSource chipDataSource) {
        this.chipDataSource = chipDataSource;
        notifyDataSetChanged();
    }

    interface OnContactClickListener {
        void onContactClicked(CityChipModel chip);
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_category_name;
        public TextView txt_price;

        Holder(View v) {
            super(v);
            txt_category_name = v.findViewById(R.id.txt_category_name);
            txt_price = v.findViewById(R.id.txt_price);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Chip clickedChip = chipDataSource.getFilteredChip(getAdapterPosition());

        }
    }
}