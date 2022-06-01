package com.slack;

public interface SlackRequest {
    public void build();
    public void _post_message(String channel, String message);
}
