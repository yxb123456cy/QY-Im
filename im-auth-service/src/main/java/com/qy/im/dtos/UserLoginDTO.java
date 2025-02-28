package com.qy.im.dtos;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserLoginDTO implements Serializable {
    /**
     * 账号,唯一索引
     */
    @TableField(value = "account")
    private String account;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
