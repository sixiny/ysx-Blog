package com.ysx.controllers.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ysx.common.Constants;
import com.ysx.pojo.Blog;
import com.ysx.service.BlogCategoryService;
import com.ysx.service.BlogService;
import com.ysx.utils.MyBlogUtils;
import com.ysx.utils.PageQueryUtil;
import com.ysx.utils.Result;
import com.ysx.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ysx
 * @Date: 2023/03/16/15:00
 * @Description:
 */
@ApiOperation("博客业务处理器")
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogCategoryService categoryService;

    @ApiOperation("侧边栏路由")
    @GetMapping("/blogs")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "blogs");
        return "admin/blog";
    }

    @ApiOperation("获取blog分页数据")
    @GetMapping("/blogs/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Integer page = Integer.parseInt((String) params.get("page"));
        Integer limit = Integer.parseInt((String)params.get("limit"));
        return ResultGenerator.genSuccessResult(blogService.getBlogPage(page, limit));
    }

    @ApiOperation("跳转到修改页面")
    @GetMapping("/blogs/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        request.setAttribute("categories", categoryService.list());
        return "admin/edit";
    }

    @ApiOperation("修改指定博客")
    @GetMapping("/blogs/edit/{blogId}")
    public String edit(HttpServletRequest request, @PathVariable("blogId") Long blogId) {
        request.setAttribute("path", "edit"); //侧边栏路由
        Blog blog = blogService.getById(blogId);
        if (blog==null) {
            return "error/error_400";
        }
        request.setAttribute("blog", blog);
        request.setAttribute("categories", categoryService.list());
        return "admin/edit";
    }

    @ApiOperation("新建博客提交保存")
    @PostMapping("/blogs/save")
    @ResponseBody
    public Result save(Long blogId, String blogTitle, String blogSubUrl, Integer blogCategoryId, String blogTags, String blogContent,
                       String blogCoverImage, Integer blogStatus,
                       Integer enableComment){
        Blog saveBlog = new Blog();
        saveBlog.setBlogId(blogId);
        saveBlog.setBlogTitle(blogTitle);
        saveBlog.setBlogSubUrl(blogSubUrl);
        saveBlog.setBlogCategoryId(blogCategoryId);
        saveBlog.setBlogTags(blogTags);
        saveBlog.setBlogContent(blogContent);
        saveBlog.setBlogCoverImage(blogCoverImage);
        saveBlog.setBlogStatus(blogStatus);
        saveBlog.setEnableComment(enableComment);
        if (saveBlog == null){
            return ResultGenerator.genFailResult("你还什么都没弄呢哥们");
        }
        String saveBlogResult = blogService.saveBlog(saveBlog);
        if ("success".equals(saveBlogResult)) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult(saveBlogResult);
        }
    }

    @ApiOperation("博客更新提交")
    @PostMapping("/blogs/update")
    @ResponseBody
    public Result update(Long blogId, String blogTitle, String blogSubUrl, Integer blogCategoryId,
                         String blogTags, String blogContent, String blogCoverImage,
                         Integer blogStatus, Integer enableComment) {
        Blog saveBlog = new Blog();
        saveBlog.setBlogId(blogId);
        saveBlog.setBlogTitle(blogTitle);
        saveBlog.setBlogSubUrl(blogSubUrl);
        saveBlog.setBlogCategoryId(blogCategoryId);
        saveBlog.setBlogTags(blogTags);
        saveBlog.setBlogContent(blogContent);
        saveBlog.setBlogCoverImage(blogCoverImage);
        saveBlog.setBlogStatus(blogStatus);
        saveBlog.setEnableComment(enableComment);
        if (saveBlog == null){
            return ResultGenerator.genFailResult("你还什么都没弄呢哥们");
        }
        boolean saveBlogResult = blogService.update(saveBlog, new QueryWrapper<Blog>().eq("blog_id", blogId));
        if (saveBlogResult) {
            return ResultGenerator.genSuccessResult("修改成功");
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }


    @ApiOperation("markdown编写时上传")
    @PostMapping("/blogs/md/uploadfile")
    public void uploadFileByEditormd(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(name = "editormd-image-file", required = true)
                                             MultipartFile file) throws IOException, URISyntaxException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        String fileUrl = MyBlogUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFileName;
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + fileUrl + "\"}");
        } catch (UnsupportedEncodingException e) {
            response.getWriter().write("{\"success\":0}");
        } catch (IOException e) {
            response.getWriter().write("{\"success\":0}");
        }
    }

    @ApiOperation("删除博客")
    @PostMapping("/blogs/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String is_delete = blogService.delBlogs(ids);
        if ("success".equals(is_delete)) {
            return ResultGenerator.genSuccessResult("删除成功");
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }







}
