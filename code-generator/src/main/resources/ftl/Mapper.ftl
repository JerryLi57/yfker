package com.yfker.${ModuleName}.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yfker.common.dao.mapper.SuperMapper;
import com.yfker.${ModuleName}.pojo.dto.${ModelName}QueryDto;
import com.yfker.${ModuleName}.pojo.vo.${ModelName}ListVo;
import com.yfker.${ModuleName}.pojo.model.${ModelName};
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @description: ${tableComment} Model
 * @author: ${author}
 * @date: ${crateDate}
 */
@Mapper
@Component("${ModelNameLower}Mapper")
public interface ${ModelName}Mapper extends SuperMapper<${ModelName}> {

    IPage<${ModelName}ListVo> listPageByDto(IPage<${ModelName}> page, @Param("dto") ${ModelName}QueryDto ${ModelNameLower}Dto);

}