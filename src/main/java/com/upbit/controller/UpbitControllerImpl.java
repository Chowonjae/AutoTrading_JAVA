package com.upbit.controller;

import com.upbit.dto.SlackJson;
import com.upbit.service.UpbitService;
import com.upbit.service.UpbitServiceImpl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class UpbitControllerImpl implements UpbitController {
//    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
    private final UpbitService service;

    public UpbitControllerImpl(UpbitService service){ this.service = service; System.out.println("controller 생성자 호출");}

    @Override
    @PostMapping(value = "/slack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> hears(@RequestBody Map<String, Object> data){
        try{
            SlackJson slackJson = new SlackJson(data);
//            log.info("event_type : " + slackJson.getEventType());
            if(slackJson.getEventType().equals("app_mention")){
                String responseText = service.appMentionResponse(slackJson.getText(), slackJson.getChannel(), slackJson.getUser());
                slackJson.setResultText(responseText);
            }

            slackJson.setJson();

            return new ResponseEntity<>(slackJson.getJson(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getCause().toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @GetMapping(value = "/")
    public String index(){
        return "안녕하세요! 여기는 WonJae's Algorithm Trading Center 입니다.";
    }

}
