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
import java.util.Arrays;

@SpringBootApplication
public class UpbitApplication {

    public static void main(String[] args) throws IOException, ParseException {
//        SpringApplication.run(UpbitApplication.class, args);
    }

}
