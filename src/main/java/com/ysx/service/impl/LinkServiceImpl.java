package com.ysx.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysx.pojo.Blog;
import com.ysx.pojo.Link;
import com.ysx.service.LinkService;
import com.ysx.mapper.LinkMapper;
import com.ysx.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author YSX
* @description 针对表【tb_link】的数据库操作Service实现
* @createDate 2023-03-16 11:33:22
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService {

    @Autowired
    private LinkMapper linkMapper;

    @Override
    public PageResult getLinkPage(int page, int limit) {
        Page<Link> blogPage = new Page<>(page, limit);
//        Page<Link> blogPages = linkMapper.selectPage(blogPage, null);
//        List<Link> links = blogPages.getRecords();
//        List<Link> collect = links.stream().sorted((e, e1) -> {
//            return e.getLinkRank() - e1.getLinkRank();
//        }).collect(Collectors.toList());
        List<Link> links = linkMapper.selectList(null);
        List<Link> collect = links.stream().sorted((e, e1) -> {
            return e.getLinkRank() - e1.getLinkRank();
        }).collect(Collectors.toList());
        int left = 0;
        if ((page - 1) * limit + limit > links.size()){
            left = links.size();
        }else{
            left = (page - 1) * limit + limit;
        }
        List<Link> ansList = collect.subList((page - 1) * limit, left);

        PageResult ans = new PageResult(ansList, links.size(), limit, page);
        return ans;
    }
}




