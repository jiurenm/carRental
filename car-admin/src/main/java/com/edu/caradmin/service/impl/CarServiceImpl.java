package com.edu.caradmin.service.impl;

import com.edu.car.mapper.CarMapper;
import com.edu.car.model.Car;
import com.edu.car.model.CarDetail;
import com.edu.car.model.Picture;
import com.edu.car.model.Price;
import com.edu.car.redis.RedisTool;
import com.edu.car.uid.IdWorker;
import com.edu.caradmin.dao.CarTypeMapper;
import com.edu.caradmin.dto.CarTypeDto;
import com.edu.caradmin.service.CarService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * CarServiceImpl
 *
 * @author Administrator
 * @date 2019/1/4 13:13
 */
@Slf4j
@Service
public class CarServiceImpl implements CarService {
    private static final int EXPIRE_TIME = 1000;

    private final Jedis jedis;
    private final CarTypeMapper carTypeMapper;
    private final CarMapper carMapper;

    @Autowired
    public CarServiceImpl(CarMapper carMapper, CarTypeMapper carTypeMapper, Jedis jedis) {
        this.carMapper = carMapper;
        this.carTypeMapper = carTypeMapper;
        this.jedis = jedis;
    }

    @Override
    public Car findTypeById(Long id) {
        return carTypeMapper.findTypeById(id);
    }

    @Override
//    @Cacheable(value = "cars")
    public List<Car> showCar() {
        return carMapper.showCar( );
    }

    @Override
    public CarDetail showDetail(Long id) {
        return carMapper.showDetail(id);
    }

    @Override
    public int addType(CarTypeDto carTypeDto) {
        String lockKey = "addType_key";
        String required = UUID.randomUUID().toString();
        CarDetail carDetails = carTypeDto.getCarDetail().get(0);
        List<Picture> pictures = carTypeDto.getPictures();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, required, EXPIRE_TIME)) {
            throw new RuntimeException("操作太快");
        } else {
            int result = 0;
            try {
                Long id = IdWorker.getId();
                result = carTypeMapper.addType(id, carTypeDto.getName(), carTypeDto.getPp(),
                        carTypeDto.getNdk(), carTypeDto.getPzk(), carTypeDto.getCs(), carTypeDto.getPicture(),
                        carTypeDto.getCx());
                carTypeMapper.addDetail(id, carDetails.getZws(), carDetails.getCms(), carDetails.getRllx(),
                        carDetails.getBsxlx(), carDetails.getPl(), carDetails.getRy(), carDetails.getQdfs(),
                        carDetails.getFdjjqxs(), carDetails.getTc(), carDetails.getYxrl(), carDetails.getYx(),
                        carDetails.getZy(), carDetails.getDcld(), carDetails.getQn(), carDetails.getDvd(),
                        carDetails.getGps());
                for (int i=0;i<pictures.size();i++) {
                    Long pid = IdWorker.getId();
                    carTypeMapper.addPic(pid, pictures.get(i).getUrl(), id, i);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            if (RedisTool.releaseDistributedLock(jedis, lockKey, required)) {
                return result;
            } else {
                throw new RuntimeException("释放锁失败");
            }
        }
    }

    @Override
    public int deleteType(Long id) {
        String lockKey = "deleteType_key";
        String requireId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requireId, EXPIRE_TIME)) {
            throw new RuntimeException("操作太快");
        } else {
            int result = 0;
            try {
                result = carTypeMapper.deleteType(id);
                carTypeMapper.deleteDetail(id);
                carTypeMapper.deletePic(id);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            if (RedisTool.releaseDistributedLock(jedis, lockKey, requireId)) {
                return result;
            } else {
                throw new RuntimeException("释放锁失败");
            }
        }
    }

    @Override
    public int updateType(CarTypeDto carTypeDto) {
        String lockKey = "updateType_key";
        String required = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, required, EXPIRE_TIME)) {
            throw new RuntimeException("操作太快");
        } else {
            CarDetail detail = carTypeDto.getCarDetail().get(0);
            Price price = carTypeDto.getPrices().get(0);
            Set<Picture> pictures = Sets.newConcurrentHashSet(carTypeDto.getPictures());
            int result = 0;
            try {
                result = carTypeMapper.updateType(Long.valueOf(carTypeDto.getId()), carTypeDto.getName(), carTypeDto.getPp(),
                        carTypeDto.getNdk(), carTypeDto.getPzk(), carTypeDto.getCs(), carTypeDto.getCx());
                carTypeMapper.updateDetail(Long.valueOf(carTypeDto.getId()), detail.getZws(), detail.getCms(), detail.getRllx(),
                        detail.getBsxlx(), detail.getPl(), detail.getRy(), detail.getQdfs(), detail.getFdjjqxs(), detail.getTc(),
                        detail.getYxrl(), detail.getYx(), detail.getZy(), detail.getDcld(), detail.getQn(), detail.getDvd(),
                        detail.getGps());
                Set<Picture> originPic = Sets.newConcurrentHashSet(carMapper.showPic(Long.valueOf(carTypeDto.getId())));
                if (originPic.size() > pictures.size()) {
                    ImmutableList<Picture> difference = Sets.difference(originPic, pictures).immutableCopy().asList();
                    //delete
                    difference.forEach(picture -> carTypeMapper.deletePic(Long.valueOf(picture.getPid())));
                } else if (originPic.size() < pictures.size()) {
                    ImmutableList<Picture> difference = Sets.difference(pictures, originPic).immutableCopy().asList();
                    log.info("add" + difference);
                    //add
                    difference.forEach(picture -> carTypeMapper.addPic(IdWorker.getId(), picture.getUrl(), Long.valueOf(carTypeDto.getId()), originPic.size()+1));
                }
                carTypeMapper.updatePrice(Long.valueOf(carTypeDto.getId()), price.getShortTime(), price.getWorkday(),
                        price.getWeek(), price.getMonth(), price.getYear());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            if (RedisTool.releaseDistributedLock(jedis, lockKey, required)) {
                return result;
            } else {
                throw new RuntimeException("释放锁失败");
            }
        }
    }

    @Override
    public Car carDetails(String id) {
        return carMapper.carDetails(Long.valueOf(id));
    }
}
