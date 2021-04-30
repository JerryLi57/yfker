package com.yfker.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yfker.common.dao.mapper.SuperMapper;
import com.yfker.common.pojo.DataResult;
import com.yfker.common.pojo.PageDto;

import java.util.List;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-12-05 16:49
 **/
public interface IBaseService<T> extends IService<T> {

    <D> DataResult<List<D>> listDtoPageByObj(PageDto pageDto, D dto, SuperMapper mapper);

    /**
     * 根据实体参数返回一行记录
     * @param condition
     * @return
     */
    T getOne(T condition);

    /**
     * 根据实体参数返回列表数据
     * @param condition
     * @return
     */
    List<T> list(T condition);

    /**
     * 分页查询
     * @param page
     * @param condition
     * @return
     */
    List<T> listByPage(IPage<T> page, T condition);

    /**
     * 分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    List<T> listByPage(IPage<T> page, Wrapper<T> queryWrapper);

}