package com.edu.caradmin.service;

/**
 * AdminService
 *
 * @author Administrator
 * @date 2018/12/29 18:24
 */
public interface AdminService {
    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    String login(String username,String password);

    /**
     * 刷新token
     *
     * @param oldToken token
     * @return newToken
     */
    String refreshToken(String oldToken);
}
