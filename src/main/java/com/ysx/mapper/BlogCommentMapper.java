package com.ysx.mapper;

import com.ysx.pojo.BlogComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author YSX
* @description 针对表【tb_blog_comment】的数据库操作Mapper
* @createDate 2023-03-16 11:33:22
* @Entity com.ysx.pojo.TbBlogComment
*/
@Mapper
public interface BlogCommentMapper extends BaseMapper<BlogComment> {

    int checkComment(@Param("ids") Integer[] ids);

}




