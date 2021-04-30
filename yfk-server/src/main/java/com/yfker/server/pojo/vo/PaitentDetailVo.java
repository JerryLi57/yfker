package com.yfker.server.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @description: 病人注射历史记录 Dto
 * @author: lijiayu
 * @date: 2021-04-29 11:36:37
 */
@Data
@ApiModel(value = "PaitentDetailVo", description = "病人注射历史记录")
public class PaitentDetailVo {

    @ApiModelProperty(value = "主键", name = "id")
    private Long id;

    @ApiModelProperty(value = "姓名", name = "username")
    private String username;

    @ApiModelProperty(value = "性别 1:男 2:女", name = "gender")
    private Boolean gender;

    @ApiModelProperty(value = "身份证号码", name = "idCardNo")
    private String idCardNo;

    @ApiModelProperty(value = "手机号", name = "phoneNumber")
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "出生年月日", name = "birthday")
    private LocalDate birthday;

}