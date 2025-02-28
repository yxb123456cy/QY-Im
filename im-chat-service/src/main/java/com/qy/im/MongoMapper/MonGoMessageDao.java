package com.qy.im.MongoMapper;


import com.anwen.mongo.mapper.MongoMapper;
import com.qy.im.document.MonGoMessage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface MonGoMessageDao extends MongoMapper<MonGoMessage> {
}
