package com.ysx.service;

import com.ysx.pojo.BlogTag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ysx.pojo.BlogTagCount;
import com.ysx.utils.PageResult;

import java.util.List;

/**
* @author YSX
* @description 针对表【tb_blog_tag】的数据库操作Service
* @createDate 2023-03-16 11:33:22
*/
public interface BlogTagService extends IService<BlogTag> {

    boolean checkTagByName(String name);

    void delTagsByName(String[] names);

    PageResult getTagPage(int page, int limit);

    boolean saveTag(String tagName);

    boolean deleteBatch(Integer[] ids);

    List<BlogTagCount> getBlogTagCountForIndex();
}
