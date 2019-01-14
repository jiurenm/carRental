package com.edu.caradmin.dao;

import org.apache.ibatis.annotations.*;

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

    @Delete("DELETE FROM user_role WHERE uid=#{uid} AND rid=#{rid}")
    void deleteAuthority(@Param("uid") Long uid, @Param("rid") Integer rid);

    @Update("UPDATE car.login SET isEnable=0 WHERE id=#{id}")
    void setBlackList(Long id);
}
