package com.inmobi.feed.dto;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "feed", noClassnameStored = true)
public class Feed {
    @Id
    private ObjectId _id;
    private String title;
    private String url;
    private String link;
    private String mimeType;
    private Tags tag;
    private ObjectId userId;
    private ObjectId eventId;

    public Feed(){

    }

    public Feed(String title, String url, String mimeType, Tags tag, ObjectId userId, ObjectId eventId){
        this.title = title;
        this.url = url;
        this.mimeType = mimeType;
        this.tag = tag;
        this.userId = userId;
        this.eventId = eventId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public String getUserId() {
        return userId.toString();
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId != null ? eventId.toString() : null;
    }

    public void setEventId(ObjectId eventId) {
        this.eventId = eventId;
    }
}
