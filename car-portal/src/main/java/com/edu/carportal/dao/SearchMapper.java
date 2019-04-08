package com.edu.carportal.dao;

import com.edu.car.model.Car;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/3/24 17:03
 */
@Mapper
public interface SearchMapper {
    @Select("SELECT c.cx FROM car.car c")
    List<Car> findAllCX();

    @Select("SELECT c.pp FROM car.car c")
    List<Car> findAllPP();

    @Select("SELECT c.`id`,c.`name`,c.`cx`,c.`pp`,c.`cs`,c.`ndk`,c.`pzk`,c.`picture` FROM car.car c WHERE c.`cx`=#{cx}")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "cx",column = "cx"),
            @Result(property = "pp",column = "pp"),
            @Result(property = "cs",column = "cs"),
            @Result(property = "ndk",column = "ndk"),
            @Result(property = "pzk",column = "pzk"),
            @Result(property = "picture",column = "picture"),
            @Result(property = "carDetails",column = "id",javaType = List.class,
                    many = @Many(select = "com.edu.car.mapper.CarMapper.showDetails")),
            @Result(property = "pic",column = "id",javaType = List.class,
                    many = @Many(select = "com.edu.car.mapper.CarMapper.showPic")),
            @Result(property = "price",column = "id",javaType = List.class,
                    many = @Many(select = "com.edu.car.mapper.CarMapper.showPrice")),
            @Result(property = "vehicles",column = "id",javaType = List.class,
                    many = @Many(select = "com.edu.car.mapper.CarMapper.showVehicle"))
    })
    List<Car> searchByCx(String cx);

    @Select("SELECT c.`id`,c.`name`,c.`cx`,c.`pp`,c.`cs`,c.`ndk`,c.`pzk`,c.`picture` FROM car.car c WHERE c.`pp`=#{pp}")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "cx",column = "cx"),
            @Result(property = "pp",column = "pp"),
            @Result(property = "cs",column = "cs"),
            @Result(property = "ndk",column = "ndk"),
            @Result(property = "pzk",column = "pzk"),
            @Result(property = "picture",column = "picture"),
            @Result(property = "carDetails",column = "id",javaType = List.class,
                    many = @Many(select = "com.edu.car.mapper.CarMapper.showDetails")),
            @Result(property = "pic",column = "id",javaType = List.class,
                    many = @Many(select = "com.edu.car.mapper.CarMapper.showPic")),
            @Result(property = "price",column = "id",javaType = List.class,
                    many = @Many(select = "com.edu.car.mapper.CarMapper.showPrice")),
            @Result(property = "vehicles",column = "id",javaType = List.class,
                    many = @Many(select = "com.edu.car.mapper.CarMapper.showVehicle"))
    })
    List<Car> searchByPp(String pp);
}
