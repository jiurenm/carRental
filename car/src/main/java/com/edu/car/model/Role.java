package com.edu.car.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 16:24
 */
@Getter
@Setter
@NoArgsConstructor
public class Role implements Serializable {
    private Long id;
    private String name;
}
