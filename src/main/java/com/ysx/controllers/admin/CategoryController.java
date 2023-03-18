package com.ysx.controllers.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ysx.pojo.BlogCategory;
import com.ysx.service.BlogCategoryService;
import com.ysx.service.BlogService;
import com.ysx.utils.Result;
import com.ysx.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ysx
 * @Date: 2023/03/16/20:45
 * @Description:
 */

@Api("博客种类处理器")
@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private BlogCategoryService categoryService;

    @Autowired
    private BlogService blogService;

    @ApiOperation("侧边栏路由 页面跳转控制")
    @GetMapping("/categories")
    public String categoryPage(HttpServletRequest request) {
        request.setAttribute("path", "categories");
        return "admin/category";
    }

    @ApiOperation("类别分页查询")
    @GetMapping("/categories/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Integer page = Integer.parseInt((String) params.get("page"));
        Integer limit = Integer.parseInt((String)params.get("limit"));
        return ResultGenerator.genSuccessResult(categoryService.getCategoryPage(page, limit));
    }

    @ApiOperation("添加分类名")
    @PostMapping("/categories/save")
    @ResponseBody
    public Result save(@RequestParam("categoryName") String categoryName,
                       @RequestParam("categoryIcon") String categoryIcon) {
        if (!StringUtils.hasText(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (!StringUtils.hasText(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategoryName(categoryName);
        blogCategory.setCategoryIcon(categoryIcon);
        blogCategory.setCreateTime(new Date());
        if (categoryService.save(blogCategory)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }


    @ApiOperation("更新类别信息/类别信息关联blog")
    @Transactional
    @PostMapping("/categories/update")
    @ResponseBody
    public Result update(@RequestParam("categoryId") Integer categoryId,
                         @RequestParam("categoryName") String categoryName,
                         @RequestParam("categoryIcon") String categoryIcon) {
        if (!StringUtils.hasText(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (!StringUtils.hasText(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategoryId(categoryId);
        blogCategory.setCategoryName(categoryName);
        blogCategory.setCategoryIcon(categoryIcon);
        boolean updateCategory = categoryService.update(blogCategory, new QueryWrapper<BlogCategory>().eq("category_id", categoryId));
        boolean updateblog = blogService.updateBlogByCategory(categoryId, categoryName);
        if (updateblog && updateCategory){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }

    @ApiOperation("删除现有分类")
    @PostMapping("/categories/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (categoryService.delBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }





}
