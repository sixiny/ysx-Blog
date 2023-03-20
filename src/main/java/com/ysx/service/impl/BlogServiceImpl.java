package com.ysx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.ysx.controllers.vo.BlogDetail;
import com.ysx.controllers.vo.BlogList;
import com.ysx.controllers.vo.SimpleBlogList;
import com.ysx.mapper.*;
import com.ysx.pojo.*;
import com.ysx.service.BlogService;
import com.ysx.service.BlogTagRelationService;
import com.ysx.service.BlogTagService;
import com.ysx.utils.MarkDownUtil;
import com.ysx.utils.PageResult;
import com.ysx.utils.PatternUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
* @author YSX
* @description 针对表【tb_blog】的数据库操作Service实现
* @createDate 2023-03-16 11:33:22
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogCategoryMapper categoryMapper;

    @Autowired
    private BlogTagService tagService;

    @Autowired
    private BlogTagMapper tagMapper;

    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Autowired
    private BlogTagRelationService tagRelationService;

    @Autowired
    private BlogTagRelationMapper tagRelationMapper;


    @Override
    public PageResult getBlogPage(int page, int limit) {
        Page<Blog> blogPage = new Page<>(page, limit);
        Page<Blog> blogPages = blogMapper.selectPage(blogPage, null);
        PageResult ans = new PageResult(blogPages.getRecords(), (int)blogPages.getTotal(), limit, page);
        return ans;
    }


    @Transactional
    @Override
    public String saveBlog(Blog blog) {
        BlogCategory blogCategory = categoryMapper.selectById(blog.getBlogCategoryId());
        if (blogCategory == null) {
            blog.setBlogCategoryId(24);
            blog.setBlogCategoryName("日常随笔");
        } else {
            //设置博客分类名称
            blog.setBlogCategoryName(blogCategory.getCategoryName());
            //分类的排序值加1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
        // 全删 再全加新的 就存在tb_blog_tags里面   然后更新关联表 tb_blog_tag_relation
//        String[] tags = blog.getBlogTags().split(",");
//        tagService.delTagsByName(tags);
//        ArrayList<BlogTag> tagList = new ArrayList<>();
//        Arrays.stream(tags).forEach(e->{
//            BlogTag temp = new BlogTag();
//            temp.setTagName(e);
//            temp.setCreateTime(new Date());
//            tagService.save(temp);
//            tagList.add(temp);
//        });
//
//        tagList.stream().forEach(e->{
//            BlogTagRelation btr = new BlogTagRelation();
//            btr.setBlogId(blog.getBlogId());
//            btr.setTagId(e.getTagId());
//            btr.setCreateTime(new Date());
//        });
        //
        if(blogMapper.insert(blog)!=-1){
            String[] tags = blog.getBlogTags().split(",");
            ArrayList<BlogTag> tagList = new ArrayList<>();

            Arrays.stream(tags).forEach(e->{
                if(tagService.checkTagByName(e)){
                    BlogTag blogTag = tagMapper.selectTagByName(e);
                    tagList.add(blogTag);
                }else{
                    BlogTag temp = new BlogTag();
                    temp.setTagName(e);
                    temp.setCreateTime(new Date());
                    tagService.save(temp);
                    tagList.add(temp);
                }
            });

            tagList.stream().forEach(e->{
                BlogTagRelation btr = new BlogTagRelation();
                btr.setBlogId(blog.getBlogId());
                btr.setTagId(e.getTagId());
                btr.setCreateTime(new Date());
                tagRelationService.save(btr);
            });

            if (blogCategory != null) {
                categoryMapper.update(blogCategory, new UpdateWrapper<BlogCategory>().eq("category_id", blogCategory.getCategoryId()));
            }
            return "success";
        };
        return "保存失败";
    }

    @Transactional
    @Override
    public String delBlogs(Integer[] ids) {
        Arrays.stream(ids).forEach(e->{
            blogMapper.deleteById(e);
            tagRelationService.remove(new QueryWrapper<BlogTagRelation>().eq("blog_id", e));
            //删除相关评论
            blogCommentMapper.delete(new QueryWrapper<BlogComment>().eq("blog_id", e));
        });
        return "success";
    }

    @Override
    public boolean updateBlogByCategory(Integer id, String categoryName) {
        return blogMapper.updateBlogByCategoryId(id, categoryName) > 0;
    }

    @Override
    public PageResult getBlogsForIndexPage(int page) {
        // 每页八个数据
        Page<Blog> param = new Page<>(page, 8);
        Page<Blog> baseBlogPage = blogMapper.selectPage(param, new QueryWrapper<Blog>().eq("blog_status", 1));
        List<BlogList> indexBlog = getBlogListVOsByBlogs(baseBlogPage.getRecords());
        PageResult pageResult = new PageResult(indexBlog, (int)baseBlogPage.getTotal(), 8, page);
        return pageResult;
    }

    @Override
    public List<SimpleBlogList> getBlogListForNew() {
        return blogMapper.getBlogListForNew();
    }

    @Override
    public List<SimpleBlogList> getBlogListForViews() {
        return blogMapper.getBlogListForViews();
    }

    @Transactional
    @Override
    public BlogDetail getBlogDetail(Long blogId) {
        Blog blog = blogMapper.selectById(blogId);
        BlogDetail blogDetailTemp = getBlogDetailTemp(blog);
        return blogDetailTemp;
    }

    @Override
    public PageResult getBlogsPageByTag(String tagName, int page) {
        if(PatternUtil.validKeyword(tagName)){
            BlogTag tag = tagMapper.selectOne(new QueryWrapper<BlogTag>().eq("tag_name", tagName));
            Integer[] blogIdsByTagId = tagRelationMapper.getBlogIdsByTagId(tag.getTagId());
            Page page1 = new Page(page, 9);
            Page blog_id = blogMapper.selectPage(page1, new QueryWrapper<Blog>().in("blog_id", blogIdsByTagId));
            List<BlogList> blogList = getBlogListVOsByBlogs(blog_id.getRecords());
            PageResult pageResult = new PageResult(blogList, (int)blog_id.getTotal(), 9, page);
            return pageResult;
        }
        return null;
    }

    @Override
    public PageResult getBlogsPageByCategory(String categoryName, int page) {
        if(PatternUtil.validKeyword(categoryName)){
            BlogCategory blogCategory = categoryMapper.selectOne(new QueryWrapper<BlogCategory>().eq("category_name", categoryName));
            if ("默认分类".equals(categoryName) && blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
            }
            if (blogCategory != null && page > 0) {
                Page page1 = blogMapper.selectPage(new Page(page, 9), new QueryWrapper<Blog>().eq("blog_category_id", blogCategory.getCategoryId()).eq("blog_status", 1));
                List<BlogList> blogList = getBlogListVOsByBlogs(page1.getRecords());
                PageResult pageResult = new PageResult(blogList, (int)page1.getTotal(), 9, page);
                return pageResult;
            }
        }
        return null;
    }

    @Override
    public BlogDetail getBlogDetailBySubUrl(String subUrl) {
        Blog blogSubUrl = blogMapper.selectOne(new QueryWrapper<Blog>().eq("blog_sub_url", subUrl));
        BlogDetail blogDetailTemp = getBlogDetailTemp(blogSubUrl);
        if (blogDetailTemp != null) {
            return blogDetailTemp;
        }
        return null;
    }

    @Override
    public PageResult getBlogsPageBySearch(String keyword, Integer page) {
        Page blog_title = blogMapper.selectPage(new Page(page, 9), new QueryWrapper<Blog>().like("blog_title", keyword));
        List blogList = getBlogListVOsByBlogs(blog_title.getRecords());
        PageResult pageResult = new PageResult(blogList, (int)blog_title.getTotal(), 9, page);
        return pageResult;
    }

    //封装博客详情信息
    private BlogDetail getBlogDetailTemp(Blog blog){
        //详情页包含Category图标 评论 评论数等
        if (blog != null && blog.getBlogStatus() == 1){
            //浏览量增加
            blog.setBlogViews(blog.getBlogViews() + 1);
            blogMapper.update(blog, new QueryWrapper<Blog>().eq("blog_id", blog.getBlogId()));
            BlogDetail blogDetail = new BlogDetail();
            BeanUtils.copyProperties(blog, blogDetail);
            blogDetail.setEnableComment(Byte.parseByte(String.valueOf(blog.getEnableComment())));
            //转存数据库的markdown为html给前端页面展示
            blogDetail.setBlogContent(MarkDownUtil.mdToHtml(blogDetail.getBlogContent()));
            BlogCategory blogCategory = categoryMapper.selectById(blog.getBlogCategoryId());
            if (blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            //分类信息
            blogDetail.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            if (StringUtils.hasText(blog.getBlogTags())) {
                //标签设置
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetail.setBlogTags(tags);
            }
            //设置评论数
            int num = blogCommentMapper.blogCommentCount(blogDetail.getBlogId(), 1);
            blogDetail.setCommentCount(num);
            return blogDetail;
        }
        return null;
    }


    //封装查询信息进行简单的博客主页内容展示
    private List<BlogList> getBlogListVOsByBlogs(List<Blog> blogList) {
        // 查出关联的category的图标  赋给 BlogList用于前端页面展示
        List<BlogList> ans = new ArrayList<>();
        if (!CollectionUtil.isEmpty(blogList)){
            blogList.stream().forEach(e->{
                BlogList blogListTemp = new BlogList();
                BeanUtils.copyProperties(e, blogListTemp);
                BlogCategory blogCategory = categoryMapper.selectById(blogListTemp.getBlogCategoryId());
                if (blogCategory!=null){
                    blogListTemp.setBlogCategoryIcon(blogCategory.getCategoryIcon());
                }else{
                    blogListTemp.setBlogCategoryId(0);
                    blogListTemp.setBlogCategoryName("默认分类");
                    blogListTemp.setBlogCategoryIcon("/admin/dist/img/category/00.png");
                }
                ans.add(blogListTemp);
            });

        }
        return ans;
    }
}




