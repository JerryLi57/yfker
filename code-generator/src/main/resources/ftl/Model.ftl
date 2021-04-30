package com.yfker.${ModuleName}.pojo.model;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
<#assign hasdatetime=0>
<#assign hasdate=0>
<#list fields.fields as field>
    <#if field.filed.javaType == 'LocalDateTime'>
        <#assign hasdatetime=1>
    </#if>
    <#if field.filed.javaType == 'LocalDate'>
        <#assign hasdate=1>
    </#if>
</#list>
<#if hasdate==1>
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
</#if>
<#if hasdatetime==1>
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
</#if>

/**
 * @description: ${tableComment} Model
 * @author: ${author}
 * @date: ${crateDate}
 */
@Data
@TableName("${tableName}")
public class ${ModelName} {

    <#list fields.fields as field>
    <#if field.fieldComment?? && field.fieldComment!=''>
    /**
     * ${field.fieldComment}
     */
    <#else>
    /**
     *
     */
    </#if>
    <#if field.fieldName =='f_id'>
    @TableId(value = "f_id", type = IdType.AUTO)
    <#else>
    @TableField("${field.fieldName}")
    </#if>
    <#if field.filed.javaType == 'LocalDateTime'>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    <#if field.filed.javaType == 'LocalDate'>
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    </#if>
    private ${field.filed.javaType} <@linetohump value="${field.fieldName}" />;

    </#list>

}
