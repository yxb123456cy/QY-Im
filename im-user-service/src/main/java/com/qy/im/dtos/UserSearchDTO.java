package com.qy.im.dtos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserSearchDTO implements Serializable {
    /**
     * 主键,唯一ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 账号,唯一索引
     */
    @TableField(value = "account")
    private String account;

    @Serial
    private static final long serialVersionUID = 1L;
}
