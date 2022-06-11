package com.upbit.logic.trading;

public interface TradingAlgorithm {
    public void buy(String ticker, double myOrderPrice);
    public void sell();
    public void target_price_define();
}
