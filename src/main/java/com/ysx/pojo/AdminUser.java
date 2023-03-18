package com.ysx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 
 * @TableName tb_admin_user
 */
@TableName(value ="tb_admin_user")
@Data
public class AdminUser implements Serializable {
    /**
     * 管理员id
     */
    @TableId(value = "admin_user_id", type = IdType.AUTO)
    private Integer adminUserId;

    /**
     * 管理员登陆名称
     */
    @TableField(value = "login_user_name")
    private String loginUserName;

    /**
     * 管理员登陆密码
     */
    @TableField(value = "login_password")
    private String loginPassword;

    /**
     * 管理员显示昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 是否锁定 0未锁定 1已锁定无法登陆
     */
    @TableField(value = "locked")
    private Integer locked;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}