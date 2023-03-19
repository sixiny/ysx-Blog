package com.ysx.service;

import com.ysx.pojo.BlogComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ysx.utils.PageResult;

/**
* @author YSX
* @description 针对表【tb_blog_comment】的数据库操作Service
* @createDate 2023-03-16 11:33:22
*/
public interface BlogCommentService extends IService<BlogComment> {

    PageResult getCommentPage(int page, int limit);


    boolean checkDone(Integer[] ids);


    boolean reply(Long commentId, String replyBody);

    //找出相应blog的评论并分页
    PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int page);
}
