package com.yfker.server.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yfker.common.dao.mapper.SuperMapper;
import com.yfker.server.pojo.dto.PaitentQueryDto;
import com.yfker.server.pojo.vo.PaitentListVo;
import com.yfker.server.pojo.model.Paitent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @description: 病人注射历史记录 Model
 * @author: lijiayu
 * @date: 2021-04-29 11:36:37
 */
@Mapper
@Component("paitentMapper")
public interface PaitentMapper extends SuperMapper<Paitent> {

    IPage<PaitentListVo> listPageByDto(IPage<Paitent> page, @Param("dto") PaitentQueryDto paitentDto);

}