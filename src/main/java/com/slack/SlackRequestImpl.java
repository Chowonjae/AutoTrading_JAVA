package com.slack;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

@Component
public class SlackRequestImpl implements SlackRequest {
    private String token;

    @Override
    public void build(){
        JSONParser jsonParser = new JSONParser();
        ClassPathResource resource = new ClassPathResource("config/key.json");
        Reader reader;
        JSONObject jsonObject = null;

        try {
            reader = new FileReader(resource.getFile());
            jsonObject = (JSONObject) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        this.token = (String) (jsonObject != null ? jsonObject.get("token") : "token 값이 없습니다.");
    }

    @Override
    public void _post_message(String channel, String message){
        String authorization = "Bearer " + this.token;
        HttpEntity entity;
        HashMap<String, String> params = new HashMap<>();
        params.put("channel", channel);
        params.put("text", message);

        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost("https://slack.com/api/chat.postMessage");
            request.setHeader("Content-Type", "application/json;");
            request.addHeader("Authorization", authorization);
            request.setEntity(new StringEntity(new Gson().toJson(params), "UTF-8"));
            client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
