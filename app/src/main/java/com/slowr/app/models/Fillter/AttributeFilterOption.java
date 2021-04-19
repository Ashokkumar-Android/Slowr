
package com.slowr.app.models.Fillter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttributeFilterOption {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("attribute_value")
    @Expose
    private List<AttributeValue> attributeValue = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributeValue> getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(List<AttributeValue> attributeValue) {
        this.attributeValue = attributeValue;
    }

}
