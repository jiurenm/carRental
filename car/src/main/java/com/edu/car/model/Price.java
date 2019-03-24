package com.edu.car.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/3/24 11:52
 */
@Getter
@Setter
@NoArgsConstructor
public class Price implements Serializable {
    private Double shortTime;
    private Double workday;
    private Double week;
    private Double month;
    private Double year;
}
