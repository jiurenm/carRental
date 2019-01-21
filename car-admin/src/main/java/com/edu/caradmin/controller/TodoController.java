package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.car.model.TodoList;
import com.edu.car.uid.IdWorker;
import com.edu.car.util.JwtTokenUtil;
import com.edu.caradmin.dto.TodoListDto;
import com.edu.caradmin.service.CustomerService;
import com.edu.caradmin.service.TodoListService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * 待办事项
 *
 * @author Administrator
 * @date 2019/1/17 10:29
 */
@Api(value = "TodoController", description = "待办事项")
@RestController
@RequestMapping(value = "/todoList")
public class TodoController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private static final String TRUE = "true";

    private final CustomerService customerService;
    private final JwtTokenUtil jwtTokenUtil;
    private final TodoListService todoListService;

    @Autowired
    public TodoController(TodoListService todoListService, JwtTokenUtil jwtTokenUtil, CustomerService customerService) {
        this.todoListService = todoListService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.customerService = customerService;
    }

    @ApiOperation(value = "获取待办事项")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Results todoList(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length()));
        Customer customer = customerService.findCustomerByName(username);
        List<TodoList> todoList = todoListService.todoList(Long.valueOf(customer.getId()));
        if (todoList.isEmpty()) {
            return new Results().success("没有记录");
        }
        Collections.reverse(todoList);
        return new Results().pageSuccess(todoList);
    }

    @ApiOperation(value = "添加待办事项")
    @ApiParam(name = "todoListDto", value = "待办事项", required = true, type = "TodoListDto")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Results addTodoList(@RequestBody TodoListDto todoListDto, HttpServletRequest request) {
        Customer customer = customerService.findCustomerByName(jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length())));
        todoListDto.setUid(customer.getId());
        todoListDto.setId(String.valueOf(IdWorker.getId()));
        todoListDto.setStatus(false);
        int result = todoListService.addTodoList(todoListDto);
        if (result == 1) {
            return new Results().success(result);
        } else {
            return new Results().failed();
        }
    }

    @ApiOperation(value = "编辑待办事项")
    @RequestMapping(value = "/edit/{id}/{status}", method = RequestMethod.GET)
    public Results editTodoList(@PathVariable Long id, @PathVariable String status) {
        if (id == null || status == null) {
            return new Results().validateFailed("id不能为空");
        }
        if (TRUE.equals(status)) {
            int result = todoListService.editTodoList(id);
            if (result == 1) {
                return new Results().success(result);
            } else {
                return new Results().failed();
            }
        } else {
            int result = todoListService.editTodoListB(id);
            if (result == 1) {
                return new Results().success(result);
            } else {
                return new Results().failed();
            }
        }
    }

    @ApiOperation(value = "删除待办事项")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Results deleteTodoList(@PathVariable Long id) {
        if (id == null) {
            return new Results().validateFailed("id不能为空");
        }
        int result = todoListService.deleteTodoList(id);
        if (result == 1) {
            return new Results().success(result);
        } else {
            return new Results().failed();
        }
    }

    @ApiOperation(value = "更新待办事项")
    @RequestMapping(value = "/update")
    public Results updateTodoList(@RequestBody TodoListDto todoListDto, HttpServletRequest request) {
        Customer customer = customerService.findCustomerByName(jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length())));
        todoListDto.setUid(customer.getId());
        int result = todoListService.updateTodoList(todoListDto);
        if (result == 1) {
            return new Results().success(result);
        } else {
            return new Results().failed();
        }
    }
}
