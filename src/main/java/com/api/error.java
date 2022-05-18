package com.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class UpbitError{
    @Override
    public String toString() {
        return "Upbit Base Error";
    }
}

class CreateAskError{
    @Override
    public String toString() {
        return "주문 요청 정보가 올바르지 않습니다.";
    }
}

class CreateBidError{
    @Override
    public String toString() {
        return "주문 요청 정보가 올바르지 않습니다.";
    }
}

class InsufficientFundsAsk{
    @Override
    public String toString() {
        return "매수/매도 가능 잔고가 부족합니다.";
    }
}

class InsufficientFundsBid{
    @Override
    public String toString(){
        return "매수/매도 가능 잔고가 부족합니다.";
    }
}

class UnderMinTotalAsk{
    @Override
    public String toString(){
        return "주문 요청 금액이 최소 주문 금액 미만입니다.";
    }
}

class UnderMinTotalBid{
    @Override
    public String toString(){
        return "주문 요청 금액이 최소 주문 금액 미만입니다.";
    }
}

class WidthdrawAddressNotRegisterd{
    @Override
    public String toString(){
        return "허용되지 않은 출금 주소입니다.";
    }
}

class ValidationError{
    @Override
    public String toString(){
        return "잘못된 API 요청입니다.";
    }
}

class InvalidQueryPayload{
    @Override
    public String toString(){
        return "JWT 헤더의 페이로드가 올바르지 않습니다.";
    }
}

class JwtVerification{
    @Override
    public String toString(){
        return "JWT 토큰 검증에 실패했습니다.";
    }
}

class ExpiredAccessKey{
    @Override
    public String toString(){
        return "API 키가 만료되었습니다.";
    }
}

class NonceUsed{
    @Override
    public String toString(){
        return "이미 요청한 nonce값이 다시 사용되었습니다.";
    }
}

class NoAutorizationIP{
    @Override
    public String toString(){
        return "허용되지 않은 IP 주소입니다.";
    }
}

class OutOfScope{
    @Override
    public String toString(){
        return "허용되지 않은 기능입니다.";
    }
}

class TooManyRequests{
    @Override
    public String toString(){
        return "요청 수 제한을 초과했습니다.";
    }
}

class RemainingReqParsingError{
    @Override
    public String toString(){
        return "요청 수 제한 파싱에 실패했습니다.";
    }
}

class InValidAccessKey{
    @Override
    public String toString(){
        return "잘못된 엑세스 키입니다.";
    }
}

public class error {
    public String raise_error(HttpResponse response) throws ParseException{
        JSONParser parser = new JSONParser();
        HttpEntity entity = response.getEntity();
        String entity_string = entity.toString();
        JSONObject jsonObject = (JSONObject) parser.parse(entity_string);
        JSONObject error = (JSONObject) jsonObject.get("error");
        String message = (String) error.get("message");
        String name = (String) error.get("name");
        int code = response.getStatusLine().getStatusCode();

        System.out.println(code);
        System.out.println(message);
        System.out.println(name);

        if (code == 429){
            return new TooManyRequests().toString();
        }else if (code == 401){
            if (name.equals("jwt_verification")){
                return new JwtVerification().toString();
            }else if (name.equals("invalid_access_key")){
                return new InValidAccessKey().toString();
            }
        }else{
            return new UpbitError().toString();
        }
        return "";
    }
}
