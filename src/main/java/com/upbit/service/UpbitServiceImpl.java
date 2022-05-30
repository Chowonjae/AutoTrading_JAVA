package com.upbit.service;

import com.upbit.logic.Main;
import org.springframework.stereotype.Service;

@Service
public class UpbitServiceImpl {
    private String message;
    private String text;
    private final Main m = Main.getInstance();

//    public UpbitServiceImpl(Main m){ this.m = m; }

    public String appMentionResponse(String text, String channel, String user){
        String result = "";
        String message = text.split(" ")[1];

        if (message.equals("start")){
            start(true);
            result = "실행중";
        }else if(message.equals("stop")){
            stop(false);
            result = "정지";
        }

        return result;
    }

    public void start(boolean status){
        m.main(status);
    }
    public void stop(boolean status){
        m.main(status);
    }
}
