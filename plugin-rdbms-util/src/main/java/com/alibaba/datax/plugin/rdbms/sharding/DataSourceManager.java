package com.alibaba.datax.plugin.rdbms.sharding;

import com.alibaba.datax.common.util.Configuration;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;

/**
 * @author TimFruit
 * @date 20-4-22 下午9:29
 */
public class DataSourceManager {

    private static volatile DataSource dataSource;


    public static void initDataSource(Configuration configuration){
        dataSource=ShardingJdbcInitializer.initShardingDataSource(configuration);
    }

    //有可能为null
    public static DataSource getDataSource(){
        return dataSource;
    }



    public static void closeDataSource(){
        if(dataSource!=null && dataSource instanceof Closeable){
            try {
                ((Closeable)dataSource).close();
            } catch (IOException e) {
                throw new RuntimeException("关闭数据源出现异常", e);
            }
        }
    }


}
