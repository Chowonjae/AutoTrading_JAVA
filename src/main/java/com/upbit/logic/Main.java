package com.upbit.logic;

import com.slack.SlackRequestImpl;
import com.upbit.dto.Execute;
import com.upbit.logic.trading.TradingAlgorithm;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Main {
    private boolean status = false;
    private boolean temp = true;
    private final TradingAlgorithm tradingAlgorithm;

    public Main(TradingAlgorithm tradingAlgorithm) {
        this.tradingAlgorithm = tradingAlgorithm;
    }

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


}
