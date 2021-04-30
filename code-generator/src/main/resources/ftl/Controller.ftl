package com.yfker.${ModuleName}.controller;

import com.yfker.common.controller.BaseController;
import com.yfker.common.pojo.DataResult;
import com.yfker.common.valid.group.AddGroup;
import com.yfker.common.valid.group.UpdateGroup;
import com.yfker.${ModuleName}.pojo.dto.${ModelName}EditDto;
import com.yfker.${ModuleName}.pojo.dto.${ModelName}QueryDto;
import com.yfker.${ModuleName}.pojo.vo.${ModelName}DetailVo;
import com.yfker.${ModuleName}.pojo.vo.${ModelName}ListVo;
import com.yfker.${ModuleName}.service.I${ModelName}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: ${tableComment}模块相关API接口
 * @author: ${author}
 * @date: ${crateDate}
 **/
@Api(tags = "${tableComment}模块相关API接口 author:${author}")
@RestController
@RequestMapping("/${ModelNameLower}")
public class ${ModelName}Controller extends BaseController {

    @Autowired
    private I${ModelName}Service ${ModelNameLower}Service;

    @GetMapping(value = "/list")
    @ApiOperation(value = "分页查询${tableComment}列表信息 author:${author}", notes = "分页查询${tableComment}列表信息")
    public DataResult<List<${ModelName}ListVo>> listByPage(${ModelName}QueryDto queryDto) {
        return resultDataPagePackage(() -> ${ModelNameLower}Service.listPageByDto(queryDto));
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "添加${tableComment}信息 author:${author}", notes = "添加${tableComment}信息")
    public DataResult<Long> save(@RequestBody @Validated(AddGroup.class) ${ModelName}EditDto editDto, BindingResult bindingResult) {
        return resultDataCommon(() -> ${ModelNameLower}Service.save(editDto), bindingResult);
    }

    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑${tableComment}信息 author:${author}", notes = "编辑${tableComment}信息")
    public DataResult<Long> edit(@RequestBody @Validated(UpdateGroup.class) ${ModelName}EditDto editDto, BindingResult bindingResult) {
        return resultDataCommon(() -> ${ModelNameLower}Service.edit(editDto), bindingResult);
    }

    @GetMapping(value = "/getDetail")
    @ApiOperation(value = "获取${tableComment}详细信息 author:${author}", notes = "获取${tableComment}详细信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "应急救援设备信息Id", dataType = "Long", required = true)
    )
    public DataResult<${ModelName}DetailVo> getDetail(@RequestParam(name = "id") Long id) {
        return resultDataCommon(() -> ${ModelNameLower}Service.getDetail(id));
    }

    @GetMapping(value = "/del")
    @ApiOperation(value = "删除${tableComment}信息 author:${author}", notes = "删除${tableComment}信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "应急救援设备信息Id", dataType = "Long", required = true)
    )
    public DataResult del(@RequestParam(name = "id") Long id) {
        return resultDataCommon(() -> ${ModelNameLower}Service.del(id));
    }

}