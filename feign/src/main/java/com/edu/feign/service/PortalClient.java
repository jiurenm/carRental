package com.edu.feign.service;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * carRental
 *
 * @author Administrator
 * @date 2019/3/8 17:21
 */
@FeignClient(value = "CAR-PORTAL")
public interface PortalClient {
}
