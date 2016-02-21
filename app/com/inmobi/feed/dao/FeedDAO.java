package com.inmobi.feed.dao;

import com.inmobi.feed.dto.Feed;
import org.bson.types.ObjectId;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.dao.BasicDAO;
import settings.Global;

import java.util.List;

public class FeedDAO extends BasicDAO<Feed, Object> {
    private DatastoreImpl datastore = getDs();

    public FeedDAO(){
        super(Global.getMorphiaObject().getMongo(), Global.getMorphiaObject().getMorphia(), Global.getMorphiaObject().getDatastore().getDB().getName());
    }

    public boolean createFeed(Feed feed){
        datastore.save(feed);
        return true;
    }

    public List<Feed> list(ObjectId userId){
        return getDs().createQuery(Feed.class).filter("userId", userId).asList();
    }

    public List<Feed> listByEvent(ObjectId eventId, ObjectId userId){
        return getDs().createQuery(Feed.class).filter("eventId", eventId).field("userId").notEqual(userId).asList();
    }

    public List<Feed> listByEvent(ObjectId eventId){
        return getDs().createQuery(Feed.class).filter("eventId", eventId).asList();
    }
}
