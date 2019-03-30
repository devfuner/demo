package com.example.demo.sample;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrackingHistoryMapper {
    List<TrackingHistory> list();

    void insert(TrackingHistory entity);

    void delete(String invoiceNo);
//    void delete(TrackingHistory entity);
}
