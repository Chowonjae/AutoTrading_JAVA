package com.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class RequestApi {
    Integer HTTP_RESP_CODE_START = 200;
    Integer HTTP_RESP_CODE_END = 400;

//    public void _parse_remaining_req(remaining_req){
//        try{
//
//        }
//    }
    public String _send_get_request(String url, String authenticationToken){
        String result = "";
        HttpEntity entity = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= HTTP_RESP_CODE_START && statusCode < HTTP_RESP_CODE_END){
                entity = response.getEntity();
            }

            result = EntityUtils.toString(entity, "UTF-8");
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
