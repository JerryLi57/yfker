package com.yfker.${ModuleName}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yfker.common.service.IBaseService;
import com.yfker.${ModuleName}.pojo.dto.${ModelName}EditDto;
import com.yfker.${ModuleName}.pojo.dto.${ModelName}QueryDto;
import com.yfker.${ModuleName}.pojo.vo.${ModelName}ListVo;
import com.yfker.${ModuleName}.pojo.vo.${ModelName}DetailVo;
import com.yfker.${ModuleName}.pojo.model.${ModelName};

/**
 * @description: ${ModuleName}.${ModelName} 相关的服务接口类
 * @author: ${author}
 * @date: ${crateDate}
 **/
public interface I${ModelName}Service extends IBaseService<${ModelName}> {

    /**
     * 分页查询获取列表数据
     * @param queryDto
     * @return
     */
    IPage<${ModelName}ListVo> listPageByDto(${ModelName}QueryDto queryDto);

    /**
     * 保存
     * @param dto
     * @return
     */
    Long save(${ModelName}EditDto dto);

    /**
     * 编辑
     * @param dto
     * @return
     */
    void edit(${ModelName}EditDto dto);

    /**
     * 删除
     * @param id
     */
    void del(Long id);

    /**
     * 根据ID获取详情
     * @param id
     * @return
     */
    ${ModelName}DetailVo getDetail(Long id);
}