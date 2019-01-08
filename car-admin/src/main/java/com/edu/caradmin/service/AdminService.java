package com.edu.caradmin.service;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 18:24
 */
public interface AdminService {
    String login(String username,String password);
    String refreshToken(String oldToken);
}
