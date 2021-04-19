
package com.slowr.app.models.Fillter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("rental_duration")
    @Expose
    private RentalDuration rentalDuration;
    @SerializedName("ad_type")
    @Expose
    private AdType adType;
    @SerializedName("fee")
    @Expose
    private Fee fee;
    @SerializedName("subcategoryFilterOption")
    @Expose
    private SubcategoryFilterOption subcategoryFilterOption;
    @SerializedName("attributeFilterOption")
    @Expose
    private List<AttributeFilterOption> attributeFilterOption = null;
    @SerializedName("localityList")
    @Expose
    private LocalityList localityList;

    public RentalDuration getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(RentalDuration rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public AdType getAdType() {
        return adType;
    }

    public void setAdType(AdType adType) {
        this.adType = adType;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public SubcategoryFilterOption getSubcategoryFilterOption() {
        return subcategoryFilterOption;
    }

    public void setSubcategoryFilterOption(SubcategoryFilterOption subcategoryFilterOption) {
        this.subcategoryFilterOption = subcategoryFilterOption;
    }

    public List<AttributeFilterOption> getAttributeFilterOption() {
        return attributeFilterOption;
    }

    public void setAttributeFilterOption(List<AttributeFilterOption> attributeFilterOption) {
        this.attributeFilterOption = attributeFilterOption;
    }

    public LocalityList getLocalityList() {
        return localityList;
    }

    public void setLocalityList(LocalityList localityList) {
        this.localityList = localityList;
    }

}
