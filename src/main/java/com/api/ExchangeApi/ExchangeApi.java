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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class ExchangeApi {
    private String access = "";
    private String secret = "";

    public ExchangeApi(){
        JSONParser jsonParser = new JSONParser();
        ClassPathResource resource = new ClassPathResource("config/key.json");
        Reader reader;
        JSONObject jsonObject = null;
        try {
            reader = new FileReader(resource.getFile());
            jsonObject = (JSONObject) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }finally {
            this.access = (String) (jsonObject != null ? jsonObject.get("accessKey") : "access 값이 지정되지 않습니다.");
            this.secret = (String) (jsonObject != null ? jsonObject.get("secretKey") : "secret 값이 지정되지 않습니다.");
        }
    }

//    public void build(){
//        JSONParser jsonParser = new JSONParser();
//        ClassPathResource resource = new ClassPathResource("config/key.json");
//        Reader reader;
//        JSONObject jsonObject = null;
//        try {
//            reader = new FileReader(resource.getFile());
//            jsonObject = (JSONObject) jsonParser.parse(reader);
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }finally {
//            this.access = (String) (jsonObject != null ? jsonObject.get("accessKey") : "access 값이 지정되지 않습니다.");
//            this.secret = (String) (jsonObject != null ? jsonObject.get("secretKey") : "secret 값이 지정되지 않습니다.");
//        }
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
//        JSONArray jArray = null;
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
    public JSONObject buy_limit_order(String ticker, double price, double volume){ return buy_limit_order(ticker, price, volume, false); }
    public JSONObject buy_limit_order(String ticker, double price, double volume, boolean contain_req){
        ResponseDto<String, String[]> responseDto = new ResponseDto<>();
        RequestDto requestDto = new RequestDto();
        JSONObject jsonObject = new JSONObject();
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

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(responseDto.getData());
            jsonObject = (JSONObject) obj;
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }
        return jsonObject;
    }

//    시장가 매도
    public JSONObject sell_market_order(String ticker, double volume){ return sell_market_order(ticker, volume, false); }
    public JSONObject sell_market_order(String ticker, double volume, boolean contain_req){
        ResponseDto<String, String[]> responseDto = new ResponseDto<>();
        RequestDto requestDto = new RequestDto();
        JSONObject jsonObject = new JSONObject();
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

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(responseDto.getData());
            jsonObject = (JSONObject) obj;
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }
        return jsonObject;
    }

}
