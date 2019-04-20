package com.edu.caradmin.service.impl;

import com.edu.car.mapper.CustomerMapper;
import com.edu.car.mapper.MessageMapper;
import com.edu.car.model.Customer;
import com.edu.car.model.Message;
import com.edu.car.uid.IdWorker;
import com.edu.caradmin.dto.MessageDto;
import com.edu.caradmin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/2/28 10:09
 */
@Service
public class MessageServiceImpl implements MessageService {
    private final CustomerMapper customerMapper;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper, CustomerMapper customerMapper) {
        this.messageMapper = messageMapper;
        this.customerMapper = customerMapper;
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

    @Override
    public void send(MessageDto messageDto) {
        Long id = IdWorker.getId();
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        String date = today.toString() + " " + time.toString();
        messageMapper.send(id, messageDto.getTitle(), messageDto.getMessage());
        List<Customer> customers = customerMapper.showCustomers();
        for (Customer customer:customers) {
            messageMapper.send1(date, Long.valueOf(customer.getId()), id);
        }
    }
}
