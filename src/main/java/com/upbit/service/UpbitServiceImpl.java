package com.upbit.service;

import com.api.QuotationApi.QuotationApi;
import com.upbit.dto.Execute;
import com.upbit.dto.History;
import com.upbit.dto.SetHistory;
import com.upbit.logic.Main;
import com.upbit.mappers.HistoryRepository;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UpbitServiceImpl implements UpbitService{
    private String message;
    private String text;
    private final Main main;
    private final HistoryRepository historyRepository;

    public UpbitServiceImpl(Main main, HistoryRepository historyRepository){
        this.main = main;
        this.historyRepository = historyRepository;
        System.out.println("service 생성자 호출");
    }

    public String appMentionResponse(String text, String channel, String user) throws ParseException {
        String result = "";
        String message = text.split(" ")[1];
        Execute execute = new Execute();
        if (message.equals("start")){
            execute.setStatus(true);
            start_stop(execute);
            result = "실행중";
        }else if(message.equals("stop")){
            execute.setStatus(false);
            start_stop(execute);
            result = "정지";
        }else if(message.equals("set")){
            setHistory(text.split(" ")[2]);
        }

        return result;
    }

    private void start_stop(Execute execute){
        this.main.setStatus(execute);
    }

    @Override
    public List<History> getHistory(){
        System.out.println(this.historyRepository.allHistorys().get(0));
        return this.historyRepository.allHistorys();
    }

    @Override
    public void setHistory(String tickers) throws ParseException {
        LocalDate current_dt = LocalDate.now();
        for(String ticker : tickers.split(",")){
            String table_name = ticker + "_history";
            History get_history = this.historyRepository.getHistory(table_name);
            Date last_dt = get_history.getBase_date();
            Period period = Period.between(current_dt, last_dt.toLocalDate());
            QuotationApi quotationApi = new QuotationApi();
            int abs = Math.abs(period.getDays());
            JSONArray ohlcv = quotationApi.get_ohlcv("KRW-" + ticker.toUpperCase(), abs);
            for(int i = abs-1; i > 0; i--){
                JSONObject jsonObject = new JSONObject((Map) ohlcv.get(i));

                SetHistory set_history = new SetHistory();

                String date_str = (String) jsonObject.get("datetime");
                String date = date_str.split("T")[0] + " " + date_str.split("T")[1];


                double high = Double.parseDouble((String) jsonObject.get("high"));
                double close = Double.parseDouble((String) jsonObject.get("close"));
                double low = Double.parseDouble((String) jsonObject.get("low"));
                double open = Double.parseDouble((String) jsonObject.get("open"));

                set_history.setBase_date(date);
                set_history.setHigh_price(high);
                set_history.setClose_price(close);
                set_history.setLow_price(low);
                set_history.setOpen_price(open);
                set_history.setTable_name(table_name);

                this.historyRepository.setHistory(set_history);
            }

        }
    }
}
