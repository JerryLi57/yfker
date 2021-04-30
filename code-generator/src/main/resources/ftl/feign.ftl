package com.yfker.gateway.feign.${ModuleName};

import com.yfker.${ModuleName}.dto.${ModelName}Dto;
import com.lolaage.utils.page.DataResult;
import com.lolaage.utils.page.PageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @description: ${tableComment}模块 feign
 * @author: ${author}
 * @date: ${crateDate}
 **/
@FeignClient(name = "egc-${ModuleName}")
public interface ${ModelName}FeignClient {

    @GetMapping("/${ModelNameLower}/list")
    DataResult<List<${ModelName}Dto>> listByPage(PageDto pageDto, ${ModelName}Dto ${ModelNameLower}Dto);

}