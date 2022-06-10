package com.upbit.service;

import com.upbit.dto.Execute;
import com.upbit.dto.History;

import java.text.ParseException;
import java.util.List;

public interface UpbitService {
    public String appMentionResponse(String text, String channel, String user) throws ParseException;
    public List<History> getHistory();
    public void setHistory(String ticker) throws ParseException;
}
