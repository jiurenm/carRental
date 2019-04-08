package com.edu.car.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 16:22
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Car implements Serializable {
    private String id;
    private String name;
    private String cx;
    private String pp;
    private String cs;
    private String ndk;
    private String pzk;
    private String picture;
    private List<CarDetail> carDetails;
    private List<Picture> pic;
    private List<Price> price;
    private List<Vehicle> vehicles;
}