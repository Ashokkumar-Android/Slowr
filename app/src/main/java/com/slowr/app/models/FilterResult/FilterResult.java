
package com.slowr.app.models.FilterResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.slowr.app.models.SortByModel;

import java.util.List;

public class FilterResult {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<FilterAdItem> data = null;

    @SerializedName("image_path")
    private String imagePath;

    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("last_page")
    private int lastPage;

    @SerializedName("sort_options")
    @Expose
    private List<SortByModel> value = null;
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FilterAdItem> getData() {
        return data;
    }

    public void setData(List<FilterAdItem> data) {
        this.data = data;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<SortByModel> getValue() {
        return value;
    }

    public void setValue(List<SortByModel> value) {
        this.value = value;
    }
}
