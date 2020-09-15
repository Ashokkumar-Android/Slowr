package com.slowr.app.models;

import androidx.palette.graphics.Palette;

import java.io.Serializable;

public class UploadImageModel implements Serializable {

    String imgURL;
    boolean isChanged;
    String uri;
    public Palette posterPalette;
    String uriType="1";

    public String getUriType() {
        return uriType;
    }

    public void setUriType(String uriType) {
        this.uriType = uriType;
    }

    public UploadImageModel() {

    }
    public UploadImageModel(String imgURL, boolean isChanged, String uri, String UriTyp) {
        this.imgURL = imgURL;
        this.isChanged = isChanged;
        this.uri = uri;
        this.uriType = UriTyp;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Palette getPosterPalette() {
        return posterPalette;
    }

    public void setPosterPalette(Palette posterPalette) {
        this.posterPalette = posterPalette;
    }
}
