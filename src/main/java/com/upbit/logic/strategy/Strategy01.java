package com.upbit.logic.strategy;

import com.api.ExchangeApi.ExchangeApi;
import com.api.QuotationApi.QuotationApi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Strategy01 {
    private final QuotationApi quotationApi;

    public Strategy01(QuotationApi quotationApi){
        this.quotationApi = quotationApi;
    }

    private double[] separation(JSONArray jsonArray){
        double[] result = new double[3];
        JSONObject previous_day = (JSONObject) jsonArray.get(1);
        JSONObject very_day = (JSONObject) jsonArray.get(0);
        result[0] = (double) very_day.get("open");
        result[1] = (double) previous_day.get("high");
        result[2] = (double) previous_day.get("low");
        return result;
    }

//    private double noise_range(String ticker){
//
//    }

    public double setTarget_price(String ticker){
        double result = 0.0;
        JSONArray jsonArray = quotationApi.get_ohlcv(ticker, "day", 2);
        double[] num = separation(jsonArray);
//        double k = noise_range(ticker);
        return result;
    }
}
