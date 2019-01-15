package com.edu.caradmin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/15 15:29
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerDto {
    @NotNull(message = "id不能为空")
    @NotEmpty(message = "id不能为空")
    private String id;
    private String name;
    private Integer age;
    private String tel;
    private String email;
    private String driveNum;
    private String driveType;
}
