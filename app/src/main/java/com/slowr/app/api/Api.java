package com.slowr.app.api;


import com.slowr.app.models.AdModel;
import com.slowr.app.models.AppVersionModel;
import com.slowr.app.models.AreaModel;
import com.slowr.app.models.AttributeModel;
import com.slowr.app.models.BannerModel;
import com.slowr.app.models.CategoryModel;
import com.slowr.app.models.ChatClearModel;
import com.slowr.app.models.ChatHistoryModel;
import com.slowr.app.models.ChildCategoryModel;
import com.slowr.app.models.CityModel;
import com.slowr.app.models.ColorModel;
import com.slowr.app.models.CountModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.EditAdModel;
import com.slowr.app.models.EditBannerModel;
import com.slowr.app.models.Fillter.FilterModel;
import com.slowr.app.models.FilterResult.FilterResult;
import com.slowr.app.models.GSTLitsModel;
import com.slowr.app.models.HomeBannerModel;
import com.slowr.app.models.HomeDetailsModel;
import com.slowr.app.models.HomeFilterAdModel;
import com.slowr.app.models.HomeFlyersModel;
import com.slowr.app.models.InvoiceModel;
import com.slowr.app.models.LoginResponse;
import com.slowr.app.models.NotificationModel;
import com.slowr.app.models.OtherProfileModel;
import com.slowr.app.models.PopularAdModel;
import com.slowr.app.models.PrivacyModel;
import com.slowr.app.models.ProductChatModel;
import com.slowr.app.models.ProfileModel;
import com.slowr.app.models.PromotePriceModel;
import com.slowr.app.models.ProsperIdModel;
import com.slowr.app.models.RegCityModel;
import com.slowr.app.models.ReportResponsModel;
import com.slowr.app.models.ReportTypeModel;
import com.slowr.app.models.SearchSuggistonModel;
import com.slowr.app.models.StateModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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


    @POST("insight")
    Call<DefaultResponse> storeRequest(@Body Object params, @Header("Authorization") String contentRange);

    @GET("postad")
    Call<CategoryModel> getCategory();

    @GET("postad/{category_id}/{subCategory_id}")
    Call<ChildCategoryModel> getSubCategory(@Path(value = "category_id", encoded = true) String categoryId, @Path(value = "subCategory_id", encoded = true) String subCategoryId);


    @GET("city")
    Call<CityModel> getCity();

    @GET("states")
    Call<StateModel> getState();

    @GET("cities/{state}")
    Call<RegCityModel> getCityState(@Path(value = "state", encoded = true) String cityId);

    @GET("report-types")
    Call<ReportTypeModel> getReportType();

    @GET("city/{city_id}")
    Call<AreaModel> getArea(@Path(value = "city_id", encoded = true) String cityId);


    @POST("post/store")
    Call<DefaultResponse> savePost(@Body Object params, @Header("Authorization") String contentRange);


    @Multipart
    @POST("post/store")
    Call<DefaultResponse> savePostForm(@Part List<MultipartBody.Part> file, @Part("type") RequestBody adType, @Part("category_type") RequestBody catType, @Part("category_id") RequestBody catId, @Part("custom_cat_value") RequestBody catTypeText, @Part("rental_fee") RequestBody rental_fee, @Part("rental_duration") RequestBody rental_duration, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("city_id") RequestBody city_id, @Part("locality_id") RequestBody locality_id, @Part("custom_locality") RequestBody locality_custom, @Part("status") RequestBody status, @Part("is_rent_negotiable") RequestBody is_rent_negotiable, @Part("attributeId[]") List<RequestBody> attributeId, @PartMap Map<String, RequestBody> attributeValue, @Part("mobile") RequestBody mobile, @Part("is_mobile_visible") RequestBody is_mobile_visible, @Part("parent_id") RequestBody parent_id, @Header("Authorization") String contentRange);


    @Multipart
    @POST("post/update")
    Call<DefaultResponse> updatePostForm(@Part List<MultipartBody.Part> file, @Part("type") RequestBody adType, @Part("category_type") RequestBody catType, @Part("category_id") RequestBody catId, @Part("custom_cat_value") RequestBody catTypeText, @Part("rental_fee") RequestBody rental_fee, @Part("rental_duration") RequestBody rental_duration, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("city_id") RequestBody city_id, @Part("locality_id") RequestBody locality_id, @Part("custom_locality") RequestBody locality_custom, @Part("status") RequestBody status, @Part("is_rent_negotiable") RequestBody is_rent_negotiable, @Part("attributeId[]") List<RequestBody> attributeId, @PartMap Map<String, RequestBody> attributeValue, @Part("mobile") RequestBody mobile, @Part("is_mobile_visible") RequestBody is_mobile_visible, @Part("parent_id") RequestBody parent_id, @Part("slug") RequestBody ads_id, @Part("new_photo_keys[]") List<RequestBody> newPhotoKey, @Part("delete_photo_keys[]") List<RequestBody> deletePhotoKey, @Header("Authorization") String contentRange);


    @POST("report-us")
    Call<ReportResponsModel> saveReport(@Body Object params, @Header("Authorization") String contentRange);

    @GET("ads/listing")
    Call<AdModel> getPost(@Header("Authorization") String contentRange);

    @GET("postdelete/{id}")
    Call<DefaultResponse> deletePost(@Path(value = "id", encoded = true) String adId, @Header("Authorization") String token);

    @GET("requirement/delete/{categoryId}/{id}")
    Call<DefaultResponse> deleteRequirementPost(@Path(value = "categoryId", encoded = true) String catId, @Path(value = "id", encoded = true) String adId, @Header("Authorization") String token);

    @GET("edit/{id}")
    Call<EditAdModel> getAdDetails(@Path(value = "id", encoded = true) String adId, @Header("Authorization") String token);

    @POST("post/update")
    Call<DefaultResponse> updatePost(@Body Object params, @Header("Authorization") String contentRange);

    @POST("requirement/update")
    Call<DefaultResponse> updateRequirementPost(@Body Object params, @Header("Authorization") String contentRange);

    @POST("ads-listing")
    Call<HomeFilterAdModel> getHomeAds(@Body Object params, @Header("Authorization") String contentRange);

    @POST("filter-result")
    Call<FilterResult> getHomeAdsNew(@Body Object params, @Header("Authorization") String contentRange);

    @POST("filter-data")
    Call<FilterModel> getFilter(@Body Object params, @Header("Authorization") String contentRange);

    @POST("home")
    Call<HomeDetailsModel> getHomeDetails(@Body Object params, @Header("Authorization") String contentRange);

    @GET("home-categories-cities")
    Call<HomeDetailsModel> getHomeCategory(@Header("Authorization") String contentRange);

    @POST("home-flyers")
    Call<HomeFlyersModel> getHomeFlyers(@Body Object params, @Header("Authorization") String contentRange);

    @POST("home-banners")
    Call<HomeBannerModel> getHomeBanners(@Body Object params, @Header("Authorization") String contentRange);

    @GET("adsview/{id}")
    Call<EditAdModel> getHomeAdDetails(@Path(value = "id", encoded = true) String adId, @Header("Authorization") String contentRange);


    @POST("like")
    Call<DefaultResponse> addLike(@Body Object params, @Header("Authorization") String contentRange);

    @POST("favorite")
    Call<DefaultResponse> addFavorite(@Body Object params, @Header("Authorization") String contentRange);

    @GET("like/delete/{categoryId}/{adsId}")
    Call<DefaultResponse> deleteLike(@Path(value = "categoryId", encoded = true) String catId, @Path(value = "adsId", encoded = true) String adId, @Header("Authorization") String token);

    @GET("favorite/delete/{adsId}")
    Call<DefaultResponse> deleteFavorite(@Path(value = "adsId", encoded = true) String adId, @Header("Authorization") String token);

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

    @POST("promotion-action")
    Call<DefaultResponse> savePromotion(@Body Object params, @Header("Authorization") String contentRange);


//    @POST("promotion")
//    Call<DefaultResponse> savePromotion(@Body Object params, @Header("Authorization") String contentRange);

    @POST("razorpay-order")
    Call<DefaultResponse> getOrderId(@Body Object params, @Header("Authorization") String contentRange);


    @POST("ads-status")
    Call<DefaultResponse> changeAdStatus(@Body Object params, @Header("Authorization") String contentRange);

    @POST("prosperids-list")
    Call<ProsperIdModel> searchProsperId(@Body Object params, @Header("Authorization") String contentRange);

    @POST("verification")
    Call<DefaultResponse> GSTVerificationSave(@Body Object params, @Header("Authorization") String contentRange);

    @GET("testAlgolia/{testId}")
    Call<ProsperIdModel> getProsperId(@Path(value = "testId", encoded = true) String catId);

    @GET("invoice/listing")
    Call<InvoiceModel> getInvoice(@Header("Authorization") String contentRange);

    @GET("searchgstin")
    Call<String> verifyGST(@Query("gstin") String gstNo, @Header("Authorization") String authorization, @Header("Content-Type") String contentRange, @Header("client_id") String clientId);

    @GET("recent-search")
    Call<SearchSuggistonModel> getRecentSearch(@Header("Authorization") String contentRange);

    @GET("notifications")
    Call<NotificationModel> getNotificationList(@Header("Authorization") String contentRange);

    @GET("email/verification-otp")
    Call<DefaultResponse> getEmailVerificationOTP(@Header("Authorization") String contentRange);

    @POST("read-notification")
    Call<DefaultResponse> ReadNotification(@Body Object params, @Header("Authorization") String contentRange);

    @POST("email/verification")
    Call<DefaultResponse> OTPVerificationEmail(@Body Object params, @Header("Authorization") String contentRange);

    @POST("generate-invoice")
    Call<DefaultResponse> sendInvoice(@Body Object params, @Header("Authorization") String contentRange);

    @POST("check-promotion-exists")
    Call<DefaultResponse> checkPromotionValid(@Body Object params, @Header("Authorization") String contentRange);


    @POST("prosper-details")
    Call<OtherProfileModel> getUserAdDetails(@Body Object params, @Header("Authorization") String contentRange);


    @GET("page/terms-conditions")
    Call<PrivacyModel> getTC();

    @GET("page/privacy-policy")
    Call<PrivacyModel> getPrivacy();

    @GET("chat")
    Call<ProductChatModel> getProductChat(@Header("Authorization") String contentRange);

    @GET("chat-user/{categoryId}/{adId}")
    Call<ProductChatModel> getChatList(@Path(value = "categoryId", encoded = true) String catId, @Path(value = "adId", encoded = true) String adId, @Header("Authorization") String contentRange);


    @POST("chat-message")
    Call<ChatHistoryModel> chatSendMessage(@Body Object params, @Header("Authorization") String contentRange);

    @POST("chat-history")
    Call<ChatHistoryModel> chatMessageHistory(@Body Object params, @Header("Authorization") String contentRange);

    @POST("clear-chat")
    Call<ChatClearModel> clearChat(@Body Object params, @Header("Authorization") String contentRange);

    @POST("service-exists")
    Call<DefaultResponse> serviceCheck(@Body Object params, @Header("Authorization") String contentRange);

    @POST("delete-notification")
    Call<DefaultResponse> deleteNotification(@Body Object params, @Header("Authorization") String contentRange);

    @Multipart
    @POST("user-profile")
    Call<DefaultResponse> uploadImage(@Part MultipartBody.Part file, @Header("Authorization") String contentRange);

    @Multipart
    @POST("chat-files")
    Call<ChatHistoryModel> uploadChatImage(@Part MultipartBody.Part file, @Part("ads_id") RequestBody adId, @Part("category_id") RequestBody catId, @Part("render_id") RequestBody renderId, @Part("chat_id") RequestBody chatId, @Header("Authorization") String contentRange);


    @GET("chat-notification-count")
    Call<CountModel> getNotificationUnreadCount(@Header("Authorization") String contentRange);

    @GET("banner/color/code")
    Call<ColorModel> getColorCode(@Header("Authorization") String contentRange);

    @GET("banner/listing")
    Call<BannerModel> getBannerList(@Header("Authorization") String contentRange);

    @GET("banner/edit/{id}")
    Call<EditBannerModel> getBannerDetails(@Path(value = "id", encoded = true) String bannerId, @Header("Authorization") String contentRange);

    @GET("banner/delete/{id}")
    Call<DefaultResponse> getBannerDelete(@Path(value = "id", encoded = true) String bannerId, @Header("Authorization") String contentRange);

    @Multipart
    @POST("razorpay-order")
    Call<DefaultResponse> AddBanner(@Part MultipartBody.Part file, @Part("ads_id") RequestBody bannerId, @Part("title") RequestBody bannerTitle, @Part("start_date") RequestBody fromDate, @Part("end_date") RequestBody toDate, @Part("description") RequestBody bannerDecs, @Part("city_id") RequestBody cityIds, @Part("total_amount") RequestBody totalAmount, @Part("total_days") RequestBody totalDays, @Part("colour_code") RequestBody colorCode, @Part("type") RequestBody promotionType, @Part("name") RequestBody gstName, @Part("gst_no") RequestBody gstNo, @Part("address") RequestBody gstAddress, @Part("gst_id") RequestBody gstId, @Header("Authorization") String contentRange);

//    @Multipart
//    @POST("promotion")
//    Call<DefaultResponse> AddBanner(@Part MultipartBody.Part file, @Part("ads_id") RequestBody bannerId, @Part("title") RequestBody bannerTitle, @Part("from") RequestBody fromDate, @Part("to") RequestBody toDate, @Part("description") RequestBody bannerDecs, @Part("city_id") RequestBody cityIds, @Part("total_amount") RequestBody totalAmount, @Part("total_days") RequestBody totalDays, @Part("colour_code") RequestBody colorCode, @Part("transaction_id") RequestBody paymentId, @Part("type") RequestBody promotionType, @Header("Authorization") String contentRange);

    @Multipart
    @POST("banner/update")
    Call<DefaultResponse> UpdateBanner(@Part MultipartBody.Part file, @Part("title") RequestBody bannerTitle, @Part("banner_id") RequestBody bannerId, @Part("description") RequestBody bannerDecs, @Part("colour_code") RequestBody colorCode, @Header("Authorization") String contentRange);

    @POST("check-app-version")
    Call<AppVersionModel> appVersionCheck(@Body Object params);

    @GET("profile/remove")
    Call<DefaultResponse> removeProfile(@Header("Authorization") String contentRange);

    @POST("geo-location")
    Call<DefaultResponse> deviceDetails(@Body Object params, @Header("Authorization") String contentRange);

    @POST("contact-view")
    Call<DefaultResponse> viewContact(@Body Object params, @Header("Authorization") String contentRange);

    @GET("all-gst")
    Call<GSTLitsModel> gstNoList(@Header("Authorization") String contentRange);

    @POST("access_token")
    Call<ResponseBody> getToken(@Body Object params);
}
