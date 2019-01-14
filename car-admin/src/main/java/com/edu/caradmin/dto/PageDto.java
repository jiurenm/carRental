package com.edu.caradmin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/13 18:32
 */
@Data
public class PageDto {
    @ApiModelProperty(value = "页码")
    @NotNull(message = "页码不能为空")
    @DecimalMin(value = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页数据量")
    @NotNull(message = "每页数据量不能为空")
    @DecimalMin(value = "1")
    private Integer pageSize;
}
