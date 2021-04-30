package com.yfker.${ModuleName}.pojo.dto;

<#assign hasdatetime=0>
<#assign hasdate=0>
<#assign hasjson=0>
<#list fields.fields as field>
    <#if field.filed.javaType == 'LocalDateTime'>
        <#assign hasdatetime=1>
        <#assign hasjson=1>
    </#if>
    <#if field.filed.javaType == 'LocalDate'>
        <#assign hasdate=1>
        <#assign hasjson=1>
    </#if>
</#list>
<#if hasjson==1>
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
import com.yfker.common.pojo.PageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

<#if hasdate==1>
import java.time.LocalDate;
</#if>
<#if hasdatetime==1>
import java.time.LocalDateTime;
</#if>

/**
 * @description: ${tableComment} Dto
 * @author: ${author}
 * @date: ${crateDate}
 */
@Data
@ApiModel(value = "${ModelName}QueryDto", description = "${tableComment} QueryDto")
public class ${ModelName}QueryDto extends PageDto {

<#list fields.fields as field>
    <#if field.filed.javaType == 'LocalDateTime'>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    <#if field.filed.javaType == 'LocalDate'>
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    </#if>
    @ApiModelProperty(value = "${field.fieldComment}", name = "<@linetohump value="${field.fieldName}" />")
    private ${field.filed.javaType} <@linetohump value="${field.fieldName}" />;

</#list>
}