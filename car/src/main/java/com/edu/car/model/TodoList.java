package com.edu.car.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/17 9:54
 */
@Getter
@Setter
@NoArgsConstructor
public class TodoList implements Serializable {
    private String id;
    private String title;
    private Boolean status;
    private String uid;
}
