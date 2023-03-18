package com.ysx.service;

import com.ysx.pojo.Config;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author YSX
* @description 针对表【tb_config】的数据库操作Service
* @createDate 2023-03-16 11:33:22
*/
public interface ConfigService extends IService<Config> {

    int updateConfigByName(String configName, String configValue);

    Map<String, String> getAllConfigs();
}
