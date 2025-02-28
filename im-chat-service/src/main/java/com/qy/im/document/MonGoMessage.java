package com.qy.im.document;


import com.anwen.mongo.annotation.ID;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class MonGoMessage implements Serializable {
    @ID
    private String messageID; //消息ID使用UUIDUtil生成;
    private Long masterID;
    private Long friendID;
    private String  content;//发送消息内容;
    private Date sendTime;//发送时间;
    private int readStatus; //默认为0;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
