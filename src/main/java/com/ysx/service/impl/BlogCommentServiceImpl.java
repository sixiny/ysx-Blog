package com.ysx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysx.mapper.BlogCategoryMapper;
import com.ysx.pojo.BlogComment;
import com.ysx.service.BlogCommentService;
import com.ysx.mapper.BlogCommentMapper;
import com.ysx.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
* @author YSX
* @description 针对表【tb_blog_comment】的数据库操作Service实现
* @createDate 2023-03-16 11:33:22
*/
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment>
    implements BlogCommentService {

    @Autowired
    private BlogCommentMapper commentMapper;

    @Override
    public PageResult getCommentPage(int page, int limit) {
        Page<BlogComment> commentpage = new Page<>(page, limit);
        Page<BlogComment> currentPage = commentMapper.selectPage(commentpage, null);
        PageResult ans = new PageResult(currentPage.getRecords(), (int)currentPage.getTotal(), limit, page);
        return ans;
    }

    @Override
    public boolean checkDone(Integer[] ids) {
        int i = commentMapper.checkComment(ids);
        return i > 0;
    }

    @Override
    public boolean reply(Long commentId, String replyBody) {
        BlogComment blogComment = commentMapper.selectById(commentId);
        if (blogComment!=null && blogComment.getCommentStatus().intValue() == 1){
            blogComment.setReplyBody(replyBody);
            blogComment.setReplyCreateTime(new Date());
            return commentMapper.update(blogComment, new QueryWrapper<BlogComment>().eq("comment_id", commentId)) >0;
        }
        return false;
    }

    @Override
    public PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int page) {
        if (page < 1) {
            return null;
        }
        Page page1 = new Page(page, 8);
        Page page2 = commentMapper.selectPage(page1, new QueryWrapper<BlogComment>().eq("blog_id", blogId).eq("comment_status", 1));
        if (!CollectionUtil.isEmpty(page2.getRecords())){
            PageResult pageResult = new PageResult(page2.getRecords(), (int)page2.getTotal(), 8, page);
            return pageResult;
        }
        return null;
    }
}




