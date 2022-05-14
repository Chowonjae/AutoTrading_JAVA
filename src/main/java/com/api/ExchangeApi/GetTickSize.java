package com.api.ExchangeApi;

public class GetTickSize {

    public double get_tick_size(double price){
        return get_tick_size(price, "floor");
    }

    private double get_func(double price, double unit, String method){
        if (method.equals("floor")){
            return Math.floor(price/unit);
        }else if (method.equals("round")){
            return Math.round(price/unit);
        }else {
            return Math.ceil(price/unit);
        }
    }

    public double get_tick_size(double price, String method){
        double tick_size;

        if (price >= 2000000){
            tick_size = get_func(price, 1000, method) * 1000;
        }else if (price >= 1000000){
            tick_size = get_func(price, 500, method) * 500;
        }else if (price >= 500000){
            tick_size = get_func(price, 100, method) * 100;
        }else if (price >= 100000){
            tick_size = get_func(price, 50, method) * 50;
        }else if (price >= 10000){
            tick_size = get_func(price, 10, method) * 10;
        }else if (price >= 1000){
            tick_size = get_func(price, 5, method) * 5;
        }else if (price >= 100){
            tick_size = get_func(price, 1, method) * 1;
        }else if (price >= 10){
            tick_size = get_func(price, 0.1, method) / 10;
        }else {
            tick_size = get_func(price, 0.01, method) / 100;
        }
        return tick_size;
    }

}
