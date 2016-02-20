package com.inmobi.event.dto;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id
    private ObjectId teamId;
    private String name;
    private String owner;
    private List<ObjectId> userIds = new ArrayList<>();

    public String get_id() {
        return teamId.toString();
    }

    public void set_id(ObjectId _id) {
        this.teamId = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getUserIds() {
        List<String> userIds = new ArrayList<>();
        for (ObjectId o : this.userIds){
            userIds.add(o.toString());
        }
        return userIds;
    }

    public void setUserIds(List<ObjectId> userIds) {
        this.userIds = userIds;
    }
}
