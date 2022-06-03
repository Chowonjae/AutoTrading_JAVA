package com.upbit.logic.trading;

import com.api.ExchangeApi.ExchangeApi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TradingAlgorithm01 implements TradingAlgorithm {
    private String[] ticker_list = new String[]{"KRW-BTC", "KRW-ETH", "KRW-XRP", "KRW-ADA", "KRW-LTC"};
    private HashMap<String, Boolean> tradeState = new HashMap<>();
    private HashMap<String, Double> target_price = new HashMap<>();

    private final ExchangeApi exchangeApi;
    private final int targetCount = ticker_list.length;
    private final double buy_percent = 0.2;

    public TradingAlgorithm01(ExchangeApi exchangeApi) {
        this.exchangeApi = exchangeApi;
        this.exchangeApi.build();
        for (String ticker : this.ticker_list){
            this.tradeState.put(ticker, false);
        }
    }

    @Override
    public void target_price_define(){
        String message = "";
        for (String ticker : this.ticker_list){
            target_price.put(ticker, 0.1);
        }
    }

    @Override
    public void buy(){
        ExchangeApi exchangeApi = new ExchangeApi();
        exchangeApi.build();


    }

    @Override
    public void sell() {
        //true 값을 생성자 호출에 넣어줘야 작
//        ExchangeApi exchangeApi = new ExchangeApi();
//        exchangeApi.build();

        JSONArray get_balances_ja = exchangeApi.get_balances().getData();

        for (Object o : get_balances_ja) {
            JSONObject jo = (JSONObject) o;
            if (!jo.get("currency").equals("KRW") && !jo.get("currency").equals("DON")) {
                this.exchangeApi.sell_market_order((String) jo.get("currency"), Double.parseDouble((String) jo.get("balance")));
                String ticker = "KRW-" + (String) jo.get("currency");
                this.tradeState.put(ticker, false);
            }
        }
    }

}
