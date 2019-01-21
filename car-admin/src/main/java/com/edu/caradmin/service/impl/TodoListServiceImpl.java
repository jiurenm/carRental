package com.edu.caradmin.service.impl;

import com.edu.car.mapper.TodoMapper;
import com.edu.car.model.TodoList;
import com.edu.caradmin.dto.TodoListDto;
import com.edu.caradmin.service.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/17 10:23
 */
@Service
public class TodoListServiceImpl implements TodoListService {
    private final TodoMapper todoMapper;

    @Autowired
    public TodoListServiceImpl(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    @Override
    public List<TodoList> todoList(Long uid) {
        return todoMapper.todoList(uid);
    }

    @Override
    public int addTodoList(TodoListDto todoListDto) {
        return todoMapper.addTodoList(Long.valueOf(todoListDto.getId()), todoListDto.getTitle(),
                todoListDto.getStatus(), Long.valueOf(todoListDto.getUid()));

    }

    @Override
    public int deleteTodoList(Long id) {
        return todoMapper.deleteTodoList(id);
    }

    @Override
    public int updateTodoList(TodoListDto todoListDto) {
        return todoMapper.updateTodoList(todoListDto.getTitle(), Long.valueOf(todoListDto.getId()));
    }

    @Override
    public int editTodoList(Long id) {
        return todoMapper.editTodoList(id);
    }

    @Override
    public int editTodoListB(Long id) {
        return todoMapper.editTodoListB(id);
    }
}
