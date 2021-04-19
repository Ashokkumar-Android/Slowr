
package com.slowr.app.models.FilterResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterAdItem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("category_type")
    @Expose
    private Integer categoryType;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("rental_duration")
    @Expose
    private String rentalDuration;
    @SerializedName("rental_fee")
    @Expose
    private String rentalFee;
    @SerializedName("is_rent_negotiable")
    @Expose
    private Integer isRentNegotiable;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("locality_id")
    @Expose
    private Integer localityId;
    @SerializedName("photos")
    @Expose
    private String photos;

    @SerializedName("is_mobile_visible")
    @Expose
    private Integer isMobileVisible;
    @SerializedName("total_favorite")
    @Expose
    private Integer totalFavorite;
    @SerializedName("total_like")
    @Expose
    private Integer totalLike;
    @SerializedName("custom_cat_value")
    @Expose
    private Object customCatValue;
    @SerializedName("custom_locality")
    @Expose
    private String customLocality;

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("user_viewed_count")
    @Expose
    private Integer userViewedCount;

    @SerializedName("ads_attributes")
    @Expose
    private List<AdsAttribute> adsAttributes = null;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("locality")
    @Expose
    private Locality locality;
    @SerializedName("user")
    User user;
    @SerializedName("service_ad_count")
    private Integer serviceAdCount;

    @SerializedName("favorites")
    private AdFav isFave;

    @SerializedName("ads_likes")
    private AdLike adsLike;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(String rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public String getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(String rentalFee) {
        this.rentalFee = rentalFee;
    }

    public Integer getIsRentNegotiable() {
        return isRentNegotiable;
    }

    public void setIsRentNegotiable(Integer isRentNegotiable) {
        this.isRentNegotiable = isRentNegotiable;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getLocalityId() {
        return localityId;
    }

    public void setLocalityId(Integer localityId) {
        this.localityId = localityId;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }


    public Integer getIsMobileVisible() {
        return isMobileVisible;
    }

    public void setIsMobileVisible(Integer isMobileVisible) {
        this.isMobileVisible = isMobileVisible;
    }

    public Integer getTotalFavorite() {
        return totalFavorite;
    }

    public void setTotalFavorite(Integer totalFavorite) {
        this.totalFavorite = totalFavorite;
    }

    public Integer getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(Integer totalLike) {
        this.totalLike = totalLike;
    }

    public Object getCustomCatValue() {
        return customCatValue;
    }

    public void setCustomCatValue(Object customCatValue) {
        this.customCatValue = customCatValue;
    }

    public String getCustomLocality() {
        return customLocality;
    }

    public void setCustomLocality(String customLocality) {
        this.customLocality = customLocality;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserViewedCount() {
        return userViewedCount;
    }

    public void setUserViewedCount(Integer userViewedCount) {
        this.userViewedCount = userViewedCount;
    }

    public List<AdsAttribute> getAdsAttributes() {
        return adsAttributes;
    }

    public void setAdsAttributes(List<AdsAttribute> adsAttributes) {
        this.adsAttributes = adsAttributes;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public Integer getServiceAdCount() {
        return serviceAdCount;
    }

    public void setServiceAdCount(Integer serviceAdCount) {
        this.serviceAdCount = serviceAdCount;
    }

    public AdFav getIsFave() {
        return isFave;
    }

    public void setIsFave(AdFav isFave) {
        this.isFave = isFave;
    }

    public AdLike getAdsLike() {
        return adsLike;
    }

    public void setAdsLike(AdLike adsLike) {
        this.adsLike = adsLike;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
