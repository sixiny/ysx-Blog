package com.ysx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysx.pojo.Config;
import com.ysx.service.ConfigService;
import com.ysx.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author YSX
* @description 针对表【tb_config】的数据库操作Service实现
* @createDate 2023-03-16 11:33:22
*/
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config>
    implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public int updateConfigByName(String configName, String configValue) {
        Config config = configMapper.selectById(configName);
        if (config == null){
            return 0;
        }
        config.setConfigValue(configValue);
        config.setUpdateTime(new Date());
        return configMapper.updateById(config);
    }

    @Override
    public Map<String, String> getAllConfigs() {
        HashMap<String, String> ans = new HashMap<>();
        List<Config> configs = configMapper.selectList(null);
        configs.stream().forEach(e->{
            ans.put(e.getConfigName(), e.getConfigValue());
        });
        return ans;
    }
}




