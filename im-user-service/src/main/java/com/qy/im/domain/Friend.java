package com.qy.im.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 好友表
 * @TableName friend
 */
@TableName(value ="friend")
@Data
public class Friend implements Serializable {
    /**
     * 主键,自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 当前登录用户ID
     */
    @TableField(value = "masterID")
    private Long masterid;

    /**
     * 好友的ID
     */
    @TableField(value = "friendID")
    private Long friendid;

    /**
     * 对好友的备注
     */
    @TableField(value = "notice")
    private String notice;

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
        Friend other = (Friend) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMasterid() == null ? other.getMasterid() == null : this.getMasterid().equals(other.getMasterid()))
            && (this.getFriendid() == null ? other.getFriendid() == null : this.getFriendid().equals(other.getFriendid()))
            && (this.getNotice() == null ? other.getNotice() == null : this.getNotice().equals(other.getNotice()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMasterid() == null) ? 0 : getMasterid().hashCode());
        result = prime * result + ((getFriendid() == null) ? 0 : getFriendid().hashCode());
        result = prime * result + ((getNotice() == null) ? 0 : getNotice().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", masterid=").append(masterid);
        sb.append(", friendid=").append(friendid);
        sb.append(", notice=").append(notice);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}