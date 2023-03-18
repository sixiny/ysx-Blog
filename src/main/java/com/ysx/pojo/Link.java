package com.ysx.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName tb_link
 */
@TableName(value ="tb_link")
@Data
public class Link implements Serializable {
    /**
     * 友链表主键id
     */
    @TableId(value = "link_id", type = IdType.AUTO)
    private Integer linkId;

    /**
     * 友链类别 0-友链 1-推荐 2-个人网站
     */
    @TableField(value = "link_type")
    private Integer linkType;

    /**
     * 网站名称
     */
    @TableField(value = "link_name")
    private String linkName;

    /**
     * 网站链接
     */
    @TableField(value = "link_url")
    private String linkUrl;

    /**
     * 网站描述
     */
    @TableField(value = "link_description")
    private String linkDescription;

    /**
     * 用于列表排序
     */
    @TableField(value = "link_rank")
    private Integer linkRank;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 添加时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}