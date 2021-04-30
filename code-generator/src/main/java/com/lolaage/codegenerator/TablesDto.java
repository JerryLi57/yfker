package com.lolaage.codegenerator;

import java.util.List;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-03-06 17:41
 **/
public class TablesDto extends BaseDto {

    private static final long serialVersionUID = -5505686808979964428L;

    private String tableName;

    private String tableComment;

    private List<FiledsDto> fields;

    public List<FiledsDto> getFields() {
        return fields;
    }

    public void setFields(List<FiledsDto> fields) {
        this.fields = fields;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getBuzName() {
        String tableName = this.getTableName();
        String[] arr = tableName.split("_");
        String buzName = "";
        for (String str : arr) {
            if ("t".equals(str)) {
                continue;
            }
            buzName = buzName + str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return buzName;
    }

    public String getPoName() {
        return this.getTableName();
    }

    public String getVoName() {
        return getBuzName() + "Vo";
    }

    public String getMapperName() {
        return getBuzName() + "Mapper";
    }

    public String getIdaoName() {
        return "I" + getBuzName() + "Dao";
    }

    public String getDaoImplName() {
        return getBuzName() + "DaoImpl";
    }

    public String getIserviceName() {
        return "I" + getBuzName() + "Service";
    }

    public String getServiceImplName() {
        return getBuzName() + "ServiceImpl";
    }


}
