package com.inmobi.user.dto;

public class Feed {
    private String title;
    private String url;
    private String mimeType;

    public Feed(){

    }

    public Feed(String title, String url, String mimeType){
        this.title = title;
        this.url = url;
        this.mimeType = mimeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
