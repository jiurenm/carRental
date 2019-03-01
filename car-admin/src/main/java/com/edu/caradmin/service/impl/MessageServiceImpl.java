package com.edu.caradmin.service.impl;

import com.edu.car.mapper.MessageMapper;
import com.edu.car.model.Message;
import com.edu.caradmin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/2/28 10:09
 */
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public List<Message> showMessage(Long recId) {
        return messageMapper.showMessage(recId);
    }

    @Override
    public List<Message> showSendMessage(Long uid) {
        return messageMapper.showSendMessage(uid);
    }

    @Override
    public int readMessage(Long id) {
        return messageMapper.readMessage(id);
    }

    @Override
    public int deleteMessage(Long id) {
        return messageMapper.deleteMessage(id);
    }

    @Override
    public int revertMessage(Long id) {
        return messageMapper.revertMessage(id);
    }

    @Override
    public int clearMessage(Long recId) {
        return messageMapper.clearMessage(recId);
    }
}
