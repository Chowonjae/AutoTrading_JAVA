package com.upbit.logic;

public class Main {
//    Instance
    private static Main innstance;

    private Main(){}

    public static Main getInstance(){
        if(innstance == null){
            synchronized (Main.class){
                innstance = new Main();
            }
        }
        return innstance;
    }

    public void main(boolean status){
        int i = 0;
        try{
            while(status){
                System.out.println(i);
                i++;
                Thread.sleep(1000);
            }
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }

    }


}
