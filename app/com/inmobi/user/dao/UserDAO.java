package com.inmobi.user.dao;

import com.inmobi.user.dto.User;
import settings.Global;

public class UserDAO {
    public User get(String name){
        return Global.getMorphiaObject().getDatastore().find(User.class).field("name").equal(name).get();
    }
}
