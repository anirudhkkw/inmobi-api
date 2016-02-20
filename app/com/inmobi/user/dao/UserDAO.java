package com.inmobi.user.dao;

import com.inmobi.event.dao.EventDAO;
import com.inmobi.feed.dao.FeedDAO;
import com.inmobi.feed.dto.Feed;
import com.inmobi.user.dto.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.dao.BasicDAO;
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

    public User get(ObjectId id){
        return getDs().find(User.class).field("_id").equal(id).get();
    }

    public List<User> list(){
        return getDs().createQuery(User.class).asList();
    }

    public boolean createUser(User user){
        datastore.save(user);
        return true;
    }

    public boolean updateUser(User user, ObjectId userId) throws Exception{
        User dbUser = getDs().createQuery(User.class).filter("_id", userId).filter("name", user.getName()).get();
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

    public List<Feed> getFeeds(ObjectId userId){
        List<ObjectId> eventIds = new EventDAO().getUserEvents(userId);
        List<Feed> feeds = new ArrayList<>();
        for(ObjectId objectId : eventIds){
            feeds.addAll(new FeedDAO().listByEvent(objectId));
        }
        feeds.addAll(new FeedDAO().list(userId));
        return feeds;
    }

}
