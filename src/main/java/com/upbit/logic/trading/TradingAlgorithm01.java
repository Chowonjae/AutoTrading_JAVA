package com.upbit.logic.trading;

import com.api.ExchangeApi.ExchangeApi;
import com.api.QuotationApi.QuotationApi;
import com.slack.SlackRequest;
import com.slack.SlackRequestImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TradingAlgorithm01 implements TradingAlgorithm {
    private String[] ticker_list = new String[]{"KRW-BTC", "KRW-ETH", "KRW-XRP", "KRW-ADA", "KRW-TRX"};
    private HashMap<String, Boolean> tradeState = new HashMap<>();
    private HashMap<String, Double> target_price = new HashMap<>();
    private HashMap<String, String> uuidMap = new HashMap<>();

//    private final ExchangeApi exchangeApi;
    private final int targetCount = ticker_list.length;
    private final double buy_percent = 0.2;

//    public TradingAlgorithm01(ExchangeApi exchangeApi) {
//        this.exchangeApi = exchangeApi;
//        this.exchangeApi.build();
//        for (String ticker : this.ticker_list){
//            this.tradeState.put(ticker, false);
//        }
//    }

    @Override
    public void target_price_define(){
        String message = "";
        for (String ticker : this.ticker_list){
            target_price.put(ticker, 0.1);
        }
    }

    @Override
    public void buy(String ticker, double myOrderPrice){
        String message = "";
        ExchangeApi exchangeApi = new ExchangeApi();
        QuotationApi quotationApi = new QuotationApi();

        JSONArray orderBook_array = quotationApi.get_orderBook(ticker);
        JSONObject orderBook_jObject = (JSONObject) orderBook_array.get(0);
        JSONArray orderBook_units = (JSONArray) orderBook_jObject.get("orderbook_units");
        JSONObject orderBook_units_object = (JSONObject) orderBook_units.get(0);
        double sell_price = Double.parseDouble((String) orderBook_units_object.get("ask_price"));

        double unit = (myOrderPrice / sell_price);

        JSONObject buy = exchangeApi.buy_limit_order(ticker, sell_price, unit);
        this.uuidMap.put(ticker, (String) buy.get("uuid"));

        message = String.valueOf(buy.get("avg_price")) + "원에 매수주문을 넣었습니다.";
        slack(message);
    }
//  TODO uuid로 주문 리스트 받아와서 state가 done이면 db에 저장하고 slack 구매했다고 하는 기능 추가, state가 done가 되면 uuidmap의 값 삭제
    @Override
    public void sell() {
        ExchangeApi exchangeApi = new ExchangeApi();

        JSONArray get_balances_ja = exchangeApi.get_balances().getData();

        for (Object o : get_balances_ja) {
            JSONObject jo = (JSONObject) o;
            if (!jo.get("currency").equals("KRW") && !jo.get("currency").equals("DON")) {
                exchangeApi.sell_market_order((String) jo.get("currency"), Double.parseDouble((String) jo.get("balance")));
                String ticker = "KRW-" + (String) jo.get("currency");
                this.tradeState.put(ticker, false);
            }
        }
    }

    private void slack(String message){
        SlackRequest slackRequest = new SlackRequestImpl();
        slackRequest.build();
        slackRequest._post_message("C01V5867612", message);
    }

}
