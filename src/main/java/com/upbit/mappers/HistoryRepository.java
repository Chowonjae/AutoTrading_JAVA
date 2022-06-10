package com.upbit.mappers;

import com.upbit.dto.History;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@org.springframework.stereotype.Repository
public interface HistoryRepository {
    List<History> allHistory();
}
