package com.upbit;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;

public class StringToJson {
    public JSONArray stj(String result) throws ParseException{
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(result);
//        System.out.println(jsonArray.get(0));
        return jsonArray;
    }
}
