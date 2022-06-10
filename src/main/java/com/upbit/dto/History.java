package com.upbit.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class History {
    private Date base_date;
    private double open_price;
    private double high_price;
    private double low_price;
    private double close_price;
    private String table_name;
}
