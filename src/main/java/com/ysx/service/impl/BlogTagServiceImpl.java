package com.ysx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysx.mapper.BlogTagRelationMapper;
import com.ysx.pojo.Blog;
import com.ysx.pojo.BlogTag;
import com.ysx.pojo.BlogTagCount;
import com.ysx.pojo.BlogTagRelation;
import com.ysx.service.BlogTagRelationService;
import com.ysx.service.BlogTagService;
import com.ysx.mapper.BlogTagMapper;
import com.ysx.utils.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
* @author YSX
* @description 针对表【tb_blog_tag】的数据库操作Service实现
* @createDate 2023-03-16 11:33:22
*/
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag>
    implements BlogTagService {

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private BlogTagRelationMapper tagRelationMapper;

    @Override
    public boolean checkTagByName(String name) {
        return !(blogTagMapper.selectTagByName(name)==null);
    }

    @Override
    public void delTagsByName(String[] names) {
        blogTagMapper.delTagsByNames(names);
    }

    @Override
    public PageResult getTagPage(int page, int limit) {
        Page<BlogTag> blogPage = new Page<>(page, limit);
        Page<BlogTag> blogPages = blogTagMapper.selectPage(blogPage, null);
        PageResult ans = new PageResult(blogPages.getRecords(), (int)blogPages.getTotal(), limit, page);
        return ans;
    }

    @Override
    public boolean saveTag(String tagName) {
        BlogTag blogTag = blogTagMapper.selectTagByName(tagName);
        if (blogTag == null){
            BlogTag blogTag1 = new BlogTag();
            blogTag1.setTagName(tagName);
            blogTag1.setCreateTime(new Date());
            blogTagMapper.insert(blogTag1);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteBatch(Integer[] ids) {
        int canDel = 0;
        // 没有关联的就删
        for (Integer e : ids) {
            BlogTagRelation blogTagRelation = tagRelationMapper.selectOne(new QueryWrapper<BlogTagRelation>().eq("tag_id", e));
            if (blogTagRelation == null){
                canDel += 1;
                blogTagMapper.deleteById(e);
            }
        }
        return canDel > 0;
    }

    @Override
    public List<BlogTagCount> getBlogTagCountForIndex() {
        ArrayList<BlogTagCount> ans = new ArrayList<>();
        List<Integer> tagsIds = blogTagMapper.getTags();
        HashMap<Integer, Integer> tagNums = new HashMap<>();
        tagsIds.stream().forEach(e->{
            BlogTag blogTag = blogTagMapper.selectById(e);
            BlogTagCount tag = new BlogTagCount();
            BeanUtils.copyProperties(blogTag, tag);
            tag.setTagCount(blogTagMapper.getTagsNums(e));
            ans.add(tag);
        });
        return ans;
    }
}




