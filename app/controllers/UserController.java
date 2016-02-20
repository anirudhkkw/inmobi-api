package controllers;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inmobi.AWS.GeneratePublicUrlAws;

import com.inmobi.event.dto.Location;
import com.inmobi.feed.dto.Tags;
import com.inmobi.user.dao.UserDAO;
import com.inmobi.feed.dto.Feed;
import com.inmobi.user.dto.User;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import settings.Global;
import settings.Response;

import java.util.ArrayList;
import java.util.List;

public class UserController extends Controller {

    public Result login(String name, String latitude, String longitude){
        Response response = new Response();
        User user = new UserDAO().get(name);
        user.setLocation(new Location(latitude, longitude));
        new UserDAO().save(user);
        response.setData(user);
        response.setSuccess(true);
        return ok(Json.toJson(response));
    }

    public Result list(){
        Response response = new Response();
        response.setData(new UserDAO().list());
        response.setSuccess(true);
        return ok(Json.toJson(response));
    }

    public Result uploadMedia(){
        JsonNode values = request().body().asJson();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("media");
        PutObjectResult putObjectResult = Global.getS3Client().putObject(new PutObjectRequest(Global.getAmazonBucket(), "media" + "/" + body.asFormUrlEncoded().get("userId")[0] + "/" +
                picture.getFilename(), picture.getFile()).withCannedAcl(CannedAccessControlList.PublicRead));
        String publicUrl = GeneratePublicUrlAws.generateUrl(body.asFormUrlEncoded().get("userId")[0], picture.getFilename(), "media");
        return ok(publicUrl);

    }

    public Result createUser(){
        JsonNode node = request().body().asJson();
        UserDAO userDAO = new UserDAO();
        try{
            userDAO.createUser(new ObjectMapper().treeToValue(node, User.class));
            return ok();
        }catch (Exception ex){
            return badRequest();
        }
    }

    public Result updateUser(){
        JsonNode node = request().body().asJson();
        UserDAO userDAO = new UserDAO();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            userDAO.updateUser(objectMapper.treeToValue(node, User.class), new ObjectId(node.get("id").asText()));
            return ok();
        }catch (Exception ex){
            return badRequest(ex.getMessage());
        }
    }

    public Result feed(String userId){
        Response response = new Response();
        List<Feed> finalResult = new ArrayList<>();
        int counter = 0;
        int index = 0;
        int tagIndex = 0;
        List<Feed> userFeeds = new UserDAO().getFeeds(new ObjectId(userId));
        System.out.println(userFeeds);
        JsonNode node = new ObjectMapper().createObjectNode();
        List<String> tags = new UserDAO().getTags(userId);
        ((ObjectNode)node).put("tags", StringUtils.join(tags, ','));
        WSRequest request = WS.url("http://40.84.159.43:8000/events/v1/getoffers/");
        try{
            JsonNode result = request.setBody(node).post(node).get(10000).asJson();
            for (Feed e : userFeeds){
                counter++;
                tagIndex++;
                finalResult.add(e);
                if(counter >= 2){
                    Feed feed = new Feed();
                    feed.setLink(result.get(tags.get(tagIndex)).get(counter).get("url").asText());
                    feed.setTag(Tags.valueOf(tags.get(tagIndex).toUpperCase()));
                    feed.setUrl(result.get(tags.get(tagIndex)).get(counter).get("image").asText());
                    feed.setMimeType("image/jpeg");
                    feed.setTitle(result.get(tags.get(tagIndex)).get(counter).get("title").asText() + " @ " + result.get(tags.get(tagIndex)).get(counter).get("price").asText());
                    feed.setUserId(new ObjectId(userId));
                    finalResult.add(feed);
                    counter = 0;
                    if(tagIndex == tags.size()){
                        tagIndex = 0;
                    }
                }

            }
            response.setData(finalResult);
            response.setSuccess(true);
            return ok(Json.toJson(response));
        } catch (Exception ex){
            response.setData(userFeeds);
            response.setSuccess(true);
            return ok(Json.toJson(response));
        }
    }
}
