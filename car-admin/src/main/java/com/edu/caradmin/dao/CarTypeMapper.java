package com.edu.caradmin.dao;

import com.edu.car.model.Car;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * CarTypeMapper
 *
 * @author Administrator
 * @date 2019/1/22 17:15
 */
@Mapper
public interface CarTypeMapper {
    @Select("SELECT c.`id`,c.`name`,c.`cx`,c.`pp`,c.`cs`,c.`ndk`,c.`pzk`,c.`picture` FROM car.car c WHERE id=#{id}")
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
    Car findTypeById(Long id);

    @Insert("INSERT INTO car.car(id,name,cx,pp,ndk,pzk,cs,picture) VALUES " +
            "(#{id},#{name},#{cx},#{pp},#{ndk},#{pzk},#{cs},#{picture})")
    int addType(@Param("id") Long id, @Param("name") String name, @Param("pp") String pp,
                @Param("ndk") String ndk, @Param("pzk") String pzk, @Param("cs") String cs,
                @Param("picture") String picture, @Param("cx") String cx);

    @Insert("INSERT INTO car.car_detail(id,zws,cms,rllx,bsxlx,pl,ry,qdfs,fdjjqxs,tc,yxrl,yx,zy,dcld,qn,dvd,gps) " +
            "VALUES (#{id},#{zws},#{cms},#{rllx},#{bsxlx},#{pl},#{ry},#{qdfs},#{fdjjqxs},#{tc},#{yxrl}," +
            "#{yx},#{zy},#{dcld},#{qn},#{dvd},#{gps})")
    void addDetail(@Param("id") Long id, @Param("zws") Integer zws, @Param("cms") Integer cms, @Param("rllx") String rllx,
                   @Param("bsxlx") String bsxlx, @Param("pl") String pl, @Param("ry") String ry,
                   @Param("qdfs") String qdfs, @Param("fdjjqxs") String fdjjqxs, @Param("tc") String tc,
                   @Param("yxrl") String yxrl, @Param("yx") String yx, @Param("zy") String zy,
                   @Param("dcld") String dcld, @Param("qn") String qn, @Param("dvd") String dvd, @Param("gps") String gps);

    @Delete("DELETE FROM car.car WHERE id=#{id}")
    int deleteType(@Param("id") Long id);

    @Delete("DELETE FROM car.car_detail WHERE id=#{id}")
    void deleteDetail(@Param("id") Long id);

    @Delete("DELETE FROM car.picture WHERE id=#{id}")
    void deletePic(@Param("id") Long id);

    @Update("UPDATE car.car c SET c.name=#{name},c.cx=#{cx},c.pp=#{pp},c.ndk=#{ndk},c.pzk=#{pzk}," +
            "c.cs=#{cs} WHERE c.id=#{id}")
    int updateType(@Param("id") Long id, @Param("name") String name, @Param("pp") String pp,
                    @Param("ndk") String ndk, @Param("pzk") String pzk, @Param("cs") String cs
            , @Param("cx") String cx);

    @Update("UPDATE car.car_detail d SET d.zws=#{zws},d.cms=#{cms},d.rllx=#{rllx},d.bsxlx=#{bsxlx}," +
            " d.pl=#{pl},d.ry=#{ry},d.qdfs=#{qdfs},d.fdjjqxs=#{fdjjqxs},d.tc=#{tc},d.yxrl=#{yxrl}," +
            " d.yx=#{yx},d.zy=#{zy},d.dcld=#{dcld},d.qn=#{qn},d.dvd=#{dvd},d.gps=#{gps} WHERE d.id=#{id}")
    void updateDetail(@Param("id") Long id, @Param("zws") Integer zws, @Param("cms") Integer cms, @Param("rllx") String rllx,
                      @Param("bsxlx") String bsxlx, @Param("pl") String pl, @Param("ry") String ry,
                      @Param("qdfs") String qdfs, @Param("fdjjqxs") String fdjjqxs, @Param("tc") String tc,
                      @Param("yxrl") String yxrl, @Param("yx") String yx, @Param("zy") String zy,
                      @Param("dcld") String dcld, @Param("qn") String qn, @Param("dvd") String dvd, @Param("gps") String gps);

    @Insert("INSERT INTO car.picture(id,url,cid,pid) VALUES (#{id},#{url},#{cid},#{pid})")
    void addPic(@Param("id") Long id, @Param("url") String url, @Param("cid") Long cid, @Param("pid") Integer pid);

    @Update("UPDATE car.price p SET p.shortTime=#{shortTime}, p.workday=#{workday}, p.week=#{week}, p.month" +
            "=#{month}, p.year=#{year} WHERE id=#{id}")
    void updatePrice(@Param("id") Long id, @Param("shortTime") Double shortTime, @Param("workday") Double workday,
                     @Param("week") Double week, @Param("month") Double month, @Param("year") Double year);

    @Delete("DELETE FROM car.picture WHERE id=#{id}")
    void deletePicById(@Param("id") Long id);
}
