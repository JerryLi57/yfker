package com.lolaage.codegenerator;

import cn.hutool.setting.dialect.Props;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-03-06 17:41
 **/
public class GeneratorConf implements Serializable {

    private static final long serialVersionUID = 4577177833736664043L;


    public static String getPropValue(String key) {
        Props prop = new Props("generator.properties");
        return prop.getStr(key);
    }

    public static String AUTHOR = getPropValue("AUTHOR");

    public static String PACKAGE = getPropValue("PACKAGE");


    public static String[] TABLES;

    public static Map<String, String> tablesMap() {
        Map<String, String> map = new HashMap<String, String>();
        TABLES = getPropValue("TABLES").split(",");
        for (String table : TABLES) {
            map.put(table, table);
        }
        return map;
    }


    /**
     * 数据库配置
     */
    public static class DbConf {


        public static String DBURL = getPropValue("DBURL");
        public static String DBNAME = getPropValue("DBNAME");


        public static String USER = getPropValue("USER");

        public static String PWD = getPropValue("PWD");

        public static String DRIVERCLASSNAME = getPropValue("DRIVERCLASSNAME");

    }

    public static class BuilderConf {

        //		public static String LOCALDIR = "/Users/lijiayu/Downloads/test/autoCode";
        public static String LOCALDIR = getPropValue("LOCALDIR");

    }


}
