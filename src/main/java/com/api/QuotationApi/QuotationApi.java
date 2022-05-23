package com.api.QuotationApi;

import com.api.dto.RequestDto;
import com.api.dto.ResponseDto;
import org.json.simple.JSONArray;

import java.util.Arrays;

public class QuotationApi {
//    private String _request_headers(){
//
//    }
    private static String get_url_ohlcv(String interval){
        String url = "";
        String[] a = new String[3];

        if (Arrays.asList(new String[]{"day", "days"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/days";
        }else if (Arrays.asList(new String[]{"minute1", "minutes1"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/minutes/1";
        }else if (Arrays.asList(new String[]{"minute3", "minutes3"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/minutes/3";
        }else if (Arrays.asList(new String[]{"minute5", "minutes5"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/minutes/5";
        }else if (Arrays.asList(new String[]{"minute10", "minutes10"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/minutes/10";
        }else if (Arrays.asList(new String[]{"minute15", "minutes15"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/minutes/15";
        }else if (Arrays.asList(new String[]{"minute30", "minutes30"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/minutes/30";
        }else if (Arrays.asList(new String[]{"minute60", "minutes60"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/minutes/60";
        }else if (Arrays.asList(new String[]{"minute240", "minutes240"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/minutes/240";
        }else if (Arrays.asList(new String[]{"week",  "weeks"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/weeks";
        }else if (Arrays.asList(new String[]{"month", "months"}).contains(interval)){
            url = "https://api.upbit.com/v1/candles/months";
        }else {
            url = "https://api.upbit.com/v1/candles/days";
        }
        return url;
    }

//    public ResponseDto<JSONArray, String[]> get_tickers(){return get_tickers("", false, false, false)}
//    public ResponseDto<JSONArray, String[]> get_tickers(){
//        ResponseDto<JSONArray, String[]> response = null;
//        RequestDto requestDto = new RequestDto();
//        try{
//            requestDto.setUrl("https://api.upbit.com/v1/market/all");
//        }
//    }

    public static void main(String[] argv){
        System.out.println(get_url_ohlcv("month"));
    }
}
