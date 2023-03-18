package com.ysx.controllers.blog;

import com.ysx.service.BlogService;
import com.ysx.service.BlogTagService;
import com.ysx.service.ConfigService;
import com.ysx.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ysx
 * @Date: 2023/03/17/16:08
 * @Description: 博客展示页面控制器
 */
@Api("博客前端展示控制器")
@Controller
public class BlogMainController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogTagService tagService;

    @Autowired
    private ConfigService configService;

    public static String theme = "amaze";

    @ApiOperation("初始化页面加载")
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    @ApiOperation("博客首页 分页展示")
    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
        PageResult blogPageResult = blogService.getBlogsForIndexPage(pageNum);
        if (blogPageResult == null) {
            return "error/error_404";
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("newBlogs", blogService.getBlogListForNew());
        request.setAttribute("hotBlogs", blogService.getBlogListForViews());
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/index";
    }


}
