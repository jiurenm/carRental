package com.edu.caradmin.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/8 11:11
 */
@Mapper
public interface AdminMapper {
    @Insert("INSERT INTO user_role(id,uid,rid) VALUES (#{id},#{uid},#{rid})")
    void addAuthority(Long id, Long uid, Integer rid);

    @Update("UPDATE car.login SET isEnable=0 WHERE id=#{id}")
    void setBlackList(Long id);
}
