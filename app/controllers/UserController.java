package controllers;

import com.inmobi.user.dao.UserDAO;
import play.db.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import settings.Response;

public class UserController extends Controller {

    public Result login(String name){
        Response response = new Response();
        response.setData(new UserDAO().get(name));
        response.setSuccess(true);
        return ok(Json.toJson(response));
    }
}
