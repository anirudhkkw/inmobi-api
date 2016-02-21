package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Result;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FitnessController extends Controller{
    public Result getFitnesssData(){
        String accessToken = "Bearer ya29.jwID9mskdLevi6ju_IzMeJwHa4CfV7m9HJ7x4eu48cbEIbZXC6OCdW2orrhwqxnuhaWM";

        WSRequest request = WS.url("https://www.googleapis.com/fitness/v1/users/me/dataSources/derived:com.google.step_count.delta:com.google.android.gms:estimated_steps/datasets/0-1597515179728708316");
        request.setHeader("Authorization", accessToken);
        request.setHeader("Content-Type", "application/json;encoding=utf-8");
        JsonNode node = request.get().get(3000).asJson();
        ArrayNode result = new ObjectMapper().createArrayNode();

        JsonNode test = new ObjectMapper().createObjectNode();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        Date temp = new Date(new Timestamp(Long.parseLong(node.get("point").get(0).get("startTimeNanos").asText().substring(0, 13))).getTime());
        boolean changeDate = false;
        int tempSteps = 0;
        for(JsonNode i : node.get("point")){

            if(changeDate){
                temp = new Date(new Timestamp(Long.parseLong(i.get("startTimeNanos").asText().substring(0, 13))).getTime());
                changeDate = false;
            }
            Timestamp timestamp = new Timestamp(Long.parseLong(i.get("startTimeNanos").asText().substring(0, 13)));
            Date date = new Date(timestamp.getTime());
            if(simpleDateFormat.format(temp).equals(simpleDateFormat.format(date))){
                tempSteps += i.get("value").get(0).get("intVal").asInt();
            } else {
                test = new ObjectMapper().createObjectNode();
                ((ObjectNode)test).put("date", simpleDateFormat.format(temp));
                ((ObjectNode) test).put("steps", tempSteps);
                tempSteps = i.get("value").get(0).get("intVal").asInt();
                result.add(test);
                changeDate = true;
            }


        }

        return ok(Json.toJson(result));
    }
}
