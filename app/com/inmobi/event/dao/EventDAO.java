package com.inmobi.event.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inmobi.event.dto.Event;
import com.inmobi.event.dto.EventJson;
import com.inmobi.event.dto.Team;
import com.inmobi.feed.dao.FeedDAO;
import com.inmobi.user.dao.UserDAO;
import com.inmobi.user.dto.Gamify;
import com.inmobi.user.dto.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.dao.BasicDAO;

import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import settings.Global;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class EventDAO extends BasicDAO<Event, Object> {
    private DatastoreImpl datastore = getDs();

    public EventDAO(){
        super(Global.getMorphiaObject().getMongo(), Global.getMorphiaObject().getMorphia(), Global.getMorphiaObject().getDatastore().getDB().getName());
    }

    public boolean create(Event event){
        for(Team t : event.getTeams()){
            t.set_id(new ObjectId());
        }
        datastore.save(event);
        return true;
    }

    public EventJson get(ObjectId eventId){
        EventJson eventJson = new EventJson();
        eventJson.setEvent(getDs().createQuery(Event.class).filter("_id", eventId).get());
        //eventJson.setFeeds(new FeedDAO().listByEvent(eventId));
        return eventJson;
    }

    public List<ObjectId> getUserEvents(ObjectId userId){
        List<ObjectId> eventIds = new ArrayList<>();
        for(Event e : getDs().createQuery(Event.class).filter("teams.userIds", userId).asList()){
            eventIds.add(new ObjectId(e.get_id()));
        }
        return eventIds;
    }

    public List<EventJson> list(String latitude, String longitude){
        List<EventJson> eventJsons = new ArrayList<>();
        WSRequest request = WS.url("http://40.84.159.43:8000/events/v1/getdistance/");
        JsonNode node;
        for(Event e : getDs().createQuery(Event.class).asList()){
            node = new ObjectMapper().createObjectNode();
            EventJson eventJson = get(new ObjectId(e.get_id()));
            eventJson.setFeeds(new FeedDAO().listByEvent(new ObjectId(e.get_id())));
            ((ObjectNode)node).put("sourceLatitude", latitude);
            ((ObjectNode)node).put("sourceLongitude", longitude);
            ((ObjectNode)node).put("destinationLatitude", e.getLocation().getLatitude());
            ((ObjectNode)node).put("destinationLongitude", e.getLocation().getLongitude());
            JsonNode result = request.setBody(node).post(node).get(5000).asJson();
            eventJson.setDistance(result.get("distance").asText());
            eventJson.setTime(result.get("time").asText());
            eventJsons.add(eventJson);

        }

        return eventJsons;
    }

    public boolean join(String eventId, String teamId, String userId){
        Event event = getDs().createQuery(Event.class).filter("_id", new ObjectId(eventId)).get();
        if(event.getTeams().stream().filter(o -> o.get_id().equals(teamId)).findFirst().isPresent()){
            event.getTeams().stream().filter(o -> o.get_id().equals(teamId)).findFirst().get().getUserIds().add(userId);
            int cur = event.getPeople().getCurrent();
            event.getPeople().setCurrent(++cur);
            datastore.save(event);
        }
        User user = new UserDAO().get(new ObjectId(userId));
        if(user.getGamify().stream().filter(o -> o.getTag().equals(event.getTag())).findFirst().isPresent()){
            int points = user.getGamify().stream().filter(o -> o.getTag().equals(event.getTag())).findFirst().get().getPoints() + 10;
            user.getGamify().stream().filter(o -> o.getTag().equals(event.getTag())).findFirst().get().setPoints(points);
        } else {
            Gamify gamify = new Gamify();
            gamify.setTag(event.getTag());
            gamify.setPoints(10);
            user.getGamify().add(gamify);
        }
        new UserDAO().save(user);
        return true;
    }

}
