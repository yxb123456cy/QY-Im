package com.qy.im.vos;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserLoginRespVO implements Serializable {
    /**
     * 账号,唯一索引
     */
    @TableField(value = "account")
    private String account;

    private Long userId; //用户ID
    private String tokenValue; //tokenValue;
    private String tokenName;//token Name;
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
