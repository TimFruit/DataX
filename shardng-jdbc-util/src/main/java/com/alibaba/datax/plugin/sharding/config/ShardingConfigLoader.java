package com.alibaba.datax.plugin.sharding.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author TimFruit
 * @date 20-2-14 下午10:06
 */
public class ShardingConfigLoader {

    private static Logger logger = LoggerFactory.getLogger(ShardingConfigLoader.class);
//    private final static String configDir="rdb-sharding";




    public static File loadYamlFile(File configDirFile){
//        File configDirFile=Util.getConfDirPath(configDir);

        String yaml="yaml";
        String yml="yml";

        File[] yamlFiles=configDirFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(yaml)||name.endsWith(yml);
            }
        });


        if(yamlFiles==null || yamlFiles.length==0){
            throw new RuntimeException("Can not find yaml File in location '"+configDirFile.getAbsolutePath()+"'");
        }

        if(yamlFiles.length>1){
            logger.warn("found {} yaml files in '{}', use the first one", yamlFiles.length, configDirFile.getName());


            yamlFiles=sortFileByName(yamlFiles);
        }

        File yarmlFile=yamlFiles[0];
        logger.info(" the rdb-sharding yaml file location is '{}' ", yarmlFile.getAbsoluteFile() );

        return yarmlFile;

    }



    private  static File[] sortFileByName(File[] files){
        List<File> fileList=Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                return file1.getName().compareTo(file2.getName());
            }
        });

        fileList.toArray(files);

        return files;
    }



}
