<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.yfker.${ModuleName}.dao.mapper.${ModelName}Mapper">

    <!-- 通用查询结果列 -->
    <sql id="baseColumnList">
    <#list fields.fields as field>
        t.${field.fieldName} AS <@linetohump value="${field.fieldName}"/><#if field_has_next>,</#if>
    </#list>
    </sql>

    <!-- 使用对象类型传参 -->
    <select id="listPageByDto" parameterType="com.yfker.${ModuleName}.pojo.dto.${ModelName}QueryDto" resultType="com.yfker.${ModuleName}.pojo.vo.${ModelName}ListVo">
        SELECT <include refid="baseColumnList" /> FROM ${tableName} t where t.f_isdeleted=0
        <if test="dto != null">
            <!-- 这里写自己的条件拼装 -->

        </if>
        ORDER BY t.f_update_time DESC
    </select>

</mapper>