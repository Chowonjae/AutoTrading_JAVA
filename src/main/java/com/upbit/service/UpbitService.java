package com.upbit.service;

import com.upbit.dto.History;

import java.util.List;

public interface UpbitService {
    public String appMentionResponse(String text, String channel, String user);
    public List<History> getHistory();
}
