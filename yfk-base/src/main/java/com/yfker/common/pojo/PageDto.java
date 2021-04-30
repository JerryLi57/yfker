package com.yfker.common.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: PageInfo
 * @author: lijiayu
 * @date: 2020-03-07 13:34
 **/
@Data
@ApiModel(value = "分页对象", description = "分页对象")
public class PageDto {

    @ApiModelProperty(value = "总数", name = "total")
    private long total = 0;

    @ApiModelProperty(value = "每页显示条数，默认 20", name = "pageSize")
    private long pageSize = 20;

    @ApiModelProperty(value = "当前页", name = "currentPage")
    private long currentPage = 1;

    public PageDto(){}

    /**
     * SQL 排序 ASC 数组
     */
    private String[] ascs;
    /**
     * SQL 排序 DESC 数组
     */
    private String[] descs;


    public PageDto(long currentPage, long pageSize){
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

}
