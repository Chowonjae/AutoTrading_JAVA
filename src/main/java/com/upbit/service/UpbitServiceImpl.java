package com.upbit.service;

import com.upbit.dto.Execute;
import com.upbit.dto.History;
import com.upbit.logic.Main;
import com.upbit.mappers.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpbitServiceImpl implements UpbitService{
    private String message;
    private String text;
    private final Main main;
    @Autowired
    private HistoryRepository historyRepository;

    public UpbitServiceImpl(Main main){
        this.main = main;
        System.out.println("service 생성자 호출");
    }

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

    @Override
    public List<History> getHistory(){
        System.out.println(2);
        System.out.println(this.historyRepository.allHistory());
        System.out.println(3);
        return this.historyRepository.allHistory();
    }
}
