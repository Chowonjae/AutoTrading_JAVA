package com.api;

import com.api.dto.RequestDto;
import com.api.dto.ResponseDto;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestApi {
    Integer HTTP_RESP_CODE_START = 200;
    Integer HTTP_RESP_CODE_END = 400;

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

    public static String[] _parse_remaining_req(String remaining_req){
        String[] result = new String[3];
        try{
            Pattern p = Pattern.compile("group=([a-z]+); min=([0-9]+); sec=([0-9]+)");
            Matcher m = p.matcher(remaining_req);

            while (m.find()){
                String k = m.group().replace(";", "");
                String[] a = k.split(" ");
                for (int i = 0; i < a.length; i++){
                    result[i] = a[i].split("=")[1];
                }
            }

        }catch (Exception e){
            System.out.println(e.getClass().getName());
            Arrays.fill(result, null);
        }
        return result;
    }

    public ResponseDto<JSONArray, String[]> _call_public_api(RequestDto requestDto){
        ResponseDto<JSONArray, String[]> result = new ResponseDto<>();
        OkHttpClient client = new OkHttpClient();
        ArrayList<String> queryElements = new ArrayList<>();
        String queryString = "";
        if (requestDto.getParams() != null){
            for (Map.Entry<String, String> entity : requestDto.getParams().entrySet()) {
                queryElements.add(entity.getKey() + "=" + entity.getValue());
            }
            queryString = String.join("&", queryElements.toArray(new String[0]));
        }

        try{
            Request request = new Request.Builder()
                    .url(requestDto.getUrl() + "?" + queryString)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            int statusCode = response.code();
            if (statusCode >= HTTP_RESP_CODE_START && statusCode < HTTP_RESP_CODE_END){
                String remaining_req = response.header("Remaining-Req");
                result.setRemaining(_parse_remaining_req(remaining_req));
                result.setData(_convertToJson(Objects.requireNonNull(response.body()).string()));
            }else{
                error error = new error();
                error.raise_error(response);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public ResponseDto<String, String[]> _send_post_request(RequestDto requestDto){
        ResponseDto<String, String[]> result = new ResponseDto<>();
        HttpEntity entity = null;
        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(requestDto.getUrl());
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", requestDto.getAuthenticationToken());
            request.setEntity(new StringEntity(new Gson().toJson(requestDto.getParams())));

            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= HTTP_RESP_CODE_START && statusCode < HTTP_RESP_CODE_END){
                Header[] remaining_req_array = response.getHeaders("Remaining-Req");
                String remaining_req = String.valueOf(remaining_req_array[0]);
                result.setRemaining(_parse_remaining_req(remaining_req));
                entity = response.getEntity();
                result.setData(EntityUtils.toString(entity, "UTF-8"));
            }else {
                error error = new error();
                error.raise_error(response);
            }
        }catch (IOException | ParseException e){
            e.printStackTrace();
        }
        return result;
    }

//    public ResponseDto<JSONArray, String[]> _send_get_request(String url, String authenticationToken){
//        return _send_get_request(url, authenticationToken, null);
//    }
    public ResponseDto<JSONArray, String[]> _send_get_request(RequestDto requestDto){
        ResponseDto<JSONArray, String[]> result = new ResponseDto<>();
        HttpEntity entity = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(requestDto.getUrl());
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", requestDto.getAuthenticationToken());

            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= HTTP_RESP_CODE_START && statusCode < HTTP_RESP_CODE_END){
                Header[] remaining_req_array = response.getHeaders("Remaining-Req");
                String remaining_req = String.valueOf(remaining_req_array[0]);
                result.setRemaining(_parse_remaining_req(remaining_req));
                entity = response.getEntity();
                result.setData(_convertToJson(EntityUtils.toString(entity, "UTF-8")));
            }else{
                error error = new error();
                error.raise_error(response);
            }
        }catch (IOException | ParseException e){
            e.printStackTrace();
        }
        return result;
    }

    public ResponseDto<String, String[]> _send_delete_request(String url, String authenticationToken){
        ResponseDto<String, String[]> result = new ResponseDto<>();
        HttpEntity entity = null;
        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpDelete request = new HttpDelete(url);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= HTTP_RESP_CODE_START && statusCode < HTTP_RESP_CODE_END){
                Header[] remaining_req_array = response.getHeaders("Remaining-Req");
                String remaining_req = String.valueOf(remaining_req_array[0]);
                result.setRemaining(_parse_remaining_req(remaining_req));
                entity = response.getEntity();
                result.setData(EntityUtils.toString(entity, "UTF-8"));
            }else{
                error error = new error();
                error.raise_error(response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    // 남은 요청 횟수 조회해서 거래 일시 정지 밑 진행 추가
    public static void main(String[] argb){

    }
}
