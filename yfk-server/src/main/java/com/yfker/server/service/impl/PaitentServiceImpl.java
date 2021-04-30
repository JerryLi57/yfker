package com.yfker.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yfker.common.constant.ErrorEnum;
import com.yfker.common.exception.ServiceException;
import com.yfker.common.service.impl.BaseServiceImpl;
import com.yfker.common.utils.SimpleBeanUtils;
import com.yfker.server.dao.mapper.PaitentMapper;
import com.yfker.server.pojo.dto.PaitentEditDto;
import com.yfker.server.pojo.dto.PaitentQueryDto;
import com.yfker.server.pojo.model.Paitent;
import com.yfker.server.pojo.vo.PaitentDetailVo;
import com.yfker.server.pojo.vo.PaitentListVo;
import com.yfker.server.service.IPaitentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: server.Paitent 相关的服务实现类
 * @author: lijiayu
 * @date: 2021-04-29 11:36:37
 **/
@Service
public class PaitentServiceImpl extends BaseServiceImpl<PaitentMapper, Paitent> implements IPaitentService {

    @Autowired
    private PaitentMapper paitentMapper;

    @Override
    public IPage<PaitentListVo> listPageByDto(PaitentQueryDto queryDto) {
        Page<Paitent> page = new Page(queryDto.getCurrentPage(), queryDto.getPageSize(), true);
        return paitentMapper.listPageByDto(page, queryDto);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Long save(PaitentEditDto dto) {
        Paitent entity = SimpleBeanUtils.autoFill(dto, Paitent.class);
        paitentMapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void edit(PaitentEditDto dto) {
        Paitent entity = SimpleBeanUtils.autoFill(dto, Paitent.class);
        paitentMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void del(Long id) {
        LambdaUpdateWrapper<Paitent> luw = new LambdaUpdateWrapper<>();
        luw.set(Paitent::getIsdeleted, true).eq(Paitent::getId, id);
        paitentMapper.update(null, luw);
    }

    @Override
    public PaitentDetailVo getDetail(Long id) {
        LambdaQueryWrapper<Paitent> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Paitent::getId, id).eq(Paitent::getIsdeleted, false);
        Paitent entity = paitentMapper.selectOne(lqw);
        if (null == entity) {
            throw new ServiceException(ErrorEnum.DATA_NOT_EXISTS);
        }
        PaitentDetailVo dto = SimpleBeanUtils.autoFill(entity, PaitentDetailVo.class);
        return dto;
    }
}