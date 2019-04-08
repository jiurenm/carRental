package com.edu.carportal.dto;

import lombok.Data;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/7 18:49
 */
@Data
public class OrderDto {
    private String cid;
    private String getTime;
    private String returnTime;
    private Double price;
}
