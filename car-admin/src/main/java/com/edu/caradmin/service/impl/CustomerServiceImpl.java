package com.edu.caradmin.service.impl;

import com.edu.car.mapper.CustomerMapper;
import com.edu.car.model.Customer;
import com.edu.car.model.Role;
import com.edu.car.redis.RedisTool;
import com.edu.car.uid.IdWorker;
import com.edu.caradmin.dao.AdminMapper;
import com.edu.caradmin.dto.AuthorityDto;
import com.edu.caradmin.dto.CustomerDto;
import com.edu.caradmin.service.CustomerService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CustomerServiceImpl
 *
 * @author Administrator
 * @date 2019/1/3 10:55
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private static final int EXPIRE_TIME = 1000;

    private final Jedis jedis;
    private final CustomerMapper customerMapper;
    private final AdminMapper adminMapper;

    @Autowired
    public CustomerServiceImpl(CustomerMapper customerMapper, AdminMapper adminMapper, Jedis jedis) {
        this.customerMapper = customerMapper;
        this.adminMapper = adminMapper;
        this.jedis = jedis;
    }

    @Override
    public List<Customer> showCustomers() {
        return customerMapper.showCustomers();
    }

    @Override
    public Customer findCustomerByName(String username) {
        return customerMapper.findCustomer(username);
    }

    @Override
    public Customer findCustomerById(Long id) {
        return customerMapper.findCustomerById(id);
    }

    @Override
    public int setBlackList(Long id) {
        String lockKey = "blackList_key";
        String requestId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requestId, EXPIRE_TIME)) {
            throw new RuntimeException("操作太快");
        } else {
            int result = 0;
            try {
                result = adminMapper.setBlackList(id);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            if (RedisTool.releaseDistributedLock(jedis, lockKey, requestId)) {
                return result;
            } else {
                log.error("释放锁失败:" + lockKey + "," + requestId);
                throw new RuntimeException("释放锁失败");
            }
        }
    }

    @Override
    public int addAuthority(List<AuthorityDto> authorityDto) {
        String lockKey = "addAuthority_key";
        String requestId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requestId, EXPIRE_TIME)) {
            throw new RuntimeException("操作太快");
        } else {
            Long id = IdWorker.getId();
            try {
                authorityDto.forEach(authority -> {
                    Role role = this.findRoleById(Long.valueOf(authority.getUid( )), authority.getRid( ));
                    if (role != null) {
                        return;
                    }
                    adminMapper.addAuthority(id, Long.valueOf(authority.getUid( )), authority.getRid( ));
                });
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            if (RedisTool.releaseDistributedLock(jedis, lockKey, requestId)) {
                return 1;
            } else {
                log.error("释放锁失败:" + lockKey + "," + requestId);
                throw new RuntimeException("释放锁失败");
            }
        }
    }

    @Override
    public Role showRolesById(Integer id) {
        return customerMapper.findRolesById(id);
    }

    @Override
    public Role findRoleById(Long uid, Integer id) {
        return customerMapper.findRoleById(uid, id);
    }

    @Override
    public int deleteAuthority(List<AuthorityDto> authorityList) {
        Set<Role> set = Sets.newConcurrentHashSet();
        final Long[] uid = new Long[1];
        authorityList.forEach(authority -> {
            uid[0] = Long.valueOf(authority.getUid());
            Role role = new Role();
            role.setId(authority.getUid());
            role.setRid(authority.getRid());
            role.setName(this.showRolesById(authority.getRid()).getName());
            set.add(role);
        });
        List<Role> roles = this.showRoles(uid[0]);
        Set<Role> roleSet = Sets.newConcurrentHashSet(roles);
        ImmutableList<Role> difference = Sets.difference(roleSet, set).immutableCopy().asList();
        if (difference.isEmpty()) {
            throw new RuntimeException("没有相关权限");
        }
        String lockKey = "deleteAuthority_key";
        String requestId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requestId, EXPIRE_TIME)) {
            throw new RuntimeException("操作太快");
        } else {
            AtomicInteger result = new AtomicInteger( );
            difference.forEach(role -> result.set(adminMapper.deleteAuthority(Long.valueOf(role.getId()), role.getRid())));
            if (RedisTool.releaseDistributedLock(jedis, lockKey, requestId)) {
                return 1;
            } else {
                log.error("释放锁失败:" + lockKey + "," + requestId);
                throw new RuntimeException("释放锁失败");
            }
        }
    }

    @Override
    public int editCustomer(CustomerDto customerDto) {
        String lockKey = "editCustomer_key";
        String requestId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requestId, EXPIRE_TIME)) {
            throw new RuntimeException("操作太快");
        } else {
            int result = adminMapper.editCustomer(customerDto.getName(), customerDto.getAge(),
                    customerDto.getTel(), customerDto.getEmail(),
                    customerDto.getDriveNum(), customerDto.getDriveType(),
                    Long.valueOf(customerDto.getId()));
            if (RedisTool.releaseDistributedLock(jedis, lockKey, requestId)) {
                return result;
            } else {
                log.error("释放锁失败:" + lockKey + "," + requestId);
                throw new RuntimeException("释放锁失败");
            }
        }
    }

    @Override
    public List<Customer> blackList() {
        return adminMapper.blackList();
    }

    @Override
    public int cancelBlackList(Long id) {
        String lockKey = "cancelBlackList_key";
        String requestId = UUID.randomUUID().toString();
        if (RedisTool.tryGetDistributedLock(jedis, lockKey, requestId, EXPIRE_TIME)) {
            throw new RuntimeException("操作太快");
        } else {
            int result = adminMapper.cancelBlackList(id);
            if (RedisTool.releaseDistributedLock(jedis, lockKey, requestId)) {
                return result;
            } else {
                log.error("释放锁失败:" + lockKey + "," + requestId);
                throw new RuntimeException("释放锁失败");
            }
        }
    }

    @Override
    public List<Role> showRoles(Long id) {
        return customerMapper.findRoles(id);
    }
}
