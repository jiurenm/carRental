package com.edu.feign.dto;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * carRental
 *
 * @author Administrator
 * @date 2018/12/29 22:44
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Results {
    /** 操作成功 */
    private static final int SUCCESS = 200;
    /** 操作失败 */
    private static final int FAILED = 500;
    /** 参数校验失败 */
    private static final int VALIDATE_FAILED = 400;
    /** 未认证 */
    private static final int UNAUTHORIZED = 401;
    /** 未授权 */
    private static final int FORBIDDEN = 403;

    private static final int NOTFOUND = 404;

    private int code;
    private String message;
    private Object data;

    public Results success(Object data) {
        this.code = SUCCESS;
        this.message = "操作成功";
        this.data = data;
        return this;
    }

    public Results failed() {
        this.code = FAILED;
        this.message = "操作失败";
        return this;
    }

    public Results failed(Object data) {
        this.code = FAILED;
        this.message = "操作失败";
        this.data = data;
        return this;
    }

    public Results pageSuccess(List data) {
        PageInfo pageInfo = new PageInfo(data);
        ImmutableMap<String,Object> result = ImmutableMap.of(
                "pageSize",pageInfo.getPageSize(),
                "totalPage",pageInfo.getPages(),
                "total",pageInfo.getTotal(),
                "pageNum", pageInfo.getPageNum(),
                "list",pageInfo.getList());
        this.code = SUCCESS;
        this.message = "操作成功";
        this.data = result;
        return this;
    }

    /**
     * 参数验证失败
     *
     * @param message 错误信息
     */
    public Results validateFailed(String message) {
        this.code = VALIDATE_FAILED;
        this.message = message;
        return this;
    }

    public Results validateFailed(BindingResult result) {
        this.code = VALIDATE_FAILED;
        List<String> errors = Lists.newArrayList();
        for(ObjectError error:result.getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }
        this.message = Joiner.on(",").join(errors);
        return this;
    }

    public Results unauthorized(String message) {
        this.code = UNAUTHORIZED;
        this.message = "未登录";
        this.data = message;
        return this;
    }

    public Results forbidden(String message) {
        this.code = FORBIDDEN;
        this.message = "没有相关权限";
        this.data = message;
        return this;
    }

    public Results notfound() {
        this.code = NOTFOUND;
        this.message = "服务器断开连接";
        return this;
    }
}
