package com.slowr.app.utils;

import android.Manifest;

/**
 * Created by Admin on 18-12-2017.
 */

public class Constant {

    public static final String AppName = "Slowr";


    public static final String tag_string_req = "string_req";
    public static final int PERMISSION_REQUEST_CODE = 1111;
    public static final int SERVER_ERROR = 500;
    public static final int Unauthorized_Error = 401;
    /// Local Database.




    public static final String msg_Somethingwentwrong = "Your internet is not stable, Check your connection";
    public static final String Msg_Something = "Something went wrong please try again!";
    public static final String Content_Type = "application/json";
    public static final String Authorization = "Bearer a387714f5c3a0a9185ac50830a0a0185dc783637";
    public static final String ClientId = "uaBInTAtsKXNexyIyj";
    public static String ParentId = "";




    public static final String LoginFlag = "LoginFlag";
    public static final String LoginType = "LoginType";
    public static final String UserId = "UserId";
    public static final String UserToken = "UserToken";
    public static final String UserName = "UserName";
    public static final String UserEmail = "UserEmail";
    public static final String UserPhone = "UserPhone";
    public static final String UserProfile = "UserProfile";
    public static final String ProsperId = "ProsperId";
    public static final String CityId = "CityId";
    public static final String CityName = "CityName";
    public static final String ImagePath = "ImagePath";
    public static final String UserVerified = "0";
    public static final String IsFirst = "IsFirst";




    public static final String[] Permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA};

    public static final String[] Permissions2 = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CALL_PHONE};

    public static final String[] LocationPermissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};


}
