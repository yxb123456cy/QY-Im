package com.qy.im.dtos;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class VerifyCationQuestionAddDTO implements Serializable {
    /**
     * 所属用户ID
     */
    @TableField(value = "userID")
    private Long userid;

    /**
     * 问题
     */
    @TableField(value = "problem")
    private String problem;

    /**
     * 正确答案
     */
    @TableField(value = "answer")
    private String answer;
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
