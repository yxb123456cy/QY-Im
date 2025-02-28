package com.qy.im.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 好友验证记录表
 * @TableName friendverify
 */
@TableName(value ="friendverify")
@Data
public class Friendverify implements Serializable {
    /**
     * 主键,自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发起验证用户ID
     */
    @TableField(value = "sendUserId")
    private Long senduserid;

    /**
     * 接受验证用户ID
     */
    @TableField(value = "revUserId")
    private Long revuserid;

    /**
     * 状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 附加消息
     */
    @TableField(value = "AdditionalMessages")
    private String additionalmessages;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Friendverify other = (Friendverify) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSenduserid() == null ? other.getSenduserid() == null : this.getSenduserid().equals(other.getSenduserid()))
            && (this.getRevuserid() == null ? other.getRevuserid() == null : this.getRevuserid().equals(other.getRevuserid()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getAdditionalmessages() == null ? other.getAdditionalmessages() == null : this.getAdditionalmessages().equals(other.getAdditionalmessages()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSenduserid() == null) ? 0 : getSenduserid().hashCode());
        result = prime * result + ((getRevuserid() == null) ? 0 : getRevuserid().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAdditionalmessages() == null) ? 0 : getAdditionalmessages().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", senduserid=").append(senduserid);
        sb.append(", revuserid=").append(revuserid);
        sb.append(", status=").append(status);
        sb.append(", additionalmessages=").append(additionalmessages);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}