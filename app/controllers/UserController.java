package controllers;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmobi.AWS.GeneratePublicUrlAws;

import com.inmobi.user.dao.UserDAO;
import com.inmobi.user.dto.Feed;
import com.inmobi.user.dto.User;
import org.apache.commons.io.FilenameUtils;
import play.data.Form;
import play.db.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import settings.Global;
import settings.Response;

public class UserController extends Controller {

    public Result login(String name){
        Response response = new Response();
        response.setData(new UserDAO().get(name));
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

    public Result updateStatus(){
        Response response = new Response();
        try{

            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart media = body.getFile("media");
            String title = body.asFormUrlEncoded().get("title")[0];
            int userId = Integer.parseInt(body.asFormUrlEncoded().get("userId")[0]);
            PutObjectResult putObjectResult = Global.getS3Client().putObject(new PutObjectRequest(Global.getAmazonBucket(), "media" + "/" + body.asFormUrlEncoded().get("userId")[0] + "/" +
                    media.getFilename(), media.getFile()).withCannedAcl(CannedAccessControlList.PublicRead));
            String publicUrl = GeneratePublicUrlAws.generateUrl(body.asFormUrlEncoded().get("userId")[0], media.getFilename(), "media");
            String mimeType;
            if(FilenameUtils.getExtension(media.getFilename()).equals("mp4")){
                mimeType = "video/mp4";
            } else {
                mimeType = "image/jpeg";
            }
            UserDAO userDAO = new UserDAO();
            if(!userDAO.updateStatus(new Feed(title,publicUrl, mimeType), userId)){
                response.setError("User not found");
                response.setSuccess(false);
                return badRequest(Json.toJson(response));
            }
            response.setSuccess(true);
            return ok(Json.toJson(response));
        }catch (Exception ex){
            response.setError(ex.getMessage());
            return badRequest(Json.toJson(response));
        }
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
            userDAO.updateUser(new ObjectMapper().treeToValue(node, User.class));
            return ok();
        }catch (Exception ex){
            return badRequest(ex.getMessage());
        }
    }
}
