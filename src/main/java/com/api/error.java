//package com.api;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//public class UpbitError(Exception){
//    @Override
//    public String toString() {
//        return "Upbit Base Error";
//    }
//}
//
//public class CreateAskError(UpbitError){
//    @Override
//    public String toString() {
//        return "주문 요청 정보가 올바르지 않습니다.";
//    }
//}
//
//public class CreateBidError(UpbitError){
//    @Override
//    public String toString() {
//        return "주문 요청 정보가 올바르지 않습니다.";
//    }
//}
//
//public class error {
//    public String raise_error(HttpResponse response) throws ParseException{
//        JSONParser parser = new JSONParser();
//        HttpEntity entity = response.getEntity();
//        String entity_string = entity.toString();
//        JSONObject jsonObject = (JSONObject) parser.parse(entity_string);
//        JSONObject error = (JSONObject) jsonObject.get("error");
//        String message = (String) error.get("message");
//        String name = (String) error.get("name");
//        int code = response.getStatusLine().getStatusCode();
//
//        System.out.println(code);
//        System.out.println(message);
//        System.out.println(name);
//
//        if (code == 429){
//            return new TooManyRequest().toString();
//        }else if (code == 401){
//            if (name.equals("jwt_verification")){
//                return new JwtVerification().toString();
//            }else if (name.equals("invalid_access_key")){
//                return new InValidAccessKey().toString();
//            }
//        }else{
//            return new UpbitError().toString();
//        }
//    }
//}
