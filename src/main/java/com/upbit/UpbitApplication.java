package com.upbit;

import com.api.ExchangeApi.ExchangeApi;
import com.api.ExchangeApi.GetTickSize;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@SpringBootApplication
public class UpbitApplication {

    public static void main(String[] args) throws IOException, ParseException {
//        SpringApplication.run(UpbitApplication.class, args);
        GetTickSize getTickSize = new GetTickSize();
        System.out.println(getTickSize.get_tick_size(1.9654523));

        JSONParser jp = new JSONParser();
        ClassPathResource resource = new ClassPathResource("config/key.json");
        Reader reader = new FileReader(resource.getFile());
        JSONObject jo = (JSONObject) jp.parse(reader);

        String access = (String) jo.get("accessKey");
        String secret = (String) jo.get("secretKey");

        ExchangeApi exchangeApi = new ExchangeApi(access, secret);
        String a = exchangeApi.get_balances();
        JSONArray jsonArray = (JSONArray) jp.parse(a);
        JSONObject b = (JSONObject)jsonArray.get(0);
        System.out.println(b.get("balance"));
//        for (Object object : jsonArray){
//            JSONObject jsonObject = (JSONObject) object;
//            System.out.println(jsonObject.get("currency"));
//        }
    }

}
