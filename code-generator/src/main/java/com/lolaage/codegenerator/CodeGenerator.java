package com.lolaage.codegenerator;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.setting.dialect.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @description: 自动代码生成器
 * @author: lijiayu
 * @date: 2020-03-06 17:41
 **/
public class CodeGenerator {

    static FreemarkerUtil freemarkerUtil = new FreemarkerUtil();

    static Logger log = LoggerFactory.getLogger(CodeGenerator.class);

    static String[] filterFields = {"f_isdeleted", "f_update_time", "f_create_time"};

    public static boolean filterForVo(String field) {
        List<String> list = Arrays.asList(filterFields);
        return list.contains(field);
    }

    public static String getPropValue(String key) {
        Props prop = new Props("generator.properties");
        return prop.getStr(key);
    }

    /**
     * 构造表模型实体类
     * @param val
     */
    static void buildModel(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        TablesDto tablesDto = val.getTablesDto();
        root.put("fields", tablesDto);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");

        freemarkerUtil.fprint("Model.ftl", root, val.getModelName() + ".java", "pojo/model");
    }


    /**
     * Dto构造
     * @param val
     */
    static void buildDetailDto(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        TablesDto tablesDto = val.getTablesDto();
        TablesDto tablesDto2 = new TablesDto();
        BeanUtil.copyProperties(tablesDto, tablesDto2);
        List<FiledsDto> fields = tablesDto.getFields().stream().filter(e -> (!filterForVo(e.getFieldName()))).collect(Collectors.toList());
        tablesDto2.setFields(fields);
        root.put("fields", tablesDto2);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");

        freemarkerUtil.fprint("DetailVo.ftl", root, val.getModelName() + "DetailVo.java", "pojo/vo");
    }

    static void buildEditDto(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        TablesDto tablesDto = val.getTablesDto();
        TablesDto tablesDto2 = new TablesDto();
        BeanUtil.copyProperties(tablesDto, tablesDto2);
        List<FiledsDto> fields = tablesDto.getFields().stream().filter(e -> (!filterForVo(e.getFieldName()))).collect(Collectors.toList());
        tablesDto2.setFields(fields);
        root.put("fields", tablesDto2);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");

        freemarkerUtil.fprint("EditDto.ftl", root, val.getModelName() + "EditDto.java", "pojo/dto");
    }

    static void buildQueryDto(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        TablesDto tablesDto = val.getTablesDto();
        TablesDto tablesDto2 = new TablesDto();
        BeanUtil.copyProperties(tablesDto, tablesDto2);
        List<FiledsDto> fields = tablesDto.getFields().stream().filter(e -> (!filterForVo(e.getFieldName()))).collect(Collectors.toList());
        tablesDto2.setFields(fields);
        root.put("fields", tablesDto2);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");

        freemarkerUtil.fprint("QueryDto.ftl", root, val.getModelName() + "QueryDto.java", "pojo/dto");
    }

    static void buildListDto(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        TablesDto tablesDto = val.getTablesDto();
        FiledsDto ff = tablesDto.getFields().get(1);
        root.put("fields", tablesDto);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");

        freemarkerUtil.fprint("ListVo.ftl", root, val.getModelName() + "ListVo.java", "pojo/vo");
    }

    /**
     * Mapper构造
     * @param val
     */
    static void buildMapper(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("ModelNameLower", val.getModelNameLower());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        TablesDto tablesDto = val.getTablesDto();
        root.put("fields", tablesDto);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");

        freemarkerUtil.fprint("Mapper.ftl", root, val.getModelName() + "Mapper.java", "dao/mapper");
    }


    /**
     * Mapper构造
     * @param val
     */
    static void buildMapperXml(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("ModelNameLower", val.getModelNameLower());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        TablesDto tablesDto = val.getTablesDto();
        root.put("fields", tablesDto);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");
        root.put("c_status", "#{c_status}");
        root.put("c_startTime", "#{c_startTime}");
        root.put("c_endTime", "#{c_endTime}");
        freemarkerUtil.fprint("MapperXml.ftl", root, val.getModelName() + "Mapper.xml", "xml");
    }


    /**
     * IService构造
     * @param val
     */
    static void buildIservice(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("ModelNameLower", val.getModelNameLower());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        TablesDto tablesDto = val.getTablesDto();
        root.put("fields", tablesDto);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");

        freemarkerUtil.fprint("IService.ftl", root, "I" + val.getModelName() + "Service.java", "service");
    }

    /**
     * Service Impl构造
     * @param val
     */
    static void buildServiceImpl(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("ModelNameLower", val.getModelNameLower());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        TablesDto tablesDto = val.getTablesDto();
        root.put("fields", tablesDto);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");

        freemarkerUtil.fprint("ServiceImpl.ftl", root, val.getModelName() + "ServiceImpl.java", "service/impl");
    }

    /**
     * Controller构造
     * @param val
     */
    static void buildController(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("ModelNameLower", val.getModelNameLower());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        root.put("mgnModule", val.getMgnModule());
        root.put("pageModule", val.getPageModule());
        TablesDto tablesDto = val.getTablesDto();
        root.put("fields", tablesDto);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");
        freemarkerUtil.fprint("Controller.ftl", root, val.getModelName() + "Controller.java", "controller");
    }

    static void buildControllerTest(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("ModelNameLower", val.getModelNameLower());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        root.put("mgnModule", val.getMgnModule());
        root.put("pageModule", val.getPageModule());
        TablesDto tablesDto = val.getTablesDto();
        root.put("fields", tablesDto);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");
        freemarkerUtil.fprint("ControllerTest.ftl", root, val.getModelName() + "ControllerTest.java", "controllerTest");
    }

    /**
     * Controller构造
     * @param val
     */
    static void buildFeign(TemplateVal val) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("ModuleName", val.getModuleName());
        root.put("ModelName", val.getModelName());
        root.put("ModelNameLower", val.getModelNameLower());
        root.put("tableComment", val.getTableComment());
        root.put("author", val.getAuthor());
        root.put("crateDate", val.getCrateDate());
        root.put("tableName", val.getTableName());
        root.put("mgnModule", val.getMgnModule());
        root.put("pageModule", val.getPageModule());
        TablesDto tablesDto = val.getTablesDto();
        root.put("fields", tablesDto);
        root.put("serialVersionUID", System.currentTimeMillis() + "L");
        freemarkerUtil.fprint("feign.ftl", root, val.getModelName() + "FeignClient.java", "feign");
    }


    public static void buildCode() {
        DbUtils.init();
        Map<String, TablesDto> tables = DbUtils.getTablesDb();
        TemplateVal val = new TemplateVal();
        for (Entry<String, TablesDto> entry : tables.entrySet()) {
            log.info(entry.getValue().getTableName());
            val = new TemplateVal();
            val.setModuleName(GeneratorConf.PACKAGE);
            val.setModelName(entry.getValue().getBuzName());
            String firstNameLower = val.getModelName().substring(0, 1).toLowerCase() + val.getModelName().substring(1);
            val.setModelNameLower(firstNameLower);
            val.setTableComment(entry.getValue().getTableComment());
            val.setAuthor(GeneratorConf.AUTHOR);
            val.setCrateDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            val.setTableName(entry.getValue().getTableName());
            val.setTablesDto(entry.getValue());
            val.setMgnModule(getPropValue("MgnModule"));
            val.setPageModule(getPropValue("PageModule"));
            log.info(entry.getValue().getTableName());
            buildModel(val);
            buildDetailDto(val);
            buildEditDto(val);
            buildQueryDto(val);
            buildListDto(val);
            buildMapper(val);
            buildMapperXml(val);
            buildIservice(val);
            buildServiceImpl(val);
            buildController(val);
            buildControllerTest(val);
//            buildFeign(val);
        }
    }

    public static void main(String[] args) {
        buildCode();
    }

}
