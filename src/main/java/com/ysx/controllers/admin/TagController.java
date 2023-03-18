package com.ysx.controllers.admin;

import com.ysx.pojo.BlogTag;
import com.ysx.service.BlogTagService;
import com.ysx.utils.Result;
import com.ysx.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ysx
 * @Date: 2023/03/16/21:39
 * @Description:
 */
@Api("标签处理器")
@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private BlogTagService tagService;

    @ApiOperation("侧边栏路由")
    @GetMapping("/tags")
    public String tagPage(HttpServletRequest request) {
        request.setAttribute("path", "tags");
        return "admin/tag";
    }

    @ApiOperation("标签分页查询")
    @GetMapping("/tags/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Integer page = Integer.parseInt((String) params.get("page"));
        Integer limit = Integer.parseInt((String)params.get("limit"));
        return ResultGenerator.genSuccessResult(tagService.getTagPage(page, limit));
    }

    @ApiOperation("新增标签")
    @PostMapping("/tags/save")
    @ResponseBody
    public Result save(String tagName) {
        if (!StringUtils.hasText(tagName)) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (tagService.saveTag(tagName)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("标签名称重复");
        }
    }

    @ApiOperation("批量删除标签")
    @PostMapping("/tags/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (tagService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult("没有关联数据的已删除");
        } else {
            return ResultGenerator.genFailResult("有关联数据请勿强行删除");
        }
    }
}
