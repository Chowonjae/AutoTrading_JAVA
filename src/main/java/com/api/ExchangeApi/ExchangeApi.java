package com.api.ExchangeApi;

import com.api.RequestApi;
import com.api.dto.RequestDto;
import com.api.dto.ResponseDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class ExchangeApi {
    private String access = "";
    private String secret = "";
    @Value("${upbit.url.ExchangeApi.asset.accounts}")
    private String accounts;
    @Value("${upbit.url.ExchangesApi.bill.order}")
    private String order;
    @Value("${upbit.url.ExchangesApi.bill.orders}")
    private String orders;
    @Value("${upbit.url.ExchangesApi.bill.chance}")
    private String chance;

    public ExchangeApi(String access, String secret){
        this.access = access;
        this.secret = secret;
    }

//    private String[] _convertToStringToArray(String StA){
//        String[] result = new String[2];
//        String[] StAArray = StA.split(", ");
//        String[] req = new String[3];
//        String a = StAArray[0].replaceFirst("\\[", "");
//        req[0] = StAArray[1].replaceFirst("\\[", "");
//        req[1] = StAArray[2];
//        req[2] = StAArray[3].replace("]]", "");
//        result[0] = a;
//        result[1] = Arrays.toString(req);
//
//        return result;
//    }
//
//    private JSONArray _convertToJson(String result){
//        JSONParser jsonParser = new JSONParser();
//        JSONArray jsonArray = null;
//        try {
//            jsonArray = (JSONArray) jsonParser.parse(result);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return jsonArray;
//    }

    private String _request_headers(){
        Algorithm algorithm = Algorithm.HMAC256(this.secret);
        String jwtToken = JWT.create()
                .withClaim("access_key", access)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        return "Bearer " + jwtToken;
    }
    private String _request_headers_withQuery(HashMap<String, String> params)
            throws NoSuchAlgorithmException {
        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()){
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes(StandardCharsets.UTF_8));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC256(this.secret);
        String jwtToken = JWT.create()
                .withClaim("access_key", this.access)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        return "Bearer " + jwtToken;
    }

//    전체 계좌 조회
    public ResponseDto<JSONArray, String[]> get_balances(){
        return get_balances(false);
    }
    public ResponseDto<JSONArray, String[]> get_balances(boolean contain_req){
        JSONArray jArray = null;
        ResponseDto<JSONArray, String[]> response = null;
        RequestDto requestDto = new RequestDto();
        try{
            requestDto.setUrl("https://api.upbit.com/v1/accounts");
            requestDto.setAuthenticationToken(_request_headers());
            RequestApi requestApi = new RequestApi();
            response = requestApi._send_get_request(requestDto);
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }
        return response;
    }

//    특정 코인 잔고 조회
    public double get_balance(){
        return get_balance("KRW", false);
    }
    public double get_balance(String ticker){
        return get_balance(ticker, false);
    }
    public double get_balance(String ticker, boolean contain_req){
        String ticker_name = "";
        double balance = 0.0;
        try{
            if (ticker.contains("-")){
                ticker_name = ticker.split("-")[1];
            }

            ResponseDto<JSONArray, String[]> balancesReq = get_balances(true);

            JSONArray balances = balancesReq.getData();
            String[] req = balancesReq.getRemaining();
            for (Object o : balances){
                JSONObject jObject = (JSONObject) o;
                if (jObject.get("currency").equals(ticker_name)){
                    balance = Double.parseDouble((String) jObject.get("balance"));
                    break;
                }
            }
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }

        return balance;
    }

//    -------------------------------------------------------------------------------
//    주문
//    -------------------------------------------------------------------------------
//    지정가 매수 (지정가 매수는 price가 호가, 시장가 매수는 price가 내가 주문하길 원하는 잔고)
    public String buy_limit_order(String ticker, int price, double volume, boolean contain_req){
        ResponseDto<String, String[]> responseDto = new ResponseDto<>();
        RequestDto requestDto = new RequestDto();
        try{
            requestDto.setUrl("https://api.upbit.com/v1/orders");
            HashMap<String, String> params = new HashMap<>();
            params.put("market", ticker);
            params.put("side", "bid");
            params.put("volume", String.format("%.8f", volume - 0.00000001));
            params.put("price", String.valueOf(price));
            params.put("ord_type", "limit");
            requestDto.setAuthenticationToken(_request_headers_withQuery(params));
            requestDto.setParams(params);
            RequestApi requestApi = new RequestApi();
            responseDto = requestApi._send_post_request(requestDto);
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }
        return responseDto.getData();
    }

//    시장가 매도
    public String sell_market_order(String ticker, double volume, boolean contain_req){
        ResponseDto<String, String[]> responseDto = new ResponseDto<>();
        RequestDto requestDto = new RequestDto();
        try{
            requestDto.setUrl("https://api.upbit.com/v1/orders");
            HashMap<String, String> params = new HashMap<>();
            params.put("market", ticker);
            params.put("side", "ask");
            params.put("volume", String.valueOf(volume));
            params.put("ord_type", "market");
            requestDto.setAuthenticationToken(_request_headers_withQuery(params));
            requestDto.setParams(params);
            RequestApi requestApi = new RequestApi();
            responseDto = requestApi._send_post_request(requestDto);
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }
        return responseDto.getData();
    }

}
