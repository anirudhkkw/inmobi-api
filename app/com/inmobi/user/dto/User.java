package com.inmobi.user.dto;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

@Entity(value = "user", noClassnameStored = true)
public class User {

    @Id
    private ObjectId _id;
    private int id;
    private String name;
    private boolean isInterestedInFootball;
    private boolean isInterestedInGym;
    private boolean isInterestedInPool;
    private boolean isInterestedInBadminton;
    private boolean isInterestedInSquash;
    private List<Feed> feeds = new ArrayList<>();

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInterestedInFootball() {
        return isInterestedInFootball;
    }

    public void setIsInterestedInFootball(boolean isInterestedInFootball) {
        this.isInterestedInFootball = isInterestedInFootball;
    }

    public boolean isInterestedInGym() {
        return isInterestedInGym;
    }

    public void setIsInterestedInGym(boolean isInterestedInGym) {
        this.isInterestedInGym = isInterestedInGym;
    }

    public boolean isInterestedInPool() {
        return isInterestedInPool;
    }

    public void setIsInterestedInPool(boolean isInterestedInPool) {
        this.isInterestedInPool = isInterestedInPool;
    }

    public boolean isInterestedInBadminton() {
        return isInterestedInBadminton;
    }

    public void setIsInterestedInBadminton(boolean isInterestedInBadminton) {
        this.isInterestedInBadminton = isInterestedInBadminton;
    }

    public boolean isInterestedInSquash() {
        return isInterestedInSquash;
    }

    public void setIsInterestedInSquash(boolean isInterestedInSquash) {
        this.isInterestedInSquash = isInterestedInSquash;
    }


}
