package com.upbit.controller;

//import org.apache.http.HttpRequest;
//import org.apache.http.HttpResponse;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
//import org.springframework.http.HttpRequest;

public interface UpbitController {
    public ResponseEntity<String> hears(@RequestBody Map<String, Object> data) throws Exception;
    public String index() throws Exception;
}
