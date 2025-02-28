package com.qy.im.dtos;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserUpdateInfoDTO implements Serializable {
    /**
     * 账号,唯一索引
     */
    @TableField(value = "account")
    private String account;

    /**
     * 用户昵称
     */
    @TableField(value = "nickName")
    private String nickname;

    /**
     * 用户简介
     */
    @TableField(value = "description")
    private String description;

    /**
     * 用户头像URL
     */
    @TableField(value = "avatar")
    private String avatar;


    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
