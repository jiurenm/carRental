package com.edu.car.model;

import lombok.*;

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
@EqualsAndHashCode
public class Role implements Serializable {
    private String id;
    private Integer rid;
    private String name;
}
