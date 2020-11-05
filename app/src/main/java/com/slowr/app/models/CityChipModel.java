package com.slowr.app.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.slowr.chips.Chip;

public class CityChipModel extends Chip {
    String id;
    String cityName;
    String cityPrice;

    @Nullable
    @Override
    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String getCityName() {
        return cityName;
    }

    @Nullable
    @Override
    public String getCityPrice() {
        return cityPrice;
    }
}
