package com.edu.car.mapper;

import com.edu.car.model.Vehicle;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/4/6 22:34
 */
@Mapper
public interface VehicleMapper {
    @Select("SELECT * FROM vehicle")
    List<Vehicle> showVehicle();

    @Select("SELECT * FROM vehicle WHERE `type`=#{id} AND `status`=0")
    List<Vehicle> findVehicle(@Param("id") Long id);

    @Select("SELECT * FROM vehicle WHERE id=#{id}")
    Vehicle findOne(@Param("id") Long id);

    @Delete("DELETE FROM vehicle WHERE id=#{id}")
    int delete(@Param("id") Long id);

    @Insert("INSERT INTO vehicle(`id`, `number`, `type`) VALUES (#{id}, #{number}, #{type})")
    int insert(@Param("id") Long id, @Param("number") String number, @Param("type") Long type);

    @Update("UPDATE vehicle SET number=#{number},type=#{type} WHERE id=#{id}")
    int update(@Param("id") Long id, @Param("number") String number, @Param("type") Long type);

    @Update("UPDATE vehicle SET status=1 WHERE id=#{id}")
    int rental(@Param("id") Long id);
}
