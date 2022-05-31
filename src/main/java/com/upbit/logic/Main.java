package com.upbit.logic;

import com.upbit.dto.Execute;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Main {
    private boolean status = false;
    private boolean temp = true;

    public void setStatus(Execute execute){
        this.status = execute.getStatus();
    }

    @Scheduled(fixedRate = 5000)
    private void execute_schedule() throws InterruptedException{
        System.out.println(this.status);
        if (this.status == this.temp){
            if(this.status){
                //            실행 됐다고 슬랙에 전송 구현
            }else {
//                정지 됐다고 슬랙에 전송 구현
            }
            this.temp = !this.status;
            main();
        }else {
            System.out.println("명령 대기 중");
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
