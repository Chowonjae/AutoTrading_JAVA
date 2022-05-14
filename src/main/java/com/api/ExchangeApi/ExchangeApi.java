package com.api.ExchangeApi;

import com.api.RequestApi;
import com.api.RequestApi.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.UUID;

public class ExchangeApi {
    String access = "";
    String secret = "";

    public ExchangeApi(String access, String secret){
        this.access = access;
        this.secret = secret;
    }

    private String _request_headers(){
        Algorithm algorithm = Algorithm.HMAC256(this.secret);
        String jwtToken = JWT.create()
                .withClaim("access_key", access)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        return "Bearer " + jwtToken;
    }
    public String get_balances(){
        return get_balances(false);
    }
    public String get_balances(boolean contain_req){
        String url = "https://api.upbit.com/v1/accounts";
        String authenticationToken = _request_headers();
        RequestApi requestApi = new RequestApi();
        String result = requestApi._send_get_request(url, authenticationToken);

//        if (contain_req) return result;
//        else return result.getContent(0);
        return result;
    }

}
