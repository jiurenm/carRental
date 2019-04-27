package com.edu.carportal.controller;

import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.car.util.JwtTokenUtil;
import com.edu.carportal.dto.MessageDto;
import com.edu.carportal.service.LoginService;
import com.edu.carportal.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/20 16:41
 */
@RestController
@RequestMapping(value = "/message")
public class MessageController {
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    private final LoginService loginService;
    private final JwtTokenUtil jwtTokenUtil;
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService, JwtTokenUtil jwtTokenUtil, LoginService loginService) {
        this.messageService = messageService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.loginService = loginService;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public Results show(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader(this.tokenHeader).substring(this.tokenHead.length()));
        Customer customer = loginService.findCustomerByName(username);
        return new Results().success(messageService.select(customer.getId()));
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public Results read(@PathVariable String id) {
        return new Results().success(messageService.read(id));
    }

    @RequestMapping(value = "/recycle/{id}", method = RequestMethod.GET)
    public Results recycle(@PathVariable String id) {
        return new Results().success(messageService.recycle(id));
    }

    @RequestMapping(value = "/revert/{id}", method = RequestMethod.GET)
    public Results revert(@PathVariable String id) {
        return new Results().success(messageService.revert(id));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Results delete(@PathVariable String id) {
        return new Results().success(messageService.delete(id));
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public Results send(@RequestBody MessageDto messageDto) {
        return new Results().success(messageService.send(messageDto));
    }
}
