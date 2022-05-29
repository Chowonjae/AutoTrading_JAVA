package com.api.QuotationApi;

import com.api.RequestApi;
import com.api.dto.RequestDto;
import com.api.dto.ResponseDto;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuotationApi {
//    데이터 가공영역 start {
    private String Object_Double(Object o){
        BigDecimal result = new BigDecimal(Double.parseDouble(String.valueOf(o)));
        return result.toString();
    }

    private JSONArray ohlcv_conversion(JSONArray ja){
        JSONArray result = new JSONArray();
        for (Object o : ja){
            JSONObject jo = (JSONObject) o;
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("datetime", String.valueOf(jo.get("candle_date_time_kst")));
            hashMap.put("open", Object_Double(jo.get("opening_price")));
            hashMap.put("high", Object_Double(jo.get("high_price")));
            hashMap.put("low", Object_Double(jo.get("low_price")));
            hashMap.put("close", Object_Double(jo.get("trade_price")));
            hashMap.put("volume", Object_Double(jo.get("candle_acc_trade_volume")));
            hashMap.put("value", Object_Double(jo.get("candle_acc_trade_price")));
            result.add((Object) hashMap);
        }
        return result;
    }

    private  JSONArray orderBook_conversion(JSONArray ja){
        for(Object o : ja){
            JSONObject jo = (JSONObject) o;
            JSONArray jsonArray = (JSONArray) jo.get("orderbook_units");

            for(int i = 0; i < jsonArray.size(); i++){
                JSONObject joj = (JSONObject) jsonArray.get(i);
                joj.put("bid_price", Object_Double(joj.get("bid_price")));
                joj.put("ask_price", Object_Double(joj.get("ask_price")));
            }
        }
        return ja;
    }

    private static boolean isInstance(String to, String s) throws ClassNotFoundException {
        return Class.forName(s).isInstance(to);
    }
//    } 데이터 가공영역 end

    private String get_url_ohlcv(String interval){
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

    public ResponseDto<List<String>, String[]> get_tickers(){ return get_tickers("", false, false, false); }
    public ResponseDto<List<String>, String[]> get_tickers(String fiat) { return get_tickers(fiat, false, false, false); }
    public ResponseDto<List<String>, String[]> get_tickers(String fiat, boolean is_details,
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

//  return keys ('volume', 'datetime', 'high', 'low', close', 'value', 'open') type (JSONArray)
    public JSONArray get_ohlcv(String ticker) throws ClassNotFoundException { return get_ohlcv(ticker, "day", 200, ""); }
    public JSONArray get_ohlcv(String ticker, String interval, int count, String to) throws ClassNotFoundException {
        int MAX_CALL_COUNT = 200;
        RequestDto requestDto = new RequestDto();
        ResponseDto<JSONArray, String[]> response = new ResponseDto<>();
        HashMap<String, String> params = new HashMap<>();
        String dt_str = "";

//        to : 마지막 캔들 시각
        if (to.equals("")){
            LocalDate curDate = LocalDate.now();
            LocalTime curTime = LocalTime.now();
            LocalDateTime dt = LocalDateTime.of(curDate, curTime);
            dt_str = dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        }else if(isInstance(to, "java.lang.String")){
            dt_str = to;
        }
        String count_str = String.valueOf(Math.max(count, 1));

        params.put("market", ticker);
        params.put("count", count_str);
        params.put("to", dt_str);

        try{
            requestDto.setUrl(get_url_ohlcv(interval));
            requestDto.setParams(params);
            RequestApi requestApi = new RequestApi();
            response = requestApi._call_public_api(requestDto);
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }
        return ohlcv_conversion(response.getData());
    }

    public JSONArray get_current_price(String ticker){ return get_current_price(ticker, false, false); }
    public JSONArray get_current_price(String ticker, boolean limit_info, boolean verbose){
        ResponseDto<JSONArray, String[]> response = new ResponseDto<>();
        RequestDto requestDto = new RequestDto();
        HashMap<String, String> params = new HashMap<>();
        params.put("market", ticker);

        try{
            requestDto.setUrl("https://api.upbit.com/v1/trades/ticks");
            requestDto.setParams(params);
            RequestApi requestApi = new RequestApi();
            response = requestApi._call_public_api(requestDto);
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }
        return response.getData();
    }

    public JSONArray get_orderBook(String ticker){ return get_orderBook(ticker, false); }
    public JSONArray get_orderBook(String ticker, boolean limit_info){
        ResponseDto<JSONArray, String[]> response = new ResponseDto<>();
        RequestDto requestDto = new RequestDto();
        HashMap<String, String> params = new HashMap<>();
        params.put("markets", ticker);

        try{
            requestDto.setUrl("https://api.upbit.com/v1/orderbook");
            requestDto.setParams(params);
            RequestApi requestApi = new RequestApi();
            response = requestApi._call_public_api(requestDto);
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }

        return orderBook_conversion(response.getData());
    }
    // json이 지수 표현으로 되어있는지를 확인해서 지수인 값만 변경할 수 있도록 하면 좋을듯
}
