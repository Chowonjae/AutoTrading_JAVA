package com.upbit.service;

import com.upbit.dto.Execute;
import com.upbit.logic.Main;
import org.springframework.stereotype.Service;

@Service
public class UpbitServiceImpl implements UpbitService{
    private String message;
    private String text;
    private final Main main;

    public UpbitServiceImpl(Main main){ this.main = main; System.out.println("service 생성자 호출"); }

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
