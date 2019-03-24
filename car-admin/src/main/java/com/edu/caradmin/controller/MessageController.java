package com.edu.caradmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.edu.car.dto.Results;
import com.edu.car.model.Customer;
import com.edu.car.model.Message;
import com.edu.car.util.JwtTokenUtil;
import com.edu.caradmin.service.CustomerService;
import com.edu.caradmin.service.MessageService;
import com.google.common.collect.Lists;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/2/28 10:12
 */
@RestController
@RequestMapping(value = "/message")
public class MessageController {
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    private final CustomerService customerService;
    private final JwtTokenUtil jwtTokenUtil;
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService, JwtTokenUtil jwtTokenUtil, CustomerService customerService) {
        this.messageService = messageService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.customerService = customerService;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public Results showMessage(HttpServletRequest request) {
        List<Message> messages = messageService.showMessage(getId(request));
        if (!messages.isEmpty()) {
            return new Results().pageSuccess(messages);
        } else {
            return new Results().failed("没有记录");
        }
    }

    @RequestMapping(value = "/showUnread", method = RequestMethod.GET)
    public Results showUnreadMessage(HttpServletRequest request) {
        List<Message> messages = messageService.showMessage(getId(request));
        Long count = messages.stream().filter(message -> message.getStatus() == 0).count();
        return new Results().success(count);
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public Results read(@PathVariable String id) {
        int result = messageService.readMessage(Long.valueOf(id));
        return new Results().success(result);
    }

    @RequestMapping(value = "/readAll", method = RequestMethod.POST)
    public Results readAll(@RequestBody String message) {
        List<Message> messages = JSONObject.parseArray(message, Message.class);
        List<Results> list = Lists.newArrayList();
        Observable.create((ObservableOnSubscribe<Long>) emitter -> {
            messages.forEach(e -> emitter.onNext(e.getId()));
            emitter.onComplete();
        }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(Long aLong) {
                messageService.readMessage(aLong);
            }

            @Override
            public void onError(Throwable throwable) {
                list.add(new Results().failed());
            }

            @Override
            public void onComplete() {
                list.add(new Results().success("成功"));
            }
        });
        return list.get(0);
    }

    @RequestMapping(value = "/delAll", method = RequestMethod.POST)
    public Results delAll(@RequestBody String message) {
        List<Message> messages = JSONObject.parseArray(message, Message.class);
        List<Results> list = Lists.newArrayList();
        Observable.create((ObservableOnSubscribe<Long>) emitter -> {
            messages.forEach(e -> emitter.onNext(e.getId()));
            emitter.onComplete();
        }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Long aLong) {
                messageService.deleteMessage(aLong);
            }

            @Override
            public void onError(Throwable throwable) {
                list.add(new Results().failed());
            }

            @Override
            public void onComplete() {
                list.add(new Results().success("成功"));
            }
        });
        return list.get(0);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Results delete(@PathVariable String id) {
        int result = messageService.deleteMessage(Long.valueOf(id));
        return new Results().success(result);
    }

    @RequestMapping(value = "/revert/{id}", method = RequestMethod.GET)
    public Results revert(@PathVariable String id) {
        int result = messageService.revertMessage(Long.valueOf(id));
        return new Results().success(result);
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public Results clear(@RequestBody String message) {
        List<Message> messages = JSONObject.parseArray(message, Message.class);
        List<Results> list = Lists.newArrayList();
        Observable.create((ObservableOnSubscribe<Long>) emitter -> {
            messages.forEach(e -> emitter.onNext(e.getId()));
            emitter.onComplete();
        }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Long aLong) {
                messageService.clearMessage(aLong);
            }

            @Override
            public void onError(Throwable throwable) {
                list.add(new Results().failed());
            }

            @Override
            public void onComplete() {
                list.add(new Results().success("成功"));
            }
        });
        return list.get(0);
    }

    private Long getId(HttpServletRequest request) {
        String authHeader = request.getHeader(this.tokenHeader);
        String authToken = authHeader.substring(this.tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        Customer customer = customerService.findCustomerByName(username);
        return Long.valueOf(customer.getId());
    }
}
