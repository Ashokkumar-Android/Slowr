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
}
