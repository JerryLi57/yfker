package com.yfker.common.utils;


import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @description: 使用hashMap的方式，将数据存储在jvm内存中，对key与value 使用 byte[] 的方式压缩
 * @author: lijiayu
 * @date: 2019-09-05 17:20
 **/
public class HashCacheUtil {

    /**
     * 内部缓存数据，考虑到String 类型 in pool of strings 的复用性，所以key不进行压缩
     */
    private static Map<String, byte[]> innerCache = new ConcurrentHashMap<>();

    public static void put(String key, String value) {
        key.intern();
        innerCache.put(key, strToBytes(value));
    }

    public static String get(String key) {
        if (innerCache.containsKey(key)) {
            return new String(innerCache.get(key));
        }
        return "";
    }

    public static void remove(String key) {
        if (innerCache.containsKey(key)) {
            innerCache.remove(key);
        }
    }

    /**
     * 匹配所有前缀为 prefix字符串的key
     * @param prefix 为空时返回所有的key
     * @return
     */
    public static List<String> listKeysByPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return new ArrayList<>(innerCache.keySet());
        }
        return innerCache.keySet().stream().filter(e -> e.startsWith(prefix)).collect(Collectors.toList());
    }

    /**
     * 字符串转byte数组
     * @param key
     * @return
     */
    private static byte[] strToBytes(String key) {
        return key.getBytes(StandardCharsets.UTF_8);
    }

}
