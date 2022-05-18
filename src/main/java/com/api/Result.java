package com.api;

public class Result <T, T1> {
    private T type1;
    private T1 type2;

    public Result(){}

    public Result(T type1, T1 type2){
        this.type1 = type1;
        this.type2 = type2;
    }

    public void setFirst(T type1){
        this.type1 = type1;
    }

    public T getFirst(){
        return this.type1;
    }

    public void setSecond(T1 type2){
        this.type2 = type2;
    }

    public T1 getSecond(){
        return this.type2;
    }

    public String toString(){
        return " ";
    }
}
