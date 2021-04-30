package com.yfker.common.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * @description: SuperMapper
 * @author: lijiayu
 * @date: 2020-03-05 20:14
 **/
public interface SuperMapper<T> extends BaseMapper<T> {

    /**
     * 使用对象类型传参
     *
     * @param page IPage 对象
     * @param dto  自定义参数对象
     * @return
     */
    <D> IPage<D> listDtoPageByObj(IPage<T> page, @Param("dto") D dto);

}
