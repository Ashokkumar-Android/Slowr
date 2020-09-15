package com.slowr.app.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.AttributeItemModel;

import java.util.List;

public class AttributeListAdapter extends RecyclerView.Adapter<AttributeListAdapter.MyViewHolder> {

    private List<AttributeItemModel> categoryList;
    Callback callback;
    Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_select_title;
        public TextView txt_select_content;
        public LinearLayout layout_select;
        public LinearLayout layout_drop_down;
        public LinearLayout layout_input;
        public EditText edt_attributeValue;

        public MyViewHolder(View view) {
            super(view);
            txt_select_title = view.findViewById(R.id.txt_select_title);
            layout_select = view.findViewById(R.id.layout_select);
            layout_drop_down = view.findViewById(R.id.layout_drop_down);
            layout_input = view.findViewById(R.id.layout_input);
            txt_select_content = view.findViewById(R.id.txt_select_content);
            edt_attributeValue = view.findViewById(R.id.edt_attributeValue);
            layout_drop_down.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_drop_down:
                    callback.itemClick(getAdapterPosition());
                    break;
            }

        }
    }

    public AttributeListAdapter(List<AttributeItemModel> _categoryList, Context ctx) {
        this.categoryList = _categoryList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_attribute_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AttributeItemModel movie = categoryList.get(position);
        if (movie.getMandatory().equals("1")) {
            holder.txt_select_title.setText(movie.getName() + " *");
        } else {
            holder.txt_select_title.setText(movie.getName().trim());
        }

        if (movie.getInputValue() != null && movie.getInputValue().equals("")) {
            holder.txt_select_content.setHint("Select" + " " + movie.getName());
            holder.edt_attributeValue.setText("");
            holder.txt_select_content.setText("");
        } else {
            if (movie.getType().equals("select")) {
                holder.txt_select_content.setText(movie.getInputValue().trim());
            } else {
                holder.edt_attributeValue.setText(movie.getInputValue().trim());
            }
        }

        if (movie.getType().equals("select")) {
            holder.layout_select.setVisibility(View.VISIBLE);
            holder.layout_input.setVisibility(View.GONE);
        } else {
            holder.layout_select.setVisibility(View.GONE);
            holder.layout_input.setVisibility(View.VISIBLE);
        }
        holder.edt_attributeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String val = holder.edt_attributeValue.getText().toString().trim();
                movie.setInputValue(val);
                if (movie.getIsTitle().equals("1")) {
                    callback.attributeEnterValue(val, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
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

        void attributeEnterValue(String val, int pos);
    }
}