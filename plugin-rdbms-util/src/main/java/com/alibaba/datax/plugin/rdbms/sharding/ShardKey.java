package com.alibaba.datax.plugin.rdbms.sharding;

/**
 * @author TimFruit
 * @date 20-4-22 下午9:38
 */
public interface ShardKey {
    //json 配置属性

    /**
     * @see ShardConstant#SHARDING_JDBC
     */
    String SHARDING_TYPE ="shardingType";



    String SHARDING_CONFIG_PATH ="shardingConfigPath";




}
