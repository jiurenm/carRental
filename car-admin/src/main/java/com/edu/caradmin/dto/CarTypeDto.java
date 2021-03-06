package com.edu.caradmin.dto;

import com.edu.car.model.CarDetail;
import com.edu.car.model.Picture;
import com.edu.car.model.Price;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * CarTypeDto
 *
 * @author Administrator
 * @date 2019/1/22 17:10
 */
@Data
public class CarTypeDto {
    @NotEmpty(message = "id不能为空")
    private String id;
    private String name;
    private String cx;
    private String pp;
    private String cs;
    private String ndk;
    private String pzk;
    private String picture;
    private List<CarDetail> carDetail;
    private List<Picture> pictures;
    private List<Price> prices;
}
