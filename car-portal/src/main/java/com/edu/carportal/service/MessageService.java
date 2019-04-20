package com.edu.carportal.service;

import com.edu.car.model.Message;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/20 16:42
 */
public interface MessageService {
    List<Message> select(String id);
    int read(String id);
    int recycle(String id);
    int revert(String id);
    int delete(String id);
}
