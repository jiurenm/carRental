package com.edu.caradmin.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.car.model.TodoList;
import com.edu.car.uid.IdWorker;
import com.edu.car.util.JwtTokenUtil;
import com.edu.caradmin.dto.TodoListDto;
import com.edu.caradmin.service.CustomerService;
import com.edu.caradmin.service.TodoListService;
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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Results addTodoList(@RequestBody TodoListDto todoListDto, HttpServletRequest request) {
        Customer customer = customerService.findCustomerByName(jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length())));
        todoListDto.setUid(customer.getId());
        todoListDto.setId(String.valueOf(IdWorker.getId()));
        todoListDto.setStatus(false);
        try {
            todoListService.addTodoList(todoListDto);
            return new Results().success("");
        } catch (Exception e) {
            return new Results().failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/edit/{id}/{status}", method = RequestMethod.GET)
    public Results editTodoList(@PathVariable Long id, @PathVariable String status) {
        if (id == null || status == null) {
            return new Results().validateFailed("id不能为空");
        }
        if (TRUE.equals(status)) {
            try {
                todoListService.editTodoList(id);
                return new Results().success("");
            } catch (Exception e) {
                return new Results( ).failed(e.getMessage( ));
            }
        } else {
            try {
                todoListService.editTodoListB(id);
                return new Results().success("");
            } catch (Exception e) {
                return new Results( ).failed(e.getMessage( ));
            }
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Results deleteTodoList(@PathVariable Long id) {
        if (id == null) {
            return new Results().validateFailed("id不能为空");
        }
        try {
            todoListService.deleteTodoList(id);
            return new Results().success("删除成功");
        } catch (Exception e) {
            return new Results().failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/update")
    public Results updateTodoList(@RequestBody TodoListDto todoListDto, HttpServletRequest request) {
        Customer customer = customerService.findCustomerByName(jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length())));
        todoListDto.setUid(customer.getId());
        try {
            todoListService.updateTodoList(todoListDto);
            return new Results().success("更新成功");
        } catch (Exception e) {
            return new Results().failed(e);
        }
    }
}
