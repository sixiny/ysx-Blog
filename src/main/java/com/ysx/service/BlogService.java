package com.ysx.service;

import com.ysx.controllers.vo.BlogDetail;
import com.ysx.controllers.vo.BlogList;
import com.ysx.controllers.vo.SimpleBlogList;
import com.ysx.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ysx.utils.PageResult;

import java.util.List;

/**
* @author YSX
* @description 针对表【tb_blog】的数据库操作Service
* @createDate 2023-03-16 11:33:22
*/
public interface BlogService extends IService<Blog> {

    PageResult getBlogPage(int page, int limit);


    String saveBlog(Blog blog);

    String delBlogs(Integer[] ids);

    boolean updateBlogByCategory(Integer id, String categoryName);

    //返回的是bloglist的内容
    PageResult getBlogsForIndexPage(int page);

    //查找最新的 blog
    List<SimpleBlogList> getBlogListForNew();

    //获取点击量最多的blog
    List<SimpleBlogList> getBlogListForViews();

    //获取博客详情信息
    BlogDetail getBlogDetail(Long blogId);

    //根据标签获取分页列表
    PageResult getBlogsPageByTag(String tagName, int page);

    //根据分类切分blog
    PageResult getBlogsPageByCategory(String categoryName, int page);


    //根据suburl获取博客信息
    BlogDetail getBlogDetailBySubUrl(String subUrl);

    //关键字查询博客
    PageResult getBlogsPageBySearch(String keyword, Integer page);


}
