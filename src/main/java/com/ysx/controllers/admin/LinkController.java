package com.ysx.controllers.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ysx.pojo.Link;
import com.ysx.service.LinkService;
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
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ysx
 * @Date: 2023/03/17/14:36
 * @Description: 友情链接处理器
 */
@Api("友链处理器")
@Controller
@RequestMapping("/admin")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @ApiOperation("侧边栏路由")
    @GetMapping("/links")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "links");
        return "admin/link";
    }

    @ApiOperation("获取blog分页数据")
    @GetMapping("/links/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Integer page = Integer.parseInt((String) params.get("page"));
        Integer limit = Integer.parseInt((String)params.get("limit"));
        return ResultGenerator.genSuccessResult(linkService.getLinkPage(page, limit));
    }

    @ApiOperation("友链添加")
    @PostMapping("/links/save")
    @ResponseBody
    public Result save(Integer linkType, String linkName, String linkUrl, Integer linkRank, String linkDescription) {
        if (linkType == null || linkType < 0 || linkRank == null || linkRank < 0 || !StringUtils.hasText(linkName) || !StringUtils.hasText(linkName) || !StringUtils.hasText(linkUrl) || !StringUtils.hasText(linkDescription)) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Link link = new Link();
        link.setLinkType(linkType);
        link.setLinkName(linkName);
        link.setLinkUrl(linkUrl);
        link.setLinkRank(linkRank);
        link.setLinkDescription(linkDescription);
        return ResultGenerator.genSuccessResult(linkService.save(link));
    }


    @ApiOperation("友链修改")
    @PostMapping("/links/update")
    @ResponseBody
    public Result update(Integer linkId, Integer linkType, String linkName, String linkUrl, Integer linkRank, String linkDescription) {
        if (linkType == null || linkType < 0 || linkRank == null || linkRank < 0 || !StringUtils.hasText(linkName) || !StringUtils.hasText(linkName) || !StringUtils.hasText(linkUrl) || !StringUtils.hasText(linkDescription)) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Link link = new Link();
        link.setLinkId(linkId);
        link.setLinkType(linkType);
        link.setLinkName(linkName);
        link.setLinkUrl(linkUrl);
        link.setLinkRank(linkRank);
        link.setLinkDescription(linkDescription);
        return ResultGenerator.genSuccessResult(linkService.update(link, new QueryWrapper<Link>().eq("link_id", link.getLinkId())));
    }


    @ApiOperation("友链删除")
    @Transactional
    @PostMapping("/links/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        boolean link_id = linkService.remove(new QueryWrapper<Link>().in("link_id", ids));
        if (link_id) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }





}
