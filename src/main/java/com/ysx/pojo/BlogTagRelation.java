package com.ysx.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName tb_blog_tag_relation
 */
@TableName(value ="tb_blog_tag_relation")
@Data
public class BlogTagRelation implements Serializable {
    /**
     * 关系表id
     */
    @TableId(value = "relation_id", type = IdType.AUTO)
    private Long relationId;

    /**
     * 博客id
     */
    @TableField(value = "blog_id")
    private Long blogId;

    /**
     * 标签id
     */
    @TableField(value = "tag_id")
    private Integer tagId;

    /**
     * 添加时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}