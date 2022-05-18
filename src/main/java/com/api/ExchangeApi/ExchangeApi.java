package com.api.ExchangeApi;

import com.api.RequestApi;
import com.api.RequestApi.*;
import com.api.Result;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ExchangeApi {
    String access = "";
    String secret = "";

    public ExchangeApi(String access, String secret){
        this.access = access;
        this.secret = secret;
    }

    private String[] _convertToStringToArray(String StA){
        String[] result = new String[2];
        String[] StAArray = StA.split(", ");
        String[] req = new String[3];
        String a = StAArray[0].replaceFirst("\\[", "");
        req[0] = StAArray[1].replaceFirst("\\[", "");
        req[1] = StAArray[2];
        req[2] = StAArray[3].replace("]]", "");
        result[0] = a;
        result[1] = Arrays.toString(req);

        return result;
    }

    private JSONArray _convertToJson(String result){
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

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
    public Result<String, String[]> get_balances(){
        return get_balances(false);
    }
    public Result<String, String[]> get_balances(boolean contain_req){
        JSONArray jArray = null;
        Result<String, String[]> response = null;
        String result1 = "";
        String result2 = "";
        try{
            String url = "https://api.upbit.com/v1/accounts";
            String authenticationToken = _request_headers();
            RequestApi requestApi = new RequestApi();
            response = requestApi._send_get_request(url, authenticationToken);
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

            Result<String, String[]> balancesReq = get_balances(true);

            String balances = balancesReq.getFirst();
            String[] req = balancesReq.getSecond();
            for (Object o : _convertToJson(balances)){
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
        Result<String, String[]> result = new Result<>();
        try{
            String url = "https://api.upbit.com/v1/orders";
            HashMap<String, String> params = new HashMap<>();
            params.put("market", ticker);
            params.put("side", "bid");
            params.put("volume", String.format("%.8f", volume - 0.00000001));
            params.put("price", String.valueOf(price));
            params.put("ord_type", "limit");
            String authenticationToken = _request_headers_withQuery(params);
            RequestApi requestApi = new RequestApi();
            result = requestApi._send_post_request(url, authenticationToken, params);
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }
        return result.getFirst();
    }

//    시장가 매도
    public String sell_market_order(String ticker, double volume, boolean contain_req){
        Result<String, String[]> result = new Result<>();
        try{
            String url = "https://api.upbit.com/v1/orders";
            HashMap<String, String> params = new HashMap<>();
            params.put("market", ticker);
            params.put("side", "ask");
            params.put("volume", String.valueOf(volume));
            params.put("ord_type", "market");
            String authenticationToken = _request_headers_withQuery(params);
            RequestApi requestApi = new RequestApi();
            result = requestApi._send_post_request(url, authenticationToken, params);
        }catch (Exception e){
            System.out.println(e.getClass().getName());
        }
        return result.getFirst();
    }

}
