package com.yfker.${ModuleName}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yfker.common.constant.ErrorEnum;
import com.yfker.common.exception.ServiceException;
import com.yfker.common.service.impl.BaseServiceImpl;
import com.yfker.common.utils.SimpleBeanUtils;
import com.yfker.${ModuleName}.dao.mapper.${ModelName}Mapper;
import com.yfker.${ModuleName}.pojo.dto.${ModelName}EditDto;
import com.yfker.${ModuleName}.pojo.dto.${ModelName}QueryDto;
import com.yfker.${ModuleName}.pojo.model.${ModelName};
import com.yfker.${ModuleName}.pojo.vo.${ModelName}DetailVo;
import com.yfker.${ModuleName}.pojo.vo.${ModelName}ListVo;
import com.yfker.${ModuleName}.service.I${ModelName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: ${ModuleName}.${ModelName} 相关的服务实现类
 * @author: ${author}
 * @date: ${crateDate}
 **/
@Service
public class ${ModelName}ServiceImpl extends BaseServiceImpl<${ModelName}Mapper, ${ModelName}> implements I${ModelName}Service {

    @Autowired
    private ${ModelName}Mapper ${ModelNameLower}Mapper;

    @Override
    public IPage<${ModelName}ListVo> listPageByDto(${ModelName}QueryDto queryDto) {
        Page<${ModelName}> page = new Page(queryDto.getCurrentPage(), queryDto.getPageSize(), true);
        return ${ModelNameLower}Mapper.listPageByDto(page, queryDto);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Long save(${ModelName}EditDto dto) {
        ${ModelName} entity = SimpleBeanUtils.autoFill(dto, ${ModelName}.class);
        ${ModelNameLower}Mapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void edit(${ModelName}EditDto dto) {
        ${ModelName} entity = SimpleBeanUtils.autoFill(dto, ${ModelName}.class);
        ${ModelNameLower}Mapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void del(Long id) {
        LambdaUpdateWrapper<${ModelName}> luw = new LambdaUpdateWrapper<>();
        luw.set(${ModelName}::getIsdeleted, true).eq(${ModelName}::getId, id);
        ${ModelNameLower}Mapper.update(null, luw);
    }

    @Override
    public ${ModelName}DetailVo getDetail(Long id) {
        LambdaQueryWrapper<${ModelName}> lqw = new LambdaQueryWrapper<>();
        lqw.eq(${ModelName}::getId, id).eq(${ModelName}::getIsdeleted, false);
        ${ModelName} entity = ${ModelNameLower}Mapper.selectOne(lqw);
        if (null == entity) {
            throw new ServiceException(ErrorEnum.DATA_NOT_EXISTS);
        }
        ${ModelName}DetailVo dto = SimpleBeanUtils.autoFill(entity, ${ModelName}DetailVo.class);
        return dto;
    }
}