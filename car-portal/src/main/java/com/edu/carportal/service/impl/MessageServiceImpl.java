package com.edu.carportal.service.impl;

import com.edu.car.mapper.MessageMapper;
import com.edu.car.model.Message;
import com.edu.carportal.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/20 16:43
 */
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public List<Message> select(String id) {
        return messageMapper.showMessage(Long.valueOf(id));
    }

    @Override
    public int read(String id) {
        return messageMapper.readMessage(Long.valueOf(id));
    }

    @Override
    public int recycle(String id) {
        return messageMapper.deleteMessage(Long.valueOf(id));
    }

    @Override
    public int revert(String id) {
        return messageMapper.revertMessage(Long.valueOf(id));
    }

    @Override
    public int delete(String id) {
        return messageMapper.clearMessage(Long.valueOf(id));
    }
}
