package com.yfker.server.controller;

import com.yfker.common.controller.BaseController;
import com.yfker.common.pojo.DataResult;
import com.yfker.common.valid.group.AddGroup;
import com.yfker.common.valid.group.UpdateGroup;
import com.yfker.server.pojo.dto.PaitentEditDto;
import com.yfker.server.pojo.dto.PaitentQueryDto;
import com.yfker.server.pojo.vo.PaitentDetailVo;
import com.yfker.server.pojo.vo.PaitentListVo;
import com.yfker.server.service.IPaitentService;
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
 * @description: 病人注射历史记录模块相关API接口
 * @author: lijiayu
 * @date: 2021-04-29 11:36:37
 **/
@Api(tags = "病人注射历史记录模块相关API接口 author:lijiayu")
@RestController
@RequestMapping("/paitent")
public class PaitentController extends BaseController {

    @Autowired
    private IPaitentService paitentService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "分页查询病人注射历史记录列表信息 author:lijiayu", notes = "分页查询病人注射历史记录列表信息")
    public DataResult<List<PaitentListVo>> listByPage(PaitentQueryDto queryDto) {
        return resultDataPagePackage(() -> paitentService.listPageByDto(queryDto));
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "添加病人注射历史记录信息 author:lijiayu", notes = "添加病人注射历史记录信息")
    public DataResult<Long> save(@RequestBody @Validated(AddGroup.class) PaitentEditDto editDto, BindingResult bindingResult) {
        return resultDataCommon(() -> paitentService.save(editDto), bindingResult);
    }

    @PostMapping(value = "/edit")
    @ApiOperation(value = "编辑病人注射历史记录信息 author:lijiayu", notes = "编辑病人注射历史记录信息")
    public DataResult<Long> edit(@RequestBody @Validated(UpdateGroup.class) PaitentEditDto editDto, BindingResult bindingResult) {
        return resultDataCommon(() -> paitentService.edit(editDto), bindingResult);
    }

    @GetMapping(value = "/getDetail")
    @ApiOperation(value = "获取病人注射历史记录详细信息 author:lijiayu", notes = "获取病人注射历史记录详细信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "应急救援设备信息Id", dataType = "Long", required = true)
    )
    public DataResult<PaitentDetailVo> getDetail(@RequestParam(name = "id") Long id) {
        return resultDataCommon(() -> paitentService.getDetail(id));
    }

    @GetMapping(value = "/del")
    @ApiOperation(value = "删除病人注射历史记录信息 author:lijiayu", notes = "删除病人注射历史记录信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "应急救援设备信息Id", dataType = "Long", required = true)
    )
    public DataResult del(@RequestParam(name = "id") Long id) {
        return resultDataCommon(() -> paitentService.del(id));
    }

}