package com.slowr.matisse;

import android.net.Uri;

public class ViewModel {
    Uri ImageUri;

    public int getCropType() {
        return cropType;
    }
    public void setCropType(int cropType) {
        this.cropType = cropType;
    }
    int cropType;//normal-0, vertical -3, square -2 , rectangle -1

    public boolean isCropped() {
        return isCropped;
    }

    public void setCropped(boolean cropped) {
        isCropped = cropped;
    }

    boolean isCropped;
    public ViewModel(Uri imageUri, int width, int height, int ctype,boolean isCropped) {
        ImageUri = imageUri;
        this.width = width;
        this.height = height;
        this.cropType = ctype;
        this.isCropped = isCropped;

    }

    int width;
    int height;

    public Uri getImageUri() {
        return ImageUri;
    }

    public void setImageUri(Uri imageUri) {
        ImageUri = imageUri;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
