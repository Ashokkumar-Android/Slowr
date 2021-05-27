package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class UserDetailsModel {
    @SerializedName("id")
    private String userId;

    @SerializedName("prosper_id")
    private String prosperId;

    @SerializedName("photo")
    private String userPhoto;

    @SerializedName("phone")
    private String userPhone;

    @SerializedName("name")
    private String userName;

    @SerializedName("mobile")
    private String userMobile;

    @SerializedName("email")
    private String userEmail;

    @SerializedName("is_mobile_verified")
    String isMobileVerified;

    @SerializedName("is_email_verified")
    String isEmailVerified;

    @SerializedName("is_verified")
    String isProfileVerified;

    @SerializedName("ads_count")
    String adCount;

    @SerializedName("state_id")
    String stateId;

    @SerializedName("city_id")
    String cityId;

    @SerializedName("state")
    String stateName;

    @SerializedName("city")
    String cityName;

    @SerializedName("prosper_page_view_count")
    String prosperPageCount;

    @SerializedName("gender")
    String userGender;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProsperId() {
        return prosperId;
    }

    public void setProsperId(String prosperId) {
        this.prosperId = prosperId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getIsMobileVerified() {
        return isMobileVerified;
    }

    public void setIsMobileVerified(String isMobileVerified) {
        this.isMobileVerified = isMobileVerified;
    }

    public String getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(String isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public String getIsProfileVerified() {
        return isProfileVerified;
    }

    public void setIsProfileVerified(String isProfileVerified) {
        this.isProfileVerified = isProfileVerified;
    }

    public String getAdCount() {
        return adCount;
    }

    public void setAdCount(String adCount) {
        this.adCount = adCount;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProsperPageCount() {
        return prosperPageCount;
    }

    public void setProsperPageCount(String prosperPageCount) {
        this.prosperPageCount = prosperPageCount;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}
