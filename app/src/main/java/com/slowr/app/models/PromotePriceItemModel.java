package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class PromotePriceItemModel {
    @SerializedName("top_of_page")
    private String topOfPagePrice;

    @SerializedName("premium_page")
    private String premiumPagePrice;

    @SerializedName("top_of_page_content")
    private String topOfPageContent;

    @SerializedName("premium_page_content")
    private String premiumPageContent;

    public String getTopOfPagePrice() {
        return topOfPagePrice;
    }

    public void setTopOfPagePrice(String topOfPagePrice) {
        this.topOfPagePrice = topOfPagePrice;
    }

    public String getPremiumPagePrice() {
        return premiumPagePrice;
    }

    public void setPremiumPagePrice(String premiumPagePrice) {
        this.premiumPagePrice = premiumPagePrice;
    }

    public String getTopOfPageContent() {
        return topOfPageContent;
    }

    public void setTopOfPageContent(String topOfPageContent) {
        this.topOfPageContent = topOfPageContent;
    }

    public String getPremiumPageContent() {
        return premiumPageContent;
    }

    public void setPremiumPageContent(String premiumPageContent) {
        this.premiumPageContent = premiumPageContent;
    }
}
