package com.edu.car.model;

import lombok.Data;

import java.io.Serializable;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/2/28 9:33
 */
@Data
public class Message implements Serializable {
    private Long id;
    private String title;
    private String date;
    private String message;
    private Long uid;
    private Long recId;
    private Integer status;
}
