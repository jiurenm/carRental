package com.edu.car;

import com.edu.car.mapper.CustomerMapper;
import com.edu.car.model.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarApplicationTests {
    @Autowired
    private CustomerMapper customerMapper;

    @Test
    public void contextLoads() {
        List<Customer> customers = customerMapper.showCustomers();
        System.out.println(customers);
    }

}

