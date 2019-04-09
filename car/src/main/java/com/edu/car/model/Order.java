package com.edu.car.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/7 18:30
 */
@Getter
@Setter
@NoArgsConstructor
public class Order implements Serializable {
    private String id;
    private String getTime;
    private String returnTime;
    private Double price;
    private Integer status;
    private String vid;
    private String uid;
}
