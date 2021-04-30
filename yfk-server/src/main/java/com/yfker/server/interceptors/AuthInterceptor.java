package com.yfker.server.interceptors;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yfker.common.constant.ErrorEnum;
import com.yfker.common.constant.RedisKeys;
import com.yfker.common.pojo.DataResult;
import com.yfker.common.pojo.TokenUser;
import com.yfker.common.utils.JwtTokenUtil;
import com.yfker.common.utils.redis.RedisUtil;
import com.yfker.server.config.NotAuthUrlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @description: AuthInterceptor 权限拦截器
 * @author: lijiayu
 * @date: 2020-07-10 16:09
 **/
@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    static final String ADMIN_ACCOUNT = "admin";
    /**
     * 请求不需要用户认证的URL
     */
    @Autowired
    private NotAuthUrlConfig notAuthUrlConfig;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 路径校验类，使用一个就好了，不用每次都new一个
     */
    private PathMatcher pathMatcher = new AntPathMatcher();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String currentUrl = request.getServletPath();

        /*// 跳过不需要认证的url
        if (shouldSkip(currentUrl)) {
            log.info("跳过认证的URL:{}", currentUrl);
            return true;
        }
        log.info("需要认证的URL:{}", currentUrl);

        // 获取token
        String token = getToken(request);
        if (null == token) {
            responseError(response, ErrorEnum.SYS_401);
        }
        TokenUser tokenUser = new TokenUser();
        try {
            tokenUser = JwtTokenUtil.getTokenUserFromToken(token);
        } catch (Exception ex) {
            responseError(response, ErrorEnum.JWT_TOKEN_INVALID);
        }

        // token 校验
        if (!validToken(response, token, tokenUser)) {
            return false;
        }
        // token 临近过期时间做替换
        String newToken = JwtTokenUtil.refreshHeadToken(token);
        if (null != newToken && newToken.length() > 0) {
            response.setHeader(JwtTokenUtil.TOKEN_HEADER, newToken);
        }
        // 跳过不需要授权但需要认证登录的路径认证的url, 这种连接不做权限校验
        if (shouldSkipButNeedLogin(currentUrl)) {
            log.info("跳过不需要权限认证的URL:{}", currentUrl);
            return true;
        }
        log.info("需要权限认证的URL:{}", currentUrl);
        //对特定的账号不做校验
        if (ADMIN_ACCOUNT.equalsIgnoreCase(tokenUser.getUsername())) {
            return true;
        }*/

        return true;
    }


    /**
     * 从请求头或Cookie中获取 egctoken
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return token;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtTokenUtil.TOKEN_HEADER)) {
                token = cookie.getValue();
                break;
            }
        }
        return token;
    }

    /**
     * 验证是否登录
     * @param response
     * @param token
     * @param tokenUser
     * @return
     * @throws IOException
     */
    private boolean validToken(HttpServletResponse response, String token, TokenUser tokenUser) throws IOException {
        if (StringUtils.isEmpty(token)) {
            responseError(response, ErrorEnum.SYS_401);
            return false;
        }
        // 校验token是否已过期
        if (JwtTokenUtil.isTokenExpired(token)) {
            responseError(response, ErrorEnum.SYS_401);
            return false;
        }
        // 校验是否已经在 已退出登录的用户列表
        String flag = redisUtil.get(RedisKeys.USER_LOGOUT_FLAG + tokenUser.getId());
        if (StringUtils.isNotEmpty(flag)) {
            responseError(response, ErrorEnum.SYS_401);
            return false;
        }
        return true;
    }


    private void responseError(HttpServletResponse response, ErrorEnum errorEnum) throws IOException {
        //不会继续执行....不会去调用服务接口，网关服务直接响应给客户端
        DataResult dataResult = null;
        if (errorEnum != null) {
            dataResult = new DataResult(errorEnum);
        } else {
            dataResult = new DataResult(ErrorEnum.SYS_403);
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().flush();
        response.getWriter().write(JSON.toJSONString(dataResult));
        response.getWriter().close();

    }


    /**
     * 方法实现说明:不需要授权的路径
     * @param currentUrl 当前请求路径
     * @return
     */
    private boolean shouldSkip(String currentUrl) {
        return urlFilter(currentUrl, notAuthUrlConfig.getShouldSkipUrls());
    }

    /**
     * 方法实现说明:不需要授权但需要认证登录的路径
     * @param currentUrl 当前请求路径
     * @return
     */
    private boolean shouldSkipButNeedLogin(String currentUrl) {
        return urlFilter(currentUrl, notAuthUrlConfig.getShouldSkipButNeedLoginUrls());
    }

    /**
     * 路径匹配器(简介SpringMvc拦截器的匹配器)
     * 比如/oauth/** 可以匹配/oauth/token    /oauth/check_token等
     * @param currentUrl
     * @param shouldSkipUrls
     * @return
     */
    private boolean urlFilter(String currentUrl, List<String> shouldSkipUrls) {
        for (String skipPath : shouldSkipUrls) {
            if (pathMatcher.match(skipPath, currentUrl)) {
                return true;
            }
        }
        return false;
    }


}
