package com.edu.car.model;

import lombok.Data;

import java.io.Serializable;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/20 20:10
 */
@Data
public class Carousel implements Serializable {
    private String id;
    private String href;
    private String src;
}
