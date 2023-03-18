package com.ysx.service;

import com.ysx.pojo.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author YSX
* @description 针对表【tb_admin_user】的数据库操作Service
* @createDate 2023-03-16 11:33:22
*/
public interface AdminUserService extends IService<AdminUser> {

    boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);

}
