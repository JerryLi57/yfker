package com.yfker.server.pojo.model;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * @description: 病人注射历史记录 Model
 * @author: lijiayu
 * @date: 2021-04-29 11:39:29
 */
@Data
@TableName("t_paitent")
public class Paitent {

    /**
     * 主键
     */
    @TableId(value = "f_id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @TableField("f_username")
    private String username;

    /**
     * 性别 1:男 2:女
     */
    @TableField("f_gender")
    private Boolean gender;

    /**
     * 身份证号码
     */
    @TableField("f_id_card_no")
    private String idCardNo;

    /**
     * 手机号
     */
    @TableField("f_phone_number")
    private String phoneNumber;

    /**
     * 出生年月日
     */
    @TableField("f_birthday")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;

    /**
     * 是否被删除 0：未删除 1：已删除
     */
    @TableField("f_isdeleted")
    private Boolean isdeleted;

    /**
     * 修改时间
     */
    @TableField("f_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField("f_create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


}
