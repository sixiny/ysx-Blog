package com.ysx.controllers.admin;

import cn.hutool.captcha.ShearCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ysx.pojo.AdminUser;
import com.ysx.pojo.BlogTag;
import com.ysx.service.*;
import com.ysx.utils.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ysx
 * @Date: 2023/03/16/13:49
 * @Description:
 */
@Api("管理员处理器")
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private BlogCategoryService categoryService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private BlogTagService tagService;

    @Autowired
    private BlogCommentService commentService;

    @ApiOperation("页面跳转控制")
    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    @ApiOperation("首页跳转控制")
    @GetMapping("/index")
    public String index(HttpServletRequest request){
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", categoryService.count());
        request.setAttribute("blogCount", blogService.count());
        request.setAttribute("linkCount", linkService.count());
        request.setAttribute("tagCount", tagService.count());
        request.setAttribute("commentCount", commentService.count());
        return "admin/index";
    }

    @ApiOperation("登录处理方法")
    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session){
        if (StringUtils.isEmpty(verifyCode)){
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        ShearCaptcha codevertify = (ShearCaptcha) session.getAttribute("verifyCode");
        if (!codevertify.verify(verifyCode)){
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }

        String password_md5 = MD5Util.MD5Encode(password, "UTF-8");
        AdminUser currentUser = adminUserService.getOne(new QueryWrapper<AdminUser>().eq("login_user_name", userName).eq("login_password", password_md5));
        if (currentUser == null){
            session.setAttribute("errorMsg", "没有此用户");
            return "admin/login";
        }else{
            session.setAttribute("loginUser", currentUser.getNickName());
            session.setAttribute("loginUserId", currentUser.getAdminUserId());
            //session过期时间设置为7200秒 即两小时
            //session.setMaxInactiveInterval(60 * 60 * 2);
            return "redirect:/admin/index";
        }
    }

    @ApiOperation("跳转修改用户名密码页")
    @GetMapping("/profile")
    public String profile(HttpServletRequest request ,HttpSession session){
        int loginUserId = (int) session.getAttribute("loginUserId");
        AdminUser currentUser = adminUserService.getById(loginUserId);
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", currentUser.getLoginUserName());
        request.setAttribute("nickName", currentUser.getNickName());
        return "admin/profile";
    }

    @ApiOperation("修改用户名")
    @PostMapping("/profile/name")
    @ResponseBody
    public String updateName(HttpServletRequest request, String loginUserName, String nickName)
    {
        if (!StringUtils.hasText(loginUserName) || !StringUtils.hasText(nickName)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser updateUser = new AdminUser();
        updateUser.setLoginUserName(loginUserName);
        updateUser.setNickName(nickName);
        if (adminUserService.update(updateUser, new QueryWrapper<AdminUser>().eq("admin_user_id", loginUserId))) {
            return "success";
        } else {
            return "修改失败";
        }
    }

    @ApiOperation("修改密码")
    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, String originalPassword,
                                 String newPassword) {

        if(StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)){
            return "密码没有输入";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if(adminUserService.updatePassword(loginUserId, originalPassword, newPassword)){
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        }
        return "修改失败";
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }



}
