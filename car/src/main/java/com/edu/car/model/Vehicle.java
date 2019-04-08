package com.edu.car.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/6 22:32
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Vehicle implements Serializable {
    private String id;
    private String number;
    private String type;
    private Boolean status;
}
