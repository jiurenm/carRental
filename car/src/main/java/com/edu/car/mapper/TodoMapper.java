package com.edu.car.mapper;

import com.edu.car.model.TodoList;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/17 9:57
 */
@Mapper
public interface TodoMapper {
    @Select("SELECT t.id,t.title,t.status FROM car.todoList t WHERE t.uid=#{id}")
    List<TodoList> todoList(Long id);

    @Insert("INSERT INTO car.todoList(id,title,status,uid) VALUES (#{id},#{title},#{status},#{uid})")
    void addTodoList(@Param("id") Long id, @Param("title") String title, @Param("status") Boolean status, @Param("uid") Long uid);

    @Delete("DELETE FROM car.todoList WHERE id=#{id}")
    void deleteTodoList(@Param("id") Long id);

    @Update("UPDATE car.todoList t SET t.title=#{title} WHERE t.id=#{id}")
    void updateTodoList(@Param("title") String title, @Param("id") Long id);

    @Update("UPDATE car.todoList t SET t.status=1 WHERE t.id=#{id}")
    void editTodoList(@Param("id") Long id);

    @Update("UPDATE car.todoList t SET t.status=0 WHERE t.id=#{id}")
    void editTodoListB(@Param("id") Long id);
}
