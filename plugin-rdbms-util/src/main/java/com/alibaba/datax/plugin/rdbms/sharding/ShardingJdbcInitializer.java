package com.alibaba.datax.plugin.rdbms.sharding;

import com.alibaba.datax.common.constant.CommonConstant;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.sharding.config.AdapterShardingDataSourceFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;

/**
 * 初始化sharding-jdbc
 * @author TimFruit
 * @date 20-4-22 下午9:34
 */
public class ShardingJdbcInitializer {
    private static final Logger LOG = LoggerFactory
            .getLogger(ShardingJdbcInitializer.class);


    private static final String DATAX_HOME=System.getProperty("datax.home");

    public static DataSource initShardingDataSource(Configuration config){
        String shardType=config.getString(ShardKey.SHARDING_TYPE);

        if(StringUtils.isEmpty(shardType)){
            LOG.debug("属性[{}]值为空", ShardKey.SHARDING_TYPE);
            return null;
        }
        if(!ShardConstant.SHARDING_JDBC.equals(shardType)){
            String message="["+ShardKey.SHARDING_TYPE+"]类型属性配置不正确，值需要为["+ShardConstant.SHARDING_JDBC+"]";
            throw new RuntimeException(message);
        }

        String configPath=config.getString(ShardKey.SHARDING_CONFIG_PATH);
        if(StringUtils.isEmpty(configPath)){
            String message="["+ShardKey.SHARDING_CONFIG_PATH+"]类型属性值为空";
            throw new RuntimeException(message);
        }

        LOG.info("["+ShardKey.SHARDING_CONFIG_PATH+"]值为{}",configPath);


        File configDir;
        if(configPath.startsWith("/")){
            configDir=new File(configPath);
        }else {
            configDir=new File(DATAX_HOME+File.separator+configPath);
        }
        LOG.info("sharding配置路径:{}",configDir.getAbsolutePath());


        try {
            DataSource dataSource=AdapterShardingDataSourceFactory.newInstance(configDir);
            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
