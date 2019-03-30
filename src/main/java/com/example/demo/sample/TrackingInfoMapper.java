package com.example.demo.sample;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrackingInfoMapper {
    TrackingInfo getById(TrackingInfo entity);

    List<TrackingInfo> list();

    void insert(TrackingInfo entity);
}
