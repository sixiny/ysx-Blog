package com.ysx.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ysx.controllers.vo.SimpleBlogList;
import com.ysx.pojo.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author YSX
* @description 针对表【tb_blog】的数据库操作Mapper
* @createDate 2023-03-16 11:33:22
* @Entity com.ysx.pojo.TbBlog
*/
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    //根据category更新后 同步更新blog
    int updateBlogByCategoryId(Integer id, String categoryName);

    //删除category后 关联blog中包含相关信息删除
    int updateBlogByCategoryIds(@Param("ids") Integer[] ids);

    //查找最新发表blog
    List<SimpleBlogList> getBlogListForNew();

    //查找hotblogs
    List<SimpleBlogList> getBlogListForViews();


}




