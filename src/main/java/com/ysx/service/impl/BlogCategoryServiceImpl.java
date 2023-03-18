package com.ysx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysx.mapper.BlogMapper;
import com.ysx.pojo.Blog;
import com.ysx.pojo.BlogCategory;
import com.ysx.service.BlogCategoryService;
import com.ysx.mapper.BlogCategoryMapper;
import com.ysx.service.BlogService;
import com.ysx.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author YSX
* @description 针对表【tb_blog_category】的数据库操作Service实现
* @createDate 2023-03-16 11:33:22
*/
@Service
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory>
    implements BlogCategoryService {

    @Autowired
    private BlogCategoryMapper categoryMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public PageResult getCategoryPage(Integer page, Integer limit) {
        Page<BlogCategory> blogPage = new Page<>(page, limit);
        Page<BlogCategory> blogPages = categoryMapper.selectPage(blogPage, null);
        PageResult ans = new PageResult(blogPages.getRecords(), (int)blogPages.getTotal(), limit, page);
        return ans;
    }

    @Transactional
    @Override
    public boolean delBatch(Integer[] ids) {
        int category_id = categoryMapper.delete(new QueryWrapper<BlogCategory>().in("category_id", ids));
        int i = blogMapper.updateBlogByCategoryIds(ids);
        if (category_id > 0){
            return true;
        }
        return false;
    }
}




