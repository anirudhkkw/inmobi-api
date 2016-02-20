package controllers;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.inmobi.AWS.GeneratePublicUrlAws;
import com.inmobi.feed.dao.FeedDAO;
import com.inmobi.feed.dto.Feed;
import com.inmobi.feed.dto.Tags;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import settings.Global;
import settings.Response;

public class FeedController extends Controller{
    public Result create(){
        Response response = new Response();
        try{

            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart media = body.getFile("media");
            String title = body.asFormUrlEncoded().get("title")[0];
            PutObjectResult putObjectResult = Global.getS3Client().putObject(new PutObjectRequest(Global.getAmazonBucket(), "media" + "/" + body.asFormUrlEncoded().get("userId")[0] + "/" +
                    media.getFilename(), media.getFile()).withCannedAcl(CannedAccessControlList.PublicRead));
            String publicUrl = GeneratePublicUrlAws.generateUrl(body.asFormUrlEncoded().get("userId")[0], media.getFilename(), "media");
            String mimeType;
            if(FilenameUtils.getExtension(media.getFilename()).equals("mp4")){
                mimeType = "video/mp4";
            } else {
                mimeType = "image/jpeg";
            }
            FeedDAO feedDAO = new FeedDAO();
            feedDAO.createFeed(new Feed(title, publicUrl, mimeType, Tags.valueOf(body.asFormUrlEncoded().get("tag")[0].toUpperCase()), new ObjectId(body.asFormUrlEncoded().get("userId")[0]), body.asFormUrlEncoded().get("eventId") == null ? null : new ObjectId(body.asFormUrlEncoded().get("eventId")[0])));
            response.setSuccess(true);
            return ok(Json.toJson(response));
        }catch (Exception ex){
            response.setError(ex.getMessage());
            return badRequest(Json.toJson(response));
        }
    }
}
