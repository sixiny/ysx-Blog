package com.ysx.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysx.pojo.AdminUser;
import com.ysx.service.AdminUserService;
import com.ysx.mapper.AdminUserMapper;
import com.ysx.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author YSX
* @description 针对表【tb_admin_user】的数据库操作Service实现
* @createDate 2023-03-16 11:33:22
*/
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser>
    implements AdminUserService {

    @Autowired
    private AdminUserMapper userMapper;

    @Override
    public boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        AdminUser adminUser = userMapper.selectById(loginUserId);
        if (!MD5Util.MD5Encode(originalPassword, "UTF-8").equals(adminUser.getLoginPassword())){
            return false;
        }
        adminUser.setLoginPassword(MD5Util.MD5Encode(newPassword, "UTF-8"));
        userMapper.update(adminUser, new UpdateWrapper<AdminUser>().eq("admin_user_id", adminUser.getAdminUserId()));
        return true;
    }
}




