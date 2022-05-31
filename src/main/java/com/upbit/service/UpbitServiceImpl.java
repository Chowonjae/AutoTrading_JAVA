package com.upbit.service;

import com.upbit.dto.Execute;
import com.upbit.logic.Main;
import org.springframework.stereotype.Service;

@Service
public class UpbitServiceImpl {
    private String message;
    private String text;
    private final Main main;

    public UpbitServiceImpl(Main m){ this.main = m; }

    public String appMentionResponse(String text, String channel, String user){
        String result = "";
        String message = text.split(" ")[1];
        Execute execute = new Execute();
        if (message.equals("start")){
            execute.setStatus(true);
            result = "실행중";
        }else if(message.equals("stop")){
            execute.setStatus(false);
            result = "정지";
        }

        main.setStatus(execute);

        return result;
    }
}
