package com.edu.caradmin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/8 11:23
 */
@Data
public class AuthorityDto {
    @ApiModelProperty(value = "用户id")
    private String uid;
    @ApiModelProperty(value = "权限id")
    private Integer rid;
}
