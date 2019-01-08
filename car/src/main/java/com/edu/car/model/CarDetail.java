package com.edu.car.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 16:22
 */
@Getter
@Setter
@NoArgsConstructor
public class CarDetail implements Serializable {
    private Integer zws;
    private Integer cms;
    private String rllx;
    private String bsxlx;
    private String pl;
    private String ry;
    private String qdfs;
    private String fdjjqxs;
    private String tc;
    private String yxrl;
    private String yx;
    private String zy;
    private String dcld;
    private String qn;
    private String dvd;
    private String gps;
}
