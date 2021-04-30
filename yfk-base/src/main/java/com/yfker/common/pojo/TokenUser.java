package com.yfker.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: token 解析的对象
 * @author: lijiayu
 * @date: 2020-03-18 19:58
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenUser {

    /**
     * 用户Id
     */
    private Long id;

    /**
     * 机构Id
     */
    private Long orgid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 是否机构负责人（领导），0:否，1:是
     */
    private Boolean isLeader;

}
