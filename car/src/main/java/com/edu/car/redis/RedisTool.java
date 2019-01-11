package com.edu.car.redis;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * 分布式redis锁
 *
 * @author Administrator
 * @date 2019/1/4 14:09
 */
public class RedisTool {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIT = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 加锁
     * @param jedis redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超时时间
     * @return 是否成功
     */
    public synchronized static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey,requestId,SET_IF_NOT_EXIT,SET_WITH_EXPIRE_TIME,expireTime);
        return !LOCK_SUCCESS.equals(result);
    }

    /**
     * 释放锁
     * @param jedis redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public synchronized static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(result);
    }
}
