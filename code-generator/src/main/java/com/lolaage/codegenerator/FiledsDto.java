package com.lolaage.codegenerator;

import lombok.Data;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-03-06 17:41
 **/
@Data
public class FiledsDto extends BaseDto {

    private String fieldName;

    private String fieldComment;

    private CloumnEnums filed;

    private String javaType;

    private String required;

}
