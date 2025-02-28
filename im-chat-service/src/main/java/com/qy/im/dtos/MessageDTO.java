package com.qy.im.dtos;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MessageDTO implements Serializable {
    private Long masterID;
    private Long friendID;
    private String  content;//发送消息内容;
    private String token;//token值;
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
