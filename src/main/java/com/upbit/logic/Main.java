package com.upbit.logic;

import com.api.ExchangeApi.ExchangeApi;
import com.api.dto.ResponseDto;
import com.slack.SlackRequest;
import com.slack.SlackRequestImpl;
import com.upbit.dto.Execute;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Main {
    private boolean status = false;
    private boolean temp = true;

    public void setStatus(Execute execute){ this.status = execute.getStatus(); }

    @Scheduled(fixedRate = 5000)
    private void execute_schedule() throws InterruptedException{
        System.out.println(this.status);
        if (this.status == this.temp){
            SlackRequestImpl slackRequest = new SlackRequestImpl();
            slackRequest.build();
            slackRequest._post_message("C01V5867612", this.status ? "서버 실행" : "서버 정지");
            this.temp = !this.status;
            main();
        }
    }

    private void main(){
        int i = 0;
        sell();
        try{
            while(this.status){
                System.out.println(i);
                i++;
                Thread.sleep(1000);
            }
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }

    }

    private void sell() {
        //true 값을 생성자 호출에 넣어줘야 작
        ExchangeApi exchangeApi = new ExchangeApi();
        exchangeApi.build();

        JSONArray get_balances_ja = exchangeApi.get_balances().getData();

        for (Object o : get_balances_ja) {
            JSONObject jo = (JSONObject) o;
            if (!jo.get("currency").equals("KRW") && !jo.get("currency").equals("DON")) {
                exchangeApi.sell_market_order((String) jo.get("currency"), Double.parseDouble((String) jo.get("balance")));
            }
        }
    }
}
