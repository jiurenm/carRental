package com.edu.carportal.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/21 13:10
 */
@Mapper
public interface MessageDao {
    @Insert("INSERT INTO messagetext(id, title, message) VALUES (#{id}, #{title}, #{message})")
    int send(@Param("id") Long id, @Param("title") String title, @Param("message") String message);

    @Insert("INSERT INTO message(date, uid, recId, messageId, `status`) VALUES (#{date}, #{uid}, 1094837868982452224, #{messageId}, 0)")
    int send2(@Param("date") String date, @Param("uid") String uid, @Param("messageId") Long messageId);
}
