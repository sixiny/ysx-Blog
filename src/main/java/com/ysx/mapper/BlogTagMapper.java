package com.ysx.mapper;

import com.ysx.pojo.BlogTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ysx.pojo.BlogTagCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author YSX
* @description 针对表【tb_blog_tag】的数据库操作Mapper
* @createDate 2023-03-16 11:33:22
* @Entity com.ysx.pojo.TbBlogTag
*/
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {

    // 根据tag名判断是不是blog_tag中有没有
    BlogTag selectTagByName(String name);

    //根据tagMin
    void delTagsByNames(@Param("names") String[] names);

    //查询tag以及tag使用的情况
    List<BlogTagCount> getBlogTagCountForIndex();

    //查找用到的tag
    List<Integer> getTags();

    //查找tag出现次数
    int getTagsNums(int id);



}




