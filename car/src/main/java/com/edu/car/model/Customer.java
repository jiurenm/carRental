package com.edu.car.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 16:23
 */
@Getter
@Setter
@NoArgsConstructor
public class Customer implements Serializable {
    private Long id;
    private String username;
    private String name;
    private Integer age;
    private String tel;
    private String email;
    private String driveNum;
    private String driveType;
    private String password;
    private Boolean isEnable;
}
