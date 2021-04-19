
package com.slowr.app.models.FilterResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsAttribute {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ads_id")
    @Expose
    private Integer adsId;
    @SerializedName("attribute_id")
    @Expose
    private Integer attributeId;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("custom_value")
    @Expose
    private Object customValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdsId() {
        return adsId;
    }

    public void setAdsId(Integer adsId) {
        this.adsId = adsId;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Object getCustomValue() {
        return customValue;
    }

    public void setCustomValue(Object customValue) {
        this.customValue = customValue;
    }

}
