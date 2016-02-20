package controllers;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmobi.AWS.GeneratePublicUrlAws;

import com.inmobi.event.dto.Location;
import com.inmobi.user.dao.UserDAO;
import com.inmobi.feed.dto.Feed;
import com.inmobi.user.dto.User;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import settings.Global;
import settings.Response;

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
        response.setData(new UserDAO().getFeeds(new ObjectId(userId)));
        response.setSuccess(true);
        return ok(Json.toJson(response));
    }
}
