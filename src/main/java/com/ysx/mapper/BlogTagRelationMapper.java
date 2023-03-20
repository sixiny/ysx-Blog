package com.ysx.mapper;

import com.ysx.pojo.BlogTagRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author YSX
* @description 针对表【tb_blog_tag_relation】的数据库操作Mapper
* @createDate 2023-03-16 11:33:22
* @Entity com.ysx.pojo.TbBlogTagRelation
*/
@Mapper
public interface BlogTagRelationMapper extends BaseMapper<BlogTagRelation> {

    //根据tag id查询blog id
    Integer[] getBlogIdsByTagId(int tagId);
}




