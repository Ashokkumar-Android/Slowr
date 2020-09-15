package com.slowr.app.api;


import com.slowr.app.models.AdModel;
import com.slowr.app.models.AreaModel;
import com.slowr.app.models.AttributeModel;
import com.slowr.app.models.CategoryModel;
import com.slowr.app.models.ChildCategoryModel;
import com.slowr.app.models.CityModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.EditAdModel;
import com.slowr.app.models.GSTModel;
import com.slowr.app.models.HomeDetailsModel;
import com.slowr.app.models.HomeFilterAdModel;
import com.slowr.app.models.InvoiceModel;
import com.slowr.app.models.LoginResponse;
import com.slowr.app.models.NotificationModel;
import com.slowr.app.models.PopularAdModel;
import com.slowr.app.models.ProfileModel;
import com.slowr.app.models.PromotePriceModel;
import com.slowr.app.models.ProsperIdModel;
import com.slowr.app.models.ReportResponsModel;
import com.slowr.app.models.ReportTypeModel;
import com.slowr.app.models.SearchSuggistonModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {


    @POST("register/check-mobile-mail")
    Call<DefaultResponse> emailPhoneValidate(@Body Object params);

    @POST("register/checkmail")
    Call<DefaultResponse> emailPhoneRegistration(@Body Object params);

    @POST("register/otp-send")
    Call<DefaultResponse> sendOTP(@Body Object params);

    @POST("register/resend")
    Call<DefaultResponse> reSendOTP(@Body Object params);

    @POST("register/verify")
    Call<DefaultResponse> verifyOTP(@Body Object params);

    @POST("register/verify")
    Call<LoginResponse> verifyOTPLogin(@Body Object params);

    @POST("register/verify")
    Call<DefaultResponse> verifyOTPForgotPassword(@Body Object params);

    @POST("register")
    Call<LoginResponse> register(@Body Object params);

    @POST("login")
    Call<LoginResponse> toLogin(@Body Object params);

    @POST("send/otp")
    Call<DefaultResponse> forgotPasswordSendMail(@Body Object params);

    @POST("send/verify/otp")
    Call<DefaultResponse> forgotPasswordVerifyOTP(@Body Object params);

    @POST("send/password")
    Call<DefaultResponse> updatePassword(@Body Object params);

    @POST("verifyUserIdDoc")
    Call<ResponseBody> verifyAadhaar(@Body Object params);

    @POST("customattribtes")
    Call<AttributeModel> getAttributes(@Body Object params, @Header("Authorization") String contentRange);

    @GET("postad")
    Call<CategoryModel> getCategory();

    @GET("postad/{category_id}/{subCategory_id}")
    Call<ChildCategoryModel> getSubCategory(@Path(value = "category_id", encoded = true) String categoryId, @Path(value = "subCategory_id", encoded = true) String subCategoryId);


    @GET("city")
    Call<CityModel> getCity();

    @GET("report-types")
    Call<ReportTypeModel> getReportType();

    @GET("city/{city_id}")
    Call<AreaModel> getArea(@Path(value = "city_id", encoded = true) String cityId);


    @POST("post/store")
    Call<DefaultResponse> savePost(@Body Object params, @Header("Authorization") String contentRange);

    @POST("report-us")
    Call<ReportResponsModel> saveReport(@Body Object params);

    @GET("ads/listing")
    Call<AdModel> getPost(@Header("Authorization") String contentRange);

    @GET("postdelete/{categoryId}/{id}")
    Call<DefaultResponse> deletePost(@Path(value = "categoryId", encoded = true) String catId, @Path(value = "id", encoded = true) String adId, @Header("Authorization") String token);

    @GET("requirement/delete/{categoryId}/{id}")
    Call<DefaultResponse> deleteRequirementPost(@Path(value = "categoryId", encoded = true) String catId, @Path(value = "id", encoded = true) String adId, @Header("Authorization") String token);

    @GET("edit/{categoryId}/{id}")
    Call<EditAdModel> getAdDetails(@Path(value = "categoryId", encoded = true) String catId, @Path(value = "id", encoded = true) String adId, @Header("Authorization") String token);

    @POST("post/update")
    Call<DefaultResponse> updatePost(@Body Object params, @Header("Authorization") String contentRange);

    @POST("requirement/update")
    Call<DefaultResponse> updateRequirementPost(@Body Object params, @Header("Authorization") String contentRange);

    @POST("ads-listing")
    Call<HomeFilterAdModel> getHomeAds(@Body Object params, @Header("Authorization") String contentRange);

    @POST("home")
    Call<HomeDetailsModel> getHomeDetails(@Body Object params);

    @GET("adsview/{categoryId}/{id}")
    Call<EditAdModel> getHomeAdDetails(@Path(value = "categoryId", encoded = true) String catId, @Path(value = "id", encoded = true) String adId, @Header("Authorization") String contentRange);


    @POST("like")
    Call<DefaultResponse> addLike(@Body Object params, @Header("Authorization") String contentRange);

    @POST("favorite")
    Call<DefaultResponse> addFavorite(@Body Object params, @Header("Authorization") String contentRange);

    @GET("like/delete/{categoryId}/{adsId}")
    Call<DefaultResponse> deleteLike(@Path(value = "categoryId", encoded = true) String catId, @Path(value = "adsId", encoded = true) String adId, @Header("Authorization") String token);

    @GET("favorite/delete/{categoryId}/{adsId}")
    Call<DefaultResponse> deleteFavorite(@Path(value = "categoryId", encoded = true) String catId, @Path(value = "adsId", encoded = true) String adId, @Header("Authorization") String token);

    @POST("user/change-password")
    Call<DefaultResponse> changePassword(@Body Object params, @Header("Authorization") String contentRange);

    @GET("user-details")
    Call<ProfileModel> getProfileDetails(@Header("Authorization") String contentRange);

    @GET("logout")
    Call<DefaultResponse> callLogout(@Header("Authorization") String contentRange);

    @POST("user/profile")
    Call<DefaultResponse> uploadProfileImage(@Body Object params, @Header("Authorization") String contentRange);

    @POST("user/details")
    Call<DefaultResponse> uploadProfileDetails(@Body Object params, @Header("Authorization") String contentRange);

    @GET("my-favorites")
    Call<PopularAdModel> getFavorites(@Header("Authorization") String contentRange);

    @GET("promotion/fee-details")
    Call<PromotePriceModel> getPromotionPrice(@Header("Authorization") String contentRange);

    @POST("search-suggestions")
    Call<SearchSuggistonModel> getSearchSuggestion(@Body Object params);

    @POST("requirement/store")
    Call<DefaultResponse> saveRequirementPost(@Body Object params, @Header("Authorization") String contentRange);

    @POST("promotion")
    Call<DefaultResponse> savePromotion(@Body Object params, @Header("Authorization") String contentRange);

    @POST("ads-status")
    Call<DefaultResponse> changeAdStatus(@Body Object params, @Header("Authorization") String contentRange);

    @POST("prosperids-list")
    Call<ProsperIdModel> searchProsperId(@Body Object params, @Header("Authorization") String contentRange);

    @POST("verification")
    Call<DefaultResponse> GSTVerificationSave(@Body Object params, @Header("Authorization") String contentRange);

    @GET("invoice/listing")
    Call<InvoiceModel> getInvoice(@Header("Authorization") String contentRange);

    @GET("searchgstin")
    Call<GSTModel> verifyGST(@Query("gstin") String gstNo, @Header("Authorization") String authorization, @Header("Content-Type") String contentRange, @Header("client_id") String clientId);

    @GET("recent-search")
    Call<SearchSuggistonModel> getRecentSearch(@Header("Authorization") String contentRange);

    @GET("notifications")
    Call<NotificationModel> getNotificationList(@Header("Authorization") String contentRange);

    @GET("email/verification-otp")
    Call<DefaultResponse> getEmailVerificationOTP(@Header("Authorization") String contentRange);

    @POST("email/verification")
    Call<DefaultResponse> OTPVerificationEmail(@Body Object params, @Header("Authorization") String contentRange);

    @POST("generate-invoice")
    Call<DefaultResponse> sendInvoice(@Body Object params, @Header("Authorization") String contentRange);
}