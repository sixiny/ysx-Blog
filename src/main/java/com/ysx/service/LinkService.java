package com.ysx.service;

import com.ysx.pojo.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ysx.utils.PageResult;

/**
* @author YSX
* @description 针对表【tb_link】的数据库操作Service
* @createDate 2023-03-16 11:33:22
*/
public interface LinkService extends IService<Link> {

    PageResult getLinkPage(int page, int limit);

}
