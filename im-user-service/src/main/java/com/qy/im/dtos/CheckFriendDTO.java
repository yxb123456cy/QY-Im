package com.qy.im.dtos;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CheckFriendDTO implements Serializable {
    /**
     * 当前登录用户ID
     */
    @TableField(value = "masterID")
    private Long masterId;

    /**
     * 好友的ID
     */
    @TableField(value = "friendID")
    private Long friendId;


    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
