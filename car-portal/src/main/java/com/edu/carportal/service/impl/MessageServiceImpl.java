package com.edu.carportal.service.impl;

import com.edu.car.mapper.MessageMapper;
import com.edu.car.model.Message;
import com.edu.car.uid.IdWorker;
import com.edu.carportal.dao.MessageDao;
import com.edu.carportal.dto.MessageDto;
import com.edu.carportal.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/20 16:43
 */
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageDao mapper;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper, MessageDao mapper) {
        this.messageMapper = messageMapper;
        this.mapper = mapper;
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

    @Override
    public int send(MessageDto messageDto) {
        Long id = IdWorker.getId();
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        String date = today.toString() + " " + time.toString();
        mapper.send(id, messageDto.getTitle(), messageDto.getContext());
        return mapper.send2(date, messageDto.getEmail(), id);
    }
}
