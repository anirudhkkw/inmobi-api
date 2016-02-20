package com.inmobi.user.dao;

import com.inmobi.user.dto.Feed;
import com.inmobi.user.dto.User;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.dao.BasicDAO;
import play.Logger;
import play.libs.Json;
import settings.Global;

import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BasicDAO<User, Object> {

    private DatastoreImpl datastore = getDs();

    public UserDAO(){
        super(Global.getMorphiaObject().getMongo(), Global.getMorphiaObject().getMorphia(), Global.getMorphiaObject().getDatastore().getDB().getName());
    }

    public User get(String name){
        return getDs().find(User.class).field("name").equal(name).get();
    }

    public List<User> list(){
        return getDs().createQuery(User.class).asList();
    }

    public boolean updateStatus(Feed feed, int userId){
        User user = getDs().createQuery(User.class).filter("id", userId).get();
        if(user == null)
            return false;
        user.getFeeds().add(feed);
        datastore.save(user);
        return true;
    }

    public boolean createUser(User user){
        datastore.save(user);
        return true;
    }

    public boolean updateUser(User user) throws Exception{
        User dbUser = getDs().createQuery(User.class).filter("id", user.getId()).filter("name", user.getName()).get();
        if(dbUser == null){
            throw new Exception("User dont exists bruuuvvvv");
        }
        dbUser.setIsInterestedInBadminton(user.isInterestedInBadminton());
        dbUser.setIsInterestedInFootball(user.isInterestedInFootball());
        dbUser.setIsInterestedInGym(user.isInterestedInGym());
        dbUser.setIsInterestedInPool(user.isInterestedInPool());
        dbUser.setIsInterestedInSquash(user.isInterestedInSquash());
        datastore.save(dbUser);
        return true;
    }
}
