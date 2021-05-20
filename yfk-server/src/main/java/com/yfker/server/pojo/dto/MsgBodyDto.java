package com.yfker.server.pojo.dto;

import lombok.Data;

/**
 * @description: 聊天记录表 Dto
 * @author: lijiayu
 * @date: 2020-10-13 09:58:27
 */
@Data
public class MsgBodyDto {

    /**
     * 消息格式如： {"date":1602748167770,"esn":"1","t":"1","c":"1"}
     *  data: 时间戳
     *  esn： Equipment serial number 设备编码
     *  t: type 消息类型
     *  c: code 消息代码
     */
    private String msg;

}