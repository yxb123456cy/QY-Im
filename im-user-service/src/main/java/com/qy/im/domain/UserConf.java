package com.qy.im.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户配置表
 * @TableName user_conf
 */
@TableName(value ="user_conf")
@Data
public class UserConf implements Serializable {
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
     * 撤回消息时提示的内容
     */
    @TableField(value = "RecallMessage")
    private String recallmessage;

    /**
     * 好友上线是否提醒
     */
    @TableField(value = "FriendOnline")
    private Integer friendonline;

    /**
     * 是否打开声音
     */
    @TableField(value = "sound")
    private Integer sound;

    /**
     * 是否开启安全链接
     */
    @TableField(value = "SecureLink")
    private Integer securelink;

    /**
     * 是否保存密码
     */
    @TableField(value = "savePwd")
    private Integer savepwd;

    /**
     * 他人查找到我的方式 0 不允许别人查找到我， 1  通过用户号找到我 2 可以通过昵称搜索到我
     */
    @TableField(value = "searchMyWay")
    private Integer searchmyway;

    /**
     * 好友验证配置 0 不允许任何人添加  1 允许任何人添加  2 需要验证消息 3 需要回答问题  4  需要正确回答问题 默认2
     */
    @TableField(value = "Verification")
    private Integer verification;

    /**
     * 是否在线 默认为false
     */
    @TableField(value = "Online")
    private Integer online;

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
        UserConf other = (UserConf) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMasterid() == null ? other.getMasterid() == null : this.getMasterid().equals(other.getMasterid()))
            && (this.getRecallmessage() == null ? other.getRecallmessage() == null : this.getRecallmessage().equals(other.getRecallmessage()))
            && (this.getFriendonline() == null ? other.getFriendonline() == null : this.getFriendonline().equals(other.getFriendonline()))
            && (this.getSound() == null ? other.getSound() == null : this.getSound().equals(other.getSound()))
            && (this.getSecurelink() == null ? other.getSecurelink() == null : this.getSecurelink().equals(other.getSecurelink()))
            && (this.getSavepwd() == null ? other.getSavepwd() == null : this.getSavepwd().equals(other.getSavepwd()))
            && (this.getSearchmyway() == null ? other.getSearchmyway() == null : this.getSearchmyway().equals(other.getSearchmyway()))
            && (this.getVerification() == null ? other.getVerification() == null : this.getVerification().equals(other.getVerification()))
            && (this.getOnline() == null ? other.getOnline() == null : this.getOnline().equals(other.getOnline()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMasterid() == null) ? 0 : getMasterid().hashCode());
        result = prime * result + ((getRecallmessage() == null) ? 0 : getRecallmessage().hashCode());
        result = prime * result + ((getFriendonline() == null) ? 0 : getFriendonline().hashCode());
        result = prime * result + ((getSound() == null) ? 0 : getSound().hashCode());
        result = prime * result + ((getSecurelink() == null) ? 0 : getSecurelink().hashCode());
        result = prime * result + ((getSavepwd() == null) ? 0 : getSavepwd().hashCode());
        result = prime * result + ((getSearchmyway() == null) ? 0 : getSearchmyway().hashCode());
        result = prime * result + ((getVerification() == null) ? 0 : getVerification().hashCode());
        result = prime * result + ((getOnline() == null) ? 0 : getOnline().hashCode());
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
        sb.append(", recallmessage=").append(recallmessage);
        sb.append(", friendonline=").append(friendonline);
        sb.append(", sound=").append(sound);
        sb.append(", securelink=").append(securelink);
        sb.append(", savepwd=").append(savepwd);
        sb.append(", searchmyway=").append(searchmyway);
        sb.append(", verification=").append(verification);
        sb.append(", online=").append(online);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}