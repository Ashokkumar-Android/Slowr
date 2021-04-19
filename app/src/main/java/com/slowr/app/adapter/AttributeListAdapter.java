package com.slowr.app.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.models.AttributeItemModel;
import com.slowr.app.models.AttributesValueItem;
import com.slowr.app.utils.Function;

import java.util.List;

public class AttributeListAdapter extends RecyclerView.Adapter<AttributeListAdapter.MyViewHolder> {

    private List<AttributeItemModel> categoryList;
    Callback callback;
    Activity ctx;
    boolean isEditable = false;
    boolean isChange = false;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_select_title;
        public AppCompatAutoCompleteTextView txt_select_content;
        public LinearLayout layout_select;
        public FrameLayout layout_drop_down;
        public LinearLayout layout_input;
        public LinearLayout layout_root;
        public EditText edt_attributeValue;
        TextView txt_product_count;
        TextView txt_brand_count;

        public MyViewHolder(View view) {
            super(view);
            txt_select_title = view.findViewById(R.id.txt_select_title);
            layout_select = view.findViewById(R.id.layout_select);
            layout_drop_down = view.findViewById(R.id.layout_drop_down);
            layout_input = view.findViewById(R.id.layout_input);
            txt_select_content = view.findViewById(R.id.txt_select_content);
            edt_attributeValue = view.findViewById(R.id.edt_attributeValue);
            txt_product_count = view.findViewById(R.id.txt_product_count);
            txt_brand_count = view.findViewById(R.id.txt_brand_count);
            layout_root = view.findViewById(R.id.layout_root);
            txt_product_count.setText(ctx.getString(R.string.txt_pro_count, "0"));
            txt_brand_count.setText(ctx.getString(R.string.txt_pro_count, "0"));
            layout_drop_down.setOnClickListener(this);
            layout_root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_drop_down:
//                    callback.itemClick(getAdapterPosition());
                    break;
                case R.id.layout_root:
                    if (!edt_attributeValue.isEnabled() && !layout_drop_down.isEnabled())
                        Function.CustomMessage(ctx, ctx.getString(R.string.you_cannot_edit));
                    break;
            }

        }
    }

    public AttributeListAdapter(List<AttributeItemModel> _categoryList, Activity ctx) {
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
        try {


            final AttributeItemModel movie = categoryList.get(position);
            if (movie.getMandatory().equals("1")) {
                holder.txt_select_title.setText(movie.getName() + " *");
            } else {
                holder.txt_select_title.setText(movie.getName().trim());
            }

            if (movie.getInputValue() != null && movie.getInputValue().equals("")) {
//                holder.txt_select_content.setHint("Type" + " " + movie.getName());
                if (movie.getAttributeValues() != null && movie.getAttributeValues().size() != 0) {
                    holder.txt_select_content.setText("");
                } else {
                    holder.edt_attributeValue.setText("");
                }
            } else {
//                holder.txt_select_content.setHint("Type" + " " + movie.getName());
                isChange = true;
                if (movie.getAttributeValues() != null && movie.getAttributeValues().size() != 0) {
                    holder.txt_select_content.setText(movie.getInputValue().trim());
                    holder.txt_brand_count.setText(ctx.getString(R.string.txt_pro_count, String.valueOf(movie.getInputValue().trim().length())));
                } else {
                    holder.edt_attributeValue.setText(movie.getInputValue().trim());
                    holder.txt_product_count.setText(ctx.getString(R.string.txt_pro_count, String.valueOf(movie.getInputValue().trim().length())));
                }
                isChange = false;
            }


            if (movie.getAttributeValues() != null && movie.getAttributeValues().size() != 0) {
                holder.txt_select_content.setHint("Type" + " " + movie.getName());
                holder.layout_select.setVisibility(View.VISIBLE);
                holder.layout_input.setVisibility(View.GONE);
                AttributesAutoCompleteAdapter attributesAutoCompleteAdapter = new AttributesAutoCompleteAdapter(ctx, R.layout.layout_sub_category_item, movie.getAttributeValues());
                //Getting the instance of AutoCompleteTextView

                holder.txt_select_content.setThreshold(1);//will start working from first character
                holder.txt_select_content.setAdapter(attributesAutoCompleteAdapter);
                attributesAutoCompleteAdapter.setCallback(new AttributesAutoCompleteAdapter.Callback() {
                    @Override
                    public void itemClick(AttributesValueItem model) {
                        holder.txt_select_content.setText(model.getValue().trim());
                        holder.txt_select_content.dismissDropDown();
                        holder.txt_select_content.setSelection(holder.txt_select_content.getText().toString().length());
                        movie.setInputId(model.getId());
                    }
                });

            } else {
                holder.edt_attributeValue.setHint("Type" + " " + movie.getName());
                holder.layout_select.setVisibility(View.GONE);
                holder.layout_input.setVisibility(View.VISIBLE);
            }
            if (isEditable) {
                holder.edt_attributeValue.setEnabled(false);
                holder.layout_drop_down.setEnabled(false);
                holder.edt_attributeValue.setTextColor(ctx.getResources().getColor(R.color.hint_txt_color));
                holder.txt_select_title.setTextColor(ctx.getResources().getColor(R.color.hint_txt_color));
                holder.txt_select_content.setTextColor(ctx.getResources().getColor(R.color.hint_txt_color));
                holder.txt_select_content.setFocusableInTouchMode(false);
            } else {
                holder.edt_attributeValue.setEnabled(true);
                holder.layout_drop_down.setEnabled(true);
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
                    int desValue = holder.edt_attributeValue.getText().toString().length();
                    movie.setInputValue(val);
                    movie.setTextLength(String.valueOf(desValue));
                    if (movie.getIsTitle().equals("1")) {
                        callback.attributeEnterValue(val, position);
                    }
                    holder.txt_product_count.setText(ctx.getString(R.string.txt_pro_count, String.valueOf(desValue)));
                    if (desValue == 50 && !isChange) {
                        Function.CustomMessage(ctx, ctx.getString(R.string.txt_limit_reached));
                    }

                }
            });

            holder.txt_select_content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String val = holder.txt_select_content.getText().toString().trim();
                    int desValue = holder.txt_select_content.getText().toString().length();
                    movie.setInputValue(val);
                    movie.setTextLength(String.valueOf(desValue));
                    if (movie.getIsTitle().equals("1")) {
                        callback.attributeEnterValue(val, position);
                    }
                    holder.txt_brand_count.setText(ctx.getString(R.string.txt_pro_count, String.valueOf(desValue)));
                    if (desValue == 50 && !isChange) {
                        Function.CustomMessage(ctx, ctx.getString(R.string.txt_limit_reached));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void SetEditable(boolean _isEditable) {
        this.isEditable = _isEditable;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void itemClick(int pos);

        void attributeEnterValue(String val, int pos);
    }
}