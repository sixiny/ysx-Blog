package com.ysx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ysx.pojo.Blog;
import com.ysx.pojo.BlogCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ysx.utils.PageResult;

/**
* @author YSX
* @description 针对表【tb_blog_category】的数据库操作Service
* @createDate 2023-03-16 11:33:22
*/
public interface BlogCategoryService extends IService<BlogCategory> {

    PageResult getCategoryPage(Integer page, Integer limit);

    boolean delBatch(Integer[] ids);
}
