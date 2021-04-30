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
import com.yfker.common.valid.group.AddGroup;
import com.yfker.common.valid.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
@ApiModel(value = "${ModelName}EditDto", description = "${tableComment}")
public class ${ModelName}EditDto {

<#list fields.fields as field>
    <#if field.filed.javaType == 'LocalDateTime'>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    <#if field.filed.javaType == 'LocalDate'>
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    </#if>
    <#if field.fieldName =='f_id'>
    @Null(message = "主键必须为空", groups = AddGroup.class)
    @NotNull(message = "主键不能为空", groups = UpdateGroup.class)
    <#elseif  field.required == 'true' && field.filed.javaType=='String'>
    @NotBlank(message = "${field.fieldComment}不能为空", groups = {AddGroup.class, UpdateGroup.class})
    <#elseif field.required == 'true' && field.filed.javaType!='String'>
    @NotNull(message = "${field.fieldComment}不能为空", groups = {AddGroup.class, UpdateGroup.class})
    </#if>
    @ApiModelProperty(value = "${field.fieldComment}", name = "<@linetohump value="${field.fieldName}" />")
    private ${field.filed.javaType} <@linetohump value="${field.fieldName}" />;

</#list>
}