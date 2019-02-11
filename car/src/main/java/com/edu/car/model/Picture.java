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
 * @date 2018/12/29 16:23
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Picture implements Serializable {
    private String pid;
    private String url;
}
