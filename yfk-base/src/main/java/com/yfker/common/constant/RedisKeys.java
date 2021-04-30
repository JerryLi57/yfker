package com.yfker.common.constant;

/**
 * @description: 定义公用的Redis常用的key
 * @author: lijiayu
 * @date: 2020-03-19 13:50
 **/
public interface RedisKeys {

    /**
     * 用户登录之后存贮的部门Id key
     */
    String USER_LOGIN_ORG_ID = "user_login_org_";

    /**
     * 用户登出标识 key
     */
    String USER_LOGOUT_FLAG = "user_logout_";

    /**
     * 权限菜单的 key
     */
    String AUTH_MENU = "auth_menu_";

    /**
     * 使用redis锁的key前缀
     */
    String LOCK_KEY = "lock_";

    /**
     * 使用redis 所有设备的key
     */
    String ALL_EQUIPMENT = "all_equipment";

    /**
     * 缓存所有的类别的key
     */
    String cacheCategoryKey="Redis_CacheCategoryList";

    /**
     * 缓存所有的区域的key
     */
    String cacheRegionKey="Redis_CacheRegionList";

    /**
     * 缓存根据父Id查询所有的类别的key
     */
    String cacheCategoryByParentIdKey="categoryListByParentId";

}
