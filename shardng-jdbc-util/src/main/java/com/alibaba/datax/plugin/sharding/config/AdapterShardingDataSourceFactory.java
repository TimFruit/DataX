package com.alibaba.datax.plugin.sharding.config;

import com.alibaba.canal.otter.canal.client.adapter.config.YmlConfigBinder;
import com.alibaba.datax.plugin.sharding.util.DataSourceUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.core.yaml.swapper.impl.EncryptRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.core.yaml.swapper.impl.MasterSlaveRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.core.yaml.swapper.impl.ShardingRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.shardingjdbc.api.EncryptDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TimFruit
 * @date 20-2-14 下午10:12
 */
public class AdapterShardingDataSourceFactory {

    private static Logger logger = LoggerFactory.getLogger(AdapterShardingDataSourceFactory.class);
    //使用spring boot 配置格式, 对应属性存在前缀"spring.shardingsphere"


    //sharding-jdbc-spring-boot-starter-4.0.0-RC2.jar

    //前缀"spring.shardingsphere.encrypt" 对应 YamlEncryptRuleConfiguration
    //前缀"spring.shardingsphere.masterslave" 对应 YamlMasterSlaveRuleConfiguration
    //前缀"spring.shardingsphere.sharding" 对应 YamlShardingRuleConfiguration

    //使用入口
    public static DataSource newInstance(File configDir) throws SQLException, ReflectiveOperationException {
        File yamlFile=ShardingConfigLoader.loadYamlFile(configDir);


        SpringShardingConfiguration config=resolveYamlConfiguration(yamlFile);


        Map<String,DataSource> dataSourceMap=config.getDatasource().getDataSources();


        DataSource dataSource=null;
        if(config.getEncrypt()!=null){
            dataSource=EncryptDataSourceFactory.createDataSource(dataSourceMap.values().iterator().next(), new EncryptRuleConfigurationYamlSwapper().swap(config.getEncrypt()), config.getProps());
        }else if(config.getMasterslave()!=null){
            dataSource=MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,new MasterSlaveRuleConfigurationYamlSwapper().swap(config.getMasterslave()), config.getProps());
        }else if(config.getSharding()!=null){
            dataSource=ShardingDataSourceFactory.createDataSource(dataSourceMap, new ShardingRuleConfigurationYamlSwapper().swap(config.getSharding()), config.getProps());
        }else {
            throw new RuntimeException("can not create datasource without config");
        }


        return dataSource;

    }



//
//    public static void main(String[] args) throws SQLException, ReflectiveOperationException, IOException {
//        newInstance();
//    }




    private static SpringShardingConfiguration resolveYamlConfiguration(File yamlFile) throws ReflectiveOperationException {

        String content=null;
        try {
            content=FileUtils.readFileToString(yamlFile);
        } catch (IOException e) {
            throw new RuntimeException("something wrong when read yaml file fore sharding datasource", e);
        }

        SpringShardingConfiguration config=YmlConfigBinder.bindYmlToObj(
                "spring.shardingsphere", content, SpringShardingConfiguration.class);


        String dataSourcePrefix="spring.shardingsphere.datasource.";
        Map<String, DataSource> dataSourceMap=new LinkedHashMap<>();
        List<String> names=config.getDatasource().getNames();
        for(String name: names){
            String prefix=dataSourcePrefix+name;
            DatasourceModel datasourceProps=YmlConfigBinder.bindYmlToObj(prefix, content, DatasourceModel.class);

            Object dataSourceType=datasourceProps.get("type");
            String dataSourceTypeStr= dataSourceType==null? null:dataSourceType.toString().trim();

            if(StringUtils.isEmpty(dataSourceTypeStr)){
                throw new RuntimeException(prefix+".type has not value");
            }


            DataSource dataSource=DataSourceUtil.getDataSource(((String)dataSourceType).trim(), datasourceProps);
            dataSourceMap.put(name, dataSource);

        }

        config.getDatasource().setDataSources(dataSourceMap);

        return config;
    }

    public static class DatasourceModel extends LinkedHashMap<String, Object>{

    }



}
