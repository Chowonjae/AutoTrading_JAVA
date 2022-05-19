package com.api.dto;

public class ResponseDto<T, T1> {
    private T data;
    private T1 remaining;

    public void setData(T data){
        this.data = data;
    }

    public T getData(){
        return this.data;
    }

    public void setRemaining(T1 remaining){
        this.remaining = remaining;
    }

    public T1 getRemaining(){
        return this.remaining;
    }
}
