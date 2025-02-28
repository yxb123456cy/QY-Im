package com.qy.im.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "message")
public class Message implements Serializable {
    @TableField(value = "messageID")
    private String messageID; //消息ID使用UUIDUtil生成;
    @TableField(value = "masterID")
    private Long masterID;
    @TableField(value = "friendID")
    private Long friendID;
    @TableField(value = "content")
    private String  content;//发送消息内容;
    @TableField(value = "sendTime")
    private Date sendTime;//发送时间;
    @TableField(value = "readStatus")
    private int readStatus; //默认为0;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
