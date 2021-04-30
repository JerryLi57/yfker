package com.yfker.common.pojo;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @description: 帮助计算分页的起始和结束位置的工具类型
 * @author: lijiayu
 * @date: 2020-03-05 17:15
 **/
public class PageHelper {

    public static <T> PageDto getPageDto(IPage<T> ipage) {
        PageDto page = new PageDto(ipage.getCurrent(), ipage.getSize());
        if (ipage.getTotal() > 0) {
            page.setTotal(ipage.getTotal());
        }
        return page;
    }

    /**
     * 计算分页开始的条数
     *
     * @return
     */
    public static Integer getStartIndex(Integer page, Integer rows) {
        Integer startIndex = null;
        validateParam(page, rows);
        startIndex = (page - 1) * rows;
        return startIndex;
    }

    /**
     * @param page 当前页
     * @param rows 显示的条数
     */
    private static void validateParam(Integer page, Integer rows) {
        if (page == null || page < 1) {
            throw new IllegalArgumentException("分页的当前页不能为空或者值小于1");
        }

        if (rows == null || rows < 1) {
            throw new IllegalArgumentException("显示的条数不能为空或者值小于1");
        }
    }


}
