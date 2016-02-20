package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmobi.event.dao.EventDAO;
import com.inmobi.event.dto.Event;
import org.bson.types.ObjectId;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import settings.Response;

public class EventController extends Controller{

    public Result create(){
        Response response = new Response();
        try{
            JsonNode node = request().body().asJson();
            EventDAO eventDAO = new EventDAO();
            eventDAO.create(new ObjectMapper().treeToValue(node, Event.class));
            return ok();
        } catch(Exception ex){
            response.setSuccess(false);
            response.setError(ex);
            return badRequest(Json.toJson(response));
        }
    }

    public Result get(String eventId){
        Response response = new Response();
        response.setData(new EventDAO().get(new ObjectId(eventId)));
        response.setSuccess(true);
        return ok(Json.toJson(response));
    }

    public Result list(String latitude, String longitude){
        Response response = new Response();
        response.setData(new EventDAO().list(latitude, longitude));
        return ok(Json.toJson(response));
    }

    public Result join(String eventId, String teamId, String userId){
        Response response = new Response();
        try{
            new EventDAO().join(eventId, teamId, userId);
            response.setSuccess(true);
            return ok(Json.toJson(response));
        }catch (Exception ex){
            response.setSuccess(false);
            response.setError(ex);
            return badRequest(Json.toJson(response));
        }
    }
}
