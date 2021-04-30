package com.yfker.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfker.common.constant.ErrorEnum;
import com.yfker.common.dao.mapper.SuperMapper;
import com.yfker.common.pojo.DataResult;
import com.yfker.common.pojo.PageDto;
import com.yfker.common.pojo.PageHelper;
import com.yfker.common.service.IBaseService;
import com.yfker.common.service.common.CallService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-03-07 13:17
 **/
@Slf4j
public class BaseServiceImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

    @Override
    public <D> DataResult<List<D>> listDtoPageByObj(PageDto pageDto, D dto, SuperMapper mapper) {
        DataResult<List<D>> result = new DataResult<>();
        Page<D> page = new Page(pageDto.getCurrentPage(), pageDto.getPageSize(), true);
        page.setDesc(pageDto.getDescs());
        page.setAsc(pageDto.getAscs());
        IPage<D> ipage = mapper.listDtoPageByObj(page, dto);
        result.setPage(PageHelper.getPageDto(ipage));
        result.setData(ipage.getRecords());
        return result;
    }

    @Override
    public T getOne(T condition) {
        return super.getOne(new QueryWrapper<>(condition), false);
    }

    @Override
    public List<T> list(T condition) {
        return super.list(new QueryWrapper<>(condition));
    }

    @Override
    public List<T> listByPage(IPage<T> page, T condition) {
        if (page == null) {
            return super.list(new QueryWrapper<>(condition));
        }
        IPage<T> ret = super.page(page, new QueryWrapper<>(condition));
        page.setTotal(ret.getTotal());
        return ret.getRecords();
    }

    @Override
    public List<T> listByPage(IPage<T> page, Wrapper<T> queryWrapper) {
        if (page == null) {
            return super.list(queryWrapper);
        }
        IPage<T> ret = super.page(page, queryWrapper);
        page.setTotal(ret.getTotal());
        return ret.getRecords();
    }

    protected <T> T getResultData(DataResult<T> dr) {
        if (dr != null && dr.getCode() == ErrorEnum.SUCCESS_CODE.getCode()) {
            return dr.getData();
        }
        return null;
    }

    /**
     * 获取描述抽取公共方法，减少重复的if else 判断代码
     * @param id
     * @param call
     * @return
     */
    public String getDesc(Long id, CallService<String> call) {
        try {
            if (id != null && id.longValue() > 0) {
                return call.call();
            }
        } catch (Exception e) {
            log.error("getDesc id:{} error:{}", id, e.getMessage());
            return null;
        }
        return null;
    }
}
