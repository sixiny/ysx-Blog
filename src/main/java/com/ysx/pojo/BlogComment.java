package com.ysx.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName tb_blog_comment
 */
@TableName(value ="tb_blog_comment")
@Data
public class BlogComment implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     * 关联的blog主键
     */
    @TableField(value = "blog_id")
    private Long blogId;

    /**
     * 评论者名称
     */
    @TableField(value = "commentator")
    private String commentator;

    /**
     * 评论人的邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 网址
     */
    @TableField(value = "website_url")
    private String websiteUrl;

    /**
     * 评论内容
     */
    @TableField(value = "comment_body")
    private String commentBody;

    /**
     * 评论提交时间
     */
    @TableField(value = "comment_create_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private Date commentCreateTime;

    /**
     * 评论时的ip地址
     */
    @TableField(value = "commentator_ip")
    private String commentatorIp;

    /**
     * 回复内容
     */
    @TableField(value = "reply_body")
    private String replyBody;

    /**
     * 回复时间
     */
    @TableField(value = "reply_create_time")
    private Date replyCreateTime;

    /**
     * 是否审核通过 0-未审核 1-审核通过
     */
    @TableField(value = "comment_status")
    private Integer commentStatus;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}