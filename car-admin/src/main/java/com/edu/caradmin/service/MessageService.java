package com.edu.caradmin.service;

import com.edu.car.model.Message;
import com.edu.caradmin.dto.MessageDto;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/2/28 10:07
 */
public interface MessageService {
    List<Message> showMessage(Long recId);
    List<Message> showSendMessage(Long uid);
    int readMessage(Long id);
    int deleteMessage(Long id);
    int revertMessage(Long id);
    int clearMessage(Long recId);
    void send(MessageDto messageDto);
}
