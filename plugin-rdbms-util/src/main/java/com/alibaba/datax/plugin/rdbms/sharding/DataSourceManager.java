package com.alibaba.datax.plugin.rdbms.sharding;

import com.alibaba.datax.common.util.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;

/**
 * @author TimFruit
 * @date 20-4-22 下午9:29
 */
public class DataSourceManager {
    private static final Logger LOG = LoggerFactory
            .getLogger(DataSourceManager.class);
    private static volatile DataSource dataSource;


    public static void initDataSource(Configuration configuration){
        if(dataSource==null){
            synchronized (DataSourceManager.class){
                if(dataSource==null){
                     doInitDataSource(configuration);
                }
            }
        }
    }


    private static void doInitDataSource(Configuration configuration){
        dataSource=ShardingJdbcInitializer.initShardingDataSource(configuration);
    }

    //有可能为null
    public static DataSource getDataSource(){
        return dataSource;
    }



    public static void closeDataSource(){
        if(dataSource!=null && dataSource instanceof AutoCloseable){
            try {
                LOG.info("关闭数据源...");
                ((AutoCloseable)dataSource).close();
                return;
            } catch (Exception e) {
                throw new RuntimeException("关闭数据源出现异常", e);
            }
        }

        if(dataSource!=null && dataSource instanceof Closeable){
            try {
                LOG.info("关闭数据源...");
                ((Closeable)dataSource).close();
                return;
            } catch (Exception e) {
                throw new RuntimeException("关闭数据源出现异常", e);
            }
        }
    }


}
