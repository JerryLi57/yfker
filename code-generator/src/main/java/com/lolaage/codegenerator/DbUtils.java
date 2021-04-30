package com.lolaage.codegenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-03-06 17:41
 **/
public class DbUtils {

    private static final long serialVersionUID = -2598048238676523383L;

    private static Logger logger = LoggerFactory.getLogger(DbUtils.class);

    private static Map<String, TablesDto> TABLESDB = new HashMap<String, TablesDto>();

    public static void putTables(TablesDto tables) {
        TABLESDB.put(tables.getTableName(), tables);
    }

    public static Map<String, TablesDto> getTablesDb() {
        return TABLESDB;
    }

    private static Connection getConn() {
        String driver = GeneratorConf.DbConf.DRIVERCLASSNAME;
        String url = GeneratorConf.DbConf.DBURL;
        String username = GeneratorConf.DbConf.USER;
        String password = GeneratorConf.DbConf.PWD;
        Connection conn = null;
        try {
            Class.forName(driver); // classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    public static void showTables() {
        Connection conn = getConn();
        logger.info("获取数据库[{}]表集合开始", GeneratorConf.DbConf.DBNAME);
        try {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            TablesDto dto = null;
            int i = 1;
            Map<String, String> tables = GeneratorConf.tablesMap();
            while (rs.next()) {
                if (tables.get(rs.getString(3)) == null) {
                    continue;
                }
                dto = new TablesDto();
                logger.info("Tables : [{}] {}", i, rs.getString(3));
                dto.setTableName(rs.getString(3));
                putTables(dto);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("获取数据库[{}]表集合结束", GeneratorConf.DbConf.DBNAME);
    }

    public static void showTablesColumns() {
        Connection conn = getConn();
        for (Entry<String, TablesDto> entry : getTablesDb().entrySet()) {
            showSingleTablesColumns(GeneratorConf.DbConf.DBNAME, entry.getValue(), conn);
            comment(GeneratorConf.DbConf.DBNAME, entry.getValue(), conn);
        }
    }

    static void showSingleTablesColumns(String databaseName, TablesDto tablesDto, Connection conn) {
        try {
            String tableName = tablesDto.getTableName();
            logger.info("数据库[{}]表[{}]分析开始.", databaseName, tableName);
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet resultSet = meta.getColumns(databaseName, null, tableName, "%");
            List<FiledsDto> fields = new ArrayList<FiledsDto>();
            FiledsDto tempFiledsDto = null;
            while (resultSet.next()) {
                tempFiledsDto = new FiledsDto();
                tempFiledsDto.setFieldName(resultSet.getString(4));

                if (CloumnEnums.matchCloumnEnums(resultSet.getString("TYPE_NAME")) == null) {
                    logger.warn("数据库类型[{}]未在枚举类型匹配", resultSet.getString("TYPE_NAME"));
                }
                tempFiledsDto.setFiled(CloumnEnums.matchCloumnEnums(resultSet.getString("TYPE_NAME")));
                fields.add(tempFiledsDto);
                String nullableStr = resultSet.getString("NULLABLE");
                if("0".equals(nullableStr)){
                    tempFiledsDto.setRequired("true");
                }else{
                    tempFiledsDto.setRequired("false");
                }
                logger.info("列[{}]类型[{}]是否允许为空[{}]", resultSet.getString(4), resultSet.getString("TYPE_NAME"), resultSet.getString("NULLABLE"));
            }
            fullFieldComment(databaseName, tablesDto, conn, fields);
            tablesDto.setFields(fields);
            logger.info("数据库[{}]表[{}]分析结束.", databaseName, tableName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取表结构数据异常.");
        }
    }

    /**
     * 字段备注
     * @param databaseName
     * @param tablesDto
     * @param conn
     * @param fields
     */
    static void fullFieldComment(String databaseName, TablesDto tablesDto, Connection conn, List<FiledsDto> fields) {
        try {
            Statement stmt = conn.createStatement();
            String table = tablesDto.getTableName();
            ResultSet rs = stmt.executeQuery("show full columns from " + table);
            Map<String, String> map = new HashMap<String, String>();
            while (rs.next()) {
                map.put(rs.getString("Field"), rs.getString("Comment"));
                logger.info("Filed[{}]comment[{}]", rs.getString("Field"), rs.getString("Comment"));
            }
            for (FiledsDto field : fields) {
                field.setFieldComment(map.get(field.getFieldName()) + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void comment(String databaseName, TablesDto tablesDto, Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tablesDto.getTableName());
            if (rs != null && rs.next()) {
                String createDdl = rs.getString(2);
                String comment = parse(createDdl);
                tablesDto.setTableComment(comment);
                logger.info("TABLE[{}] COMMENT[{}]", tablesDto.getTableName(), comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回注释信息
     *
     * @param all
     * @return
     */

    static String parse(String all) {
        String comment = null;
        int index = all.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = all.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        return comment;
    }

    public static void init() {
        showTables();
        showTablesColumns();
    }

    public static void main(String[] args) {
        showTables();
        showTablesColumns();
    }


}
