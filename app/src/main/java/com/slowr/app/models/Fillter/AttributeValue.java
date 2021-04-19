
package com.slowr.app.models.Fillter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributeValue {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("attributeId")
    @Expose
    private Integer attributeId;
    @SerializedName("value")
    @Expose
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
