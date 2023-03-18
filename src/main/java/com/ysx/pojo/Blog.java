package com.ysx.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName tb_blog
 */
@TableName(value ="tb_blog")
@Data
public class Blog implements Serializable {
    /**
     * 博客表主键id
     */
    @TableId(value = "blog_id", type = IdType.AUTO)
    private Long blogId;

    /**
     * 博客标题
     */
    @TableField(value = "blog_title")
    private String blogTitle;

    /**
     * 博客自定义路径url
     */
    @TableField(value = "blog_sub_url")
    private String blogSubUrl;

    /**
     * 博客封面图
     */
    @TableField(value = "blog_cover_image")
    private String blogCoverImage;

    /**
     * 博客内容
     */
    @TableField(value = "blog_content")
    private String blogContent;

    /**
     * 博客分类id
     */
    @TableField(value = "blog_category_id")
    private Integer blogCategoryId;

    /**
     * 博客分类(冗余字段)
     */
    @TableField(value = "blog_category_name")
    private String blogCategoryName;

    /**
     * 博客标签
     */
    @TableField(value = "blog_tags")
    private String blogTags;

    /**
     * 0-草稿 1-发布
     */
    @TableField(value = "blog_status")
    private Integer blogStatus;

    /**
     * 阅读量
     */
    @TableField(value = "blog_views")
    private Long blogViews;

    /**
     * 0-允许评论 1-不允许评论
     */
    @TableField(value = "enable_comment")
    private Integer enableComment;

    /**
     * 是否删除 0=否 1=是
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 添加时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}