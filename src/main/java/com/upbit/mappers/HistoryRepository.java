package com.upbit.mappers;

import com.upbit.dto.History;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@org.springframework.stereotype.Repository
public interface HistoryRepository {
    List<History> allHistorys();
    History getHistory(String table_name);
    void setHistory(History history);
}
