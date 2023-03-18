package com.ysx.mapper;

import com.ysx.pojo.Link;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author YSX
* @description 针对表【tb_link】的数据库操作Mapper
* @createDate 2023-03-16 11:33:22
* @Entity com.ysx.pojo.TbLink
*/
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}




