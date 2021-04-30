package com.yfker.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.yfker.common.constant.ErrorEnum;
import com.yfker.common.exception.JwtException;
import com.yfker.common.pojo.TokenUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"userid":18,"orgid":6,"username":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * @author: lijiayu
 * @date: 2020-03-18 10:21
 **/
@Slf4j
public class JwtTokenUtil {

    /**
     * JWT定义的头命名
     */
    public static final String TOKEN_HEADER = "crm-token";

    /**
     * 定义好一个Jwt包含的字段信息名称
     */
    private static final String CLAIM_KEY_USERID = "userid";
    private static final String CLAIM_KEY_ORGID = "orgid";
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_IS_LEADER = "isLeader";
    private static final String CLAIM_KEY_CREATED = "created";

    /**
     * JWT加解密使用的密钥
     */
    private static String secret = "project-jwt-secret";

    /**
     * JWT的超期限时间单位秒(60*60*24) 默认一天 24小时
     */
    private static Long expiration = 60 * 60 * 12L;

    /**
     * 刷新token的时间， 快过期时需要提前刷新token，默认token还有最后2小时时刷新
     */
    private static int toRefreshTime = 60 * 30;

    /**
     * 根据负责生成JWT的token
     */
    private static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("JWT格式验证失败:{}", token);
            throw new JwtException(ErrorEnum.JWT_TOKEN_INVALID);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private static Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取用户信息
     */
    public static TokenUser getTokenUserFromToken(String token) {
        TokenUser tokenUser = null;
        try {
            Claims claims = getClaimsFromToken(token);
            tokenUser = new TokenUser();
            tokenUser.setId(claims.get(CLAIM_KEY_USERID, Long.class));
            tokenUser.setOrgid(claims.get(CLAIM_KEY_ORGID, Long.class));
            tokenUser.setUsername(claims.get(CLAIM_KEY_USERNAME, String.class));
            tokenUser.setIsLeader(claims.get(CLAIM_IS_LEADER, Boolean.class));
        } catch (Exception e) {
            throw new JwtException(ErrorEnum.JWT_TOKEN_INVALID);
        }
        return tokenUser;
    }

    /**
     * 验证token是否还有效
     * @param token 客户端传入的token
     * @param orgId 部门Id
     */
    public static boolean validateToken(String token, Long orgId) {
        try {
            if (orgId == null || orgId.longValue() <= 0) {
                return false;
            }
            TokenUser tokeUser = getTokenUserFromToken(token);
            if (orgId.longValue() == tokeUser.getOrgid().longValue()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断token是否已经失效
     */
    public static boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private static Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public static String generateToken(TokenUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERID, user.getId());
        claims.put(CLAIM_KEY_ORGID, user.getOrgid());
        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
        claims.put(CLAIM_IS_LEADER, user.getIsLeader());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 当原来的token没过期时是可以刷新的
     * @param oldToken 带tokenHead的token
     */
    public static String refreshHeadToken(String oldToken) {
        if (StrUtil.isEmpty(oldToken)) {
            return null;
        }
        //token校验不通过
        Claims claims = getClaimsFromToken(oldToken);
        if (claims == null) {
            return null;
        }
        //如果token已经过期，不支持刷新
        if (isTokenExpired(oldToken)) {
            return null;
        }
        //如果token在 指定时间之内刚刷新过, 不做处理
        if (tokenRefreshJustBefore(oldToken, toRefreshTime)) {
            return null;
        } else {
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(claims);
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     * @param token 原token
     * @param time  指定时间（秒）
     */
    private static boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();
        //刷新时间在创建时间的指定时间内
        if (refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, time))) {
            return true;
        }
        return false;
    }
}
