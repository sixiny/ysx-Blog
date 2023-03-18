package com.ysx.controllers.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ysx.pojo.Config;
import com.ysx.service.ConfigService;
import com.ysx.utils.Result;
import com.ysx.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ysx
 * @Date: 2023/03/17/15:16
 * @Description: 配置处理器
 */
@Controller
@RequestMapping("/admin")
public class ConfigurationController {

    @Autowired
    private ConfigService configService;

    @ApiOperation("侧边栏路由")
    @GetMapping("/configurations")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "configurations");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "admin/configuration";
    }


    @ApiOperation("修改配置信息")
    @Transactional
    @PostMapping("/configurations/website")
    @ResponseBody
    public Result website(@RequestParam(value = "websiteName", required = false) String websiteName,
                          @RequestParam(value = "websiteDescription", required = false) String websiteDescription,
                          @RequestParam(value = "websiteLogo", required = false) String websiteLogo,
                          @RequestParam(value = "websiteIcon", required = false) String websiteIcon) {
        int updateNum = 0;
        if (StringUtils.hasText(websiteName)){
            updateNum += configService.updateConfigByName("websiteName", websiteName);
        }
        if (StringUtils.hasText(websiteDescription)) {
            updateNum += configService.updateConfigByName("websiteDescription", websiteDescription);
        }
        if (StringUtils.hasText(websiteLogo)) {
            updateNum += configService.updateConfigByName("websiteLogo", websiteLogo);
        }
        if (StringUtils.hasText(websiteIcon)) {
            updateNum += configService.updateConfigByName("websiteIcon", websiteIcon);
        }
        return ResultGenerator.genSuccessResult(updateNum > 0);
    }

    @ApiOperation("修改用户相关")
    @Transactional
    @PostMapping("/configurations/userInfo")
    @ResponseBody
    public Result userInfo(@RequestParam(value = "yourAvatar", required = false) String yourAvatar,
                           @RequestParam(value = "yourName", required = false) String yourName,
                           @RequestParam(value = "yourEmail", required = false) String yourEmail) {
        int updateResult = 0;
        if (StringUtils.hasText(yourAvatar)) {
            updateResult += configService.updateConfigByName("yourAvatar", yourAvatar);
        }
        if (StringUtils.hasText(yourName)) {
            updateResult += configService.updateConfigByName("yourName", yourName);
        }
        if (StringUtils.hasText(yourEmail)) {
            updateResult += configService.updateConfigByName("yourEmail", yourEmail);
        }
        return ResultGenerator.genSuccessResult(updateResult > 0);
    }

    @ApiOperation("修改脚注")
    @Transactional
    @PostMapping("/configurations/footer")
    @ResponseBody
    public Result footer(@RequestParam(value = "footerAbout", required = false) String footerAbout,
                         @RequestParam(value = "footerICP", required = false) String footerICP,
                         @RequestParam(value = "footerCopyRight", required = false) String footerCopyRight,
                         @RequestParam(value = "footerPoweredBy", required = false) String footerPoweredBy,
                         @RequestParam(value = "footerPoweredByURL", required = false) String footerPoweredByURL) {
        int updateResult = 0;
        if (StringUtils.hasText(footerAbout)) {
            updateResult += configService.updateConfigByName("footerAbout", footerAbout);
        }
        if (StringUtils.hasText(footerICP)) {
            updateResult += configService.updateConfigByName("footerICP", footerICP);
        }
        if (StringUtils.hasText(footerCopyRight)) {
            updateResult += configService.updateConfigByName("footerCopyRight", footerCopyRight);
        }
        if (StringUtils.hasText(footerPoweredBy)) {
            updateResult += configService.updateConfigByName("footerPoweredBy", footerPoweredBy);
        }
        if (StringUtils.hasText(footerPoweredByURL)) {
            updateResult += configService.updateConfigByName("footerPoweredByURL", footerPoweredByURL);
        }
        return ResultGenerator.genSuccessResult(updateResult > 0);
    }



}
