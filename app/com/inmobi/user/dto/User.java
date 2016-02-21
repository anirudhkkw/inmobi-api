package com.inmobi.user.dto;

import com.inmobi.event.dto.Location;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

@Entity(value = "user", noClassnameStored = true)
public class User {

    @Id
    private ObjectId _id;
    private String name;
    private String profileURL;
    private boolean isInterestedInFootball;
    private boolean isInterestedInGym;
    private boolean isInterestedInPool;
    private boolean isInterestedInBadminton;
    private boolean isInterestedInSquash;
    private Location location;

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    private List<Gamify> gamify = new ArrayList<>();

    public List<Gamify> getGamify() {
        return gamify;
    }

    public void setGamify(List<Gamify> gamify) {
        this.gamify = gamify;
    }

    public String get_id() {
        return _id.toString();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
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
