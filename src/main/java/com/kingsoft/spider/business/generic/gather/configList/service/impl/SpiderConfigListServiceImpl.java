package com.kingsoft.spider.business.generic.gather.configList.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.kingsoft.spider.business.generic.gather.configList.dto.SpiderConfigDatabaseDto;
import com.kingsoft.spider.business.generic.gather.configList.dto.SpiderConfigSaveDto;
import com.kingsoft.spider.business.spidercore.SpiderBoot;
import com.kingsoft.spider.business.spidercore.common.SpiderConfigInfoDto;
import com.kingsoft.spider.business.spidercore.common.SpiderConfigInfoTransition;
import com.kingsoft.spider.business.generic.gather.configList.dto.SpiderConfigListDto;
import com.kingsoft.spider.business.generic.gather.configList.mapper.SpiderConfigListMapper;
import com.kingsoft.spider.business.generic.gather.configList.service.SpiderConfigListService;
import com.kingsoft.spider.business.spidercore.common.TestPipelineDto;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wangyujie on 2018/1/30.
 */
@Service
public class SpiderConfigListServiceImpl implements SpiderConfigListService {
    @Autowired
    private SpiderConfigListMapper spiderConfigListMapper;


    @Override
    public List<SpiderConfigListDto> getList() {
        return spiderConfigListMapper.getList();
    }

    @Override
    public SpiderConfigInfoDto getEditList(Long id) {
        SpiderConfigInfoTransition transition=new SpiderConfigInfoTransition();
        return  transition.switchToDto(spiderConfigListMapper.getEditList(id));
    }

    @Override
    public void delete(Long id) {
        spiderConfigListMapper.delete(id);
    }

    @Override
    public void update(SpiderConfigSaveDto spiderConfigEntity) {
        spiderConfigListMapper.update(spiderConfigEntity);
    }

    @Override
    public List<TestPipelineDto> testRun(SpiderConfigInfoDto entity) {
        SpiderBoot spiderBoot=new SpiderBoot();
        return spiderBoot.runTestResult(entity);
    }

    @Override
    public boolean testDataBase(SpiderConfigDatabaseDto databaseDto) {
        boolean flag=false;
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://"+databaseDto.getDbAddress()+"/"+databaseDto.getName()+"?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&allowMultiQueries=true";
        String user=databaseDto.getUser();
        String password=databaseDto.getPassword();
        Connection connection=null;
        try {
            Class.forName(driver);
            connection= DriverManager.getConnection(url,user,password);
            flag=true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection!=null)
            {
                try {
                    DbUtils.close(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }
}
