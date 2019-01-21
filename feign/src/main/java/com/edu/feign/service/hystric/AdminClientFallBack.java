package com.edu.feign.service.hystric;

import com.edu.feign.dto.LoginDto;
import com.edu.feign.dto.Results;
import com.edu.feign.service.AdminClient;
import org.springframework.stereotype.Component;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/1 13:44
 */
@Component
public class AdminClientFallBack implements AdminClient {
    @Override
    public Results login(LoginDto loginDto) {
        return new Results().notfound();
    }

    @Override
    public Results refreshToken() {
        return new Results().notfound();
    }

    @Override
    public Results showCustomers() {
        return new Results().notfound();
    }

    @Override
    public Results findCustomer(String username) {
        return new Results().notfound();
    }

    @Override
    public Results setBlackList(String id) {
        return new Results().notfound();
    }

    @Override
    public Results findList(Integer pageNum, Integer pageSize) {
        return new Results().notfound();
    }
}
