package com.lolaage.codegenerator;

import lombok.Data;

/**
 * @description: TemplateVal
 * @author: lijiayu
 * @date: 2020-03-06 17:12
 **/
@Data
public class TemplateVal {

    private String modelName;

    /**
     * 第一个字母大写的实体名称
     */
    private String modelNameLower;

    private String tableName;

    private String tableComment;

    private String moduleName;

    private String author;

    private String crateDate;

    private String serialVersionUid;

    private TablesDto tablesDto;

    private String mgnModule;

    private String pageModule;
}
