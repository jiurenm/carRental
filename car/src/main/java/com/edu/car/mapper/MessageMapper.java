package com.edu.car.mapper;

import com.edu.car.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/2/28 9:34
 */
@Mapper
public interface MessageMapper {
    @Select("SELECT m.id,mt.title,mt.message,m.date,m.uid,m.recId,m.messageId,m.status FROM car.message m JOIN car.messagetext mt WHERE m.recId=#{recId} AND m.messageId=mt.id")
    List<Message> showMessage(Long recId);

    @Select("SELECT m.id,mt.title,mt.message,m.date,m.uid,m.recId,m.messageId FROM car.message m JOIN car.messagetext mt WHERE m.uid=#{uid} AND m.messageId=mt.id")
    List<Message> showSendMessage(Long uid);

    @Update("UPDATE car.message m SET m.status=1 WHERE m.id=#{id}")
    int readMessage(Long id);

    @Update("UPDATE car.message m SET m.status=2 WHERE m.id=#{id}")
    int deleteMessage(Long id);

    @Update("UPDATE car.message m SET m.status=1 WHERE m.id=#{id}")
    int revertMessage(Long id);

    @Delete("DELETE FROM car.message WHERE id=#{id} AND `status`=2")
    int clearMessage(Long id);

    @Insert("INSERT INTO car.messagetext(id, title, message) VALUES (#{id}, #{title}, #{message})")
    int send(@Param("id") Long id, @Param("title") String title, @Param("message") String message);

    @Insert("INSERT INTO car.message(date, recId, messageId) VALUES (#{date}, #{recId}, #{messageId})")
    int send1(@Param("date") String date, @Param("recId") Long recId, @Param("messageId") Long messageId);
}
