package com.yfker.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yfker.common.service.IBaseService;
import com.yfker.server.pojo.dto.PaitentEditDto;
import com.yfker.server.pojo.dto.PaitentQueryDto;
import com.yfker.server.pojo.vo.PaitentListVo;
import com.yfker.server.pojo.vo.PaitentDetailVo;
import com.yfker.server.pojo.model.Paitent;

/**
 * @description: server.Paitent 相关的服务接口类
 * @author: lijiayu
 * @date: 2021-04-29 11:36:37
 **/
public interface IPaitentService extends IBaseService<Paitent> {

    /**
     * 分页查询获取列表数据
     * @param queryDto
     * @return
     */
    IPage<PaitentListVo> listPageByDto(PaitentQueryDto queryDto);

    /**
     * 保存
     * @param dto
     * @return
     */
    Long save(PaitentEditDto dto);

    /**
     * 编辑
     * @param dto
     * @return
     */
    void edit(PaitentEditDto dto);

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
    PaitentDetailVo getDetail(Long id);
}