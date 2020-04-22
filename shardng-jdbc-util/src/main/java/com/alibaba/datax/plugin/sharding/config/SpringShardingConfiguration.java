package com.alibaba.datax.plugin.sharding.config;

import org.apache.shardingsphere.core.yaml.config.encrypt.YamlEncryptRuleConfiguration;
import org.apache.shardingsphere.core.yaml.config.masterslave.YamlMasterSlaveRuleConfiguration;
import org.apache.shardingsphere.core.yaml.config.sharding.YamlShardingRuleConfiguration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author TimFruit
 * @date 20-2-15 上午12:30
 */
public class SpringShardingConfiguration {
    //前缀"spring.shardingsphere.encrypt" 对应 YamlEncryptRuleConfiguration
    //前缀"spring.shardingsphere.masterslave" 对应 YamlMasterSlaveRuleConfiguration
    //前缀"spring.shardingsphere.sharding" 对应 YamlShardingRuleConfiguration

    private YamlEncryptRuleConfiguration encrypt;

    private YamlMasterSlaveRuleConfiguration masterslave;


    private YamlShardingRuleConfiguration sharding;

    private DatasourceDto datasource;

    private Properties props = new Properties();


    public YamlEncryptRuleConfiguration getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(YamlEncryptRuleConfiguration encrypt) {
        this.encrypt = encrypt;
    }

    public YamlMasterSlaveRuleConfiguration getMasterslave() {
        return masterslave;
    }

    public void setMasterslave(YamlMasterSlaveRuleConfiguration masterslave) {
        this.masterslave = masterslave;
    }

    public YamlShardingRuleConfiguration getSharding() {
        return sharding;
    }

    public void setSharding(YamlShardingRuleConfiguration sharding) {
        this.sharding = sharding;
    }

    public DatasourceDto getDatasource() {
        return datasource;
    }

    public void setDatasource(DatasourceDto datasource) {
        this.datasource = datasource;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }


    public static class DatasourceDto{
        private List<String> names;
        private Map<String, DataSource> dataSources = new HashMap();

        public List<String> getNames() {
            return names;
        }

        public void setNames(List<String> names) {
            this.names = names;
        }

        public Map<String, DataSource> getDataSources() {
            return dataSources;
        }

        public void setDataSources(Map<String, DataSource> dataSources) {
            this.dataSources = dataSources;
        }
    }
}


