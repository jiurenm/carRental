package com.edu.car.util;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * JWT工具类
 *
 * @author Administrator
 * @date 2018/12/29 16:30
 */
@Slf4j
@Component
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME ="sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成Token
     *
     */
    private String generateToken(Map<String,Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 获取JWT负载
     *
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("JWT格式验证失败：{}",token);
            return null;
        }
    }

    /**
     * 生成过期时间
     *
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     *
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null ? claims.getSubject() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证token是否有效
     *
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    /**
     * 判断是否过期
     *
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return !Objects.requireNonNull(expiredDate).before(new Date());
    }

    /**
     * 获取过期时间
     *
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    /**
     * 生成token
     *
     */
    public String generateToken(UserDetails userDetails) {
        Map<String,Object> claims = Maps.newHashMap();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断是否可以被刷新
     *
     */
    public boolean canRefresh(String token) {
        return isTokenExpired(token);
    }

    /**
     * 刷新token
     *
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Objects.requireNonNull(claims).put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
