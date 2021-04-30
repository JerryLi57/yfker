package com.yfker.common.pojo;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @description: 参数校验Dto
 * @author: lijiayu
 * @date: 2020-05-15 18:48
 **/
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class ValidParamsDto {

    /**
     * 校验的结果
     */
    @NonNull
    private boolean allowed;

    /**
     * 校验的信息
     */
    private String msg;
}
