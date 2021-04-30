package com.yfker.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yfker.common.constant.ErrorEnum;
import com.yfker.common.exception.JwtException;
import com.yfker.common.exception.ServiceException;
import com.yfker.common.pojo.DataResult;
import com.yfker.common.pojo.PageHelper;
import com.yfker.common.pojo.TokenUser;
import com.yfker.common.pojo.ValidParamsDto;
import com.yfker.common.service.common.CallService;
import com.yfker.common.service.common.RunService;
import com.yfker.common.service.common.ValidService;
import com.yfker.common.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: controller 的基类，抽出处理controller的公共方法
 * @author: lijiayu
 * @date: 2020-03-11 19:47
 **/
@Slf4j
public class BaseController {

    /**
     * 记录一个key是否已存在缓存中，存在为 "1" 否则为 null
     */
    public final static String KEY_EXISTED_IN_CACHE = "1";

    /**
     * 统一封装返回对象类型 可以是对象或list类型
     * @param callService
     * @param <T>
     * @return
     */
    protected <T> DataResult<T> resultDataCommon(CallService<T> callService) {
        return packageException(() -> {
            DataResult<T> dataResult = new DataResult<>();
            dataResult.setData(callService.call());
            return dataResult;
        });
    }


    /**
     * 统一封装返回对象类型 除了对象与list类型
     * @param callService
     * @param <T>
     * @return
     */
    protected <T> DataResult<T> resultDataCommon(CallService<T> callService, BindingResult bindingResult) {
        return packageException(() -> {
            if (bindingResult.hasErrors()) {
                return new DataResult<>(ErrorEnum.PARAM_ILLEGAL.getCode(), bindingResult.getFieldError().getDefaultMessage());
            }
            DataResult<T> dataResult = new DataResult<>();
            dataResult.setData(callService.call());
            return dataResult;
        });
    }

    /**
     * 统一处理需要字段校验，并涉及带数据库校验的方法
     * @param callService
     * @param bindingResult
     * @param <T>
     * @return
     */
    protected <T> DataResult<T> resultDataValidCommon(CallService<T> callService, ValidService validService, BindingResult bindingResult) {
        return packageException(() -> {
            if (bindingResult.hasErrors()) {
                return new DataResult<>(ErrorEnum.PARAM_ILLEGAL.getCode(), bindingResult.getFieldError().getDefaultMessage());
            }
            ValidParamsDto validRet = validService.verify();
            if (!validRet.isAllowed()) {
                return new DataResult<>(ErrorEnum.PARAM_ILLEGAL.getCode(), validRet.getMsg());
            }
            DataResult<T> dataResult = new DataResult();
            dataResult.setData(callService.call());
            return dataResult;
        });
    }

    /**
     * 统一封装返回对象类型,带分页的 处理 DataResult
     * @param callService
     * @param <T>
     * @return
     */
    protected <T> DataResult<List<T>> resultDataPagePackage(CallService<IPage<T>> callService) {
        return packageException(() -> {
            IPage<T> pageResult = callService.call();
            DataResult<List<T>> result = new DataResult<>();
            result.setPage(PageHelper.getPageDto(pageResult));
            result.setData(pageResult.getRecords());
            return result;
        });
    }

    /**
     * 统一封装返回对象类型
     * @param runService 业务执行对象，无返回
     * @return 统一返回对象
     * @Description: 适用于只操作的业务方法，不需要返回。
     */
    protected DataResult resultDataCommon(RunService runService) {
        return resultDataCommon(runService, null);
    }

    protected DataResult resultDataCommon(RunService runService, BindingResult bindingResult) {
        return packageException(() -> {
            if (bindingResult != null && bindingResult.hasErrors()) {
                return new DataResult<>(ErrorEnum.PARAM_ILLEGAL.getCode(), bindingResult.getFieldError().getDefaultMessage());
            }
            DataResult<?> dataResult = new DataResult<>();
            if (runService != null) {
                runService.run();
            }
            return dataResult;
        });
    }

    /**
     * 统一封装返回对象类型 处理 DataResult
     * @param callService
     * @param <T>
     * @return
     */
    protected <T> DataResult<List<T>> resultDataPackage(CallService<DataResult<List<T>>> callService) {
        return packageException(() -> callService.call());
    }

    /**
     * 统一获取登录用户的信息
     * @return
     */
    protected TokenUser getCurrentUser() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = getToken(request);
        return JwtTokenUtil.getTokenUserFromToken(token);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtTokenUtil.TOKEN_HEADER)) {
                token = cookie.getValue();
                break;
            }
        }
        return token;
    }

    /**
     * 统一异常封装处理
     * @param callService
     * @param <T>
     * @return
     */
    private <T> DataResult<T> packageException(CallService<DataResult<T>> callService) {
        try {
            return callService.call();
        } catch (ServiceException e) {
            log.error("ServiceException:{}", e.getMsg());
            return new DataResult<>(e.getCode(), e.getMsg());
        } catch (JwtException e) {
            log.error("JwtException:{}", e.getMsg());
            return new DataResult<>(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.error("error:", e);
            return new DataResult<>(ErrorEnum.SERVICE_ERROR);
        }
    }
}
