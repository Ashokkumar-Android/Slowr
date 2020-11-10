package com.slowr.materialchips.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Chip implements ChipInterface {

    private String id;
    private String label;
    private String info;


    public Chip(@NonNull String label, @Nullable String info) {
        this.label = label;
        this.info = info;
    }

    public Chip(@NonNull String id, @NonNull String label, @Nullable String info) {
        this.id = id;
        this.label = label;
        this.info = info;
    }



    @Override
    public String getId() {
        return id;
    }


    @Override
    public String getCityName() {
        return label;
    }

    @Override
    public String getPrice() {
        return info;
    }
}
