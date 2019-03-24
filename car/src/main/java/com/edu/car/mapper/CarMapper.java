package com.edu.car.mapper;

import com.edu.car.model.Car;
import com.edu.car.model.CarDetail;
import com.edu.car.model.Picture;
import com.edu.car.model.Price;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 22:15
 */
@Mapper
public interface CarMapper {
    /**
     * 汽车列表
     *
     * @return 汽车信息列表
     */
    @Select("SELECT c.`id`,c.`name`,c.`cx`,c.`pp`,c.`cs`,c.`ndk`,c.`pzk`,c.`picture` FROM car.car c")
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
                    many = @Many(select = "com.edu.car.mapper.CarMapper.showPrice"))
    })
    List<Car> showCar();

    /**
     * 汽车图片
     *
     * @param id 汽车id
     * @return 汽车的图片
     */
    @Select("SELECT p.`id` as pid,p.`url` FROM car.picture p WHERE p.`cid`=#{id}")
    List<Picture> showPic(Long id);

    /**
     * 汽车配置
     *
     * @param id 汽车id
     * @return 汽车配置信息
     */
    @Select("SELECT c.`zws`,c.`cms`,c.`rllx`,c.`bsxlx`,c.`pl`" +
            ",c.`ry`,c.`qdfs`,c.`fdjjqxs`,c.`tc`,c.`yxrl`" +
            ",c.`yx`,c.`zy`,c.`dcld`,c.`qn`,c.`dvd`,c.`gps` " +
            "FROM car.car_detail c " +
            "WHERE c.`id`=#{id}")
    List<CarDetail> showDetails(Long id);

    @Select("SELECT c.`zws`,c.`cms`,c.`rllx`,c.`bsxlx`,c.`pl`" +
            ",c.`ry`,c.`qdfs`,c.`fdjjqxs`,c.`tc`,c.`yxrl`" +
            ",c.`yx`,c.`zy`,c.`dcld`,c.`qn`,c.`dvd`,c.`gps` " +
            "FROM car.car_detail c " +
            "WHERE c.`id`=#{id}")
    CarDetail showDetail(Long id);

    @Select("SELECT p.`shortTime`,p.`workday`,p.`week`,p.`month`,p.`year` FROM car.price p WHERE p.`id`=#{id}")
    Price showPrice(Long id);
}
