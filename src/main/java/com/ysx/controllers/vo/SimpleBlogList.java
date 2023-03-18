package com.ysx.controllers.vo;

import lombok.ToString;

import java.io.Serializable;

/**
 * 简易blog信息
 */
@ToString
public class SimpleBlogList implements Serializable {

    private Long blogId;

    private String blogTitle;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }
}
