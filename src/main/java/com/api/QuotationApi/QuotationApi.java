package com.api.QuotationApi;

import com.api.RequestApi;
import com.api.dto.RequestDto;
import com.api.dto.ResponseDto;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    public static ResponseDto<List<String>, String[]> get_tickers(){ return get_tickers("", false, false, false); }
    public static ResponseDto<List<String>, String[]> get_tickers(String fiat) { return get_tickers(fiat, false, false, false); }
    public static ResponseDto<List<String>, String[]> get_tickers(String fiat, boolean is_details,
                                                        boolean limit_info, boolean verbose){
        ResponseDto<JSONArray, String[]> response;
        ResponseDto<List<String>, String[]> result = new ResponseDto<>();
        RequestDto requestDto = new RequestDto();
        HashMap<String, String> params = new HashMap<>();
        List<String> tickers = new ArrayList<>();
        params.put("is_details", String.valueOf(is_details));
        try{
            requestDto.setUrl("https://api.upbit.com/v1/market/all");
            requestDto.setParams(params);
            RequestApi requestApi = new RequestApi();
            response = requestApi._call_public_api(requestDto);

            if (!verbose){
                for (Object o : response.getData()){
                    JSONObject jsonObject = (JSONObject) o;
                    if (jsonObject.get("market").toString().startsWith(fiat)){
                        tickers.add((String) jsonObject.get("market"));
                    }
                }
                result.setData(tickers);
            }
            result.setRemaining(response.getRemaining());

        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }

        return result;
    }

    public static void main(String[] argv){
//        System.out.println(get_url_ohlcv("month"));
        System.out.println(get_tickers("KRW").getData());
    }
}
