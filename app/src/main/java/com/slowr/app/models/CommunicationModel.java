package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class CommunicationModel {
    @SerializedName("header_msg")
    String headerMessage;

    @SerializedName("comments")
    String comments;

    @SerializedName("footer_msg")
    String footerMessage;

    public String getHeaderMessage() {
        return headerMessage;
    }

    public void setHeaderMessage(String headerMessage) {
        this.headerMessage = headerMessage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFooterMessage() {
        return footerMessage;
    }

    public void setFooterMessage(String footerMessage) {
        this.footerMessage = footerMessage;
    }
}
