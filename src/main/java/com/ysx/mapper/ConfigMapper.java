package com.ysx.mapper;

import com.ysx.pojo.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author YSX
* @description 针对表【tb_config】的数据库操作Mapper
* @createDate 2023-03-16 11:33:22
* @Entity com.ysx.pojo.TbConfig
*/
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {


}




