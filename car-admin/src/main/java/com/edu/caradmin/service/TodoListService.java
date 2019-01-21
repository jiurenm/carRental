package com.edu.caradmin.service;

import com.edu.car.model.TodoList;
import com.edu.caradmin.dto.TodoListDto;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/17 10:20
 */
public interface TodoListService {
    List<TodoList> todoList(Long uid);
    int addTodoList(TodoListDto todoListDto);
    int deleteTodoList(Long id);
    int updateTodoList(TodoListDto todoListDto);
    int editTodoList(Long id);
    int editTodoListB(Long id);
}
