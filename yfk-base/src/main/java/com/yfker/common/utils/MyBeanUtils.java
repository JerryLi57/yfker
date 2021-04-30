package com.yfker.common.utils;

import cn.hutool.core.bean.BeanUtil;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBeanUtils {


    /**
     * 将两个JavaBean里相同的字段自动填充
     * @param obj   原JavaBean对象
     * @param toObj 将要填充的对象
     */
    public static void autoFillEqFields(Object obj, Object toObj) {
        try {
            Map<String, Method> getMaps = new HashMap<>();
            Method[] sourceMethods = obj.getClass().getMethods();
            for (Method m : sourceMethods) {
                if (m.getName().startsWith("get")) {
                    getMaps.put(m.getName(), m);
                }
            }

            Method[] targetMethods = toObj.getClass().getMethods();
            for (Method m : targetMethods) {
                if (!m.getName().startsWith("set")) {
                    continue;
                }
                String key = "g" + m.getName().substring(1);
                Method getm = getMaps.get(key);
                if (null == getm) {
                    continue;
                }
                // 写入方法写入
                try {
                    m.invoke(toObj, getm.invoke(obj));
                } catch (Exception e) {
                    // 如果数据类型不一致打印日志
                    System.out.println("autoFillEqFields error field:" + key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将两个JavaBean里相同的字段自动填充
     * @param obj      原JavaBean对象
     * @param toObj    将要填充的对象
     * @param excludes 排除不需要处理的字段
     */
    public static void autoFillEqFields(Object obj, Object toObj, String... excludes) {
        try {
            Map<String, Method> getMaps = new HashMap<>();
            Method[] sourceMethods = obj.getClass().getMethods();
            for (Method m : sourceMethods) {
                if (m.getName().startsWith("get") && !getMisInfields(m.getName(), excludes)) {
                    getMaps.put(m.getName(), m);
                }
            }

            Method[] targetMethods = toObj.getClass().getMethods();
            for (Method m : targetMethods) {
                if (!m.getName().startsWith("set")) {
                    continue;
                }
                String key = "g" + m.getName().substring(1);
                Method getm = getMaps.get(key);
                if (null == getm) {
                    continue;
                }
                // 写入方法写入
                try {
                    m.invoke(toObj, getm.invoke(obj));
                } catch (Exception e) {
                    // 如果数据类型不一致打印日志
                    System.out.println("autoFillEqFields error field:" + key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断一个对象的get方法的名称是否在一个字符串数组里
     * @param fields
     * @return
     */
    private static boolean getMisInfields(String gname, String[] fields) {
        String field = gname.substring(3, 4).toLowerCase() + gname.substring(4);
        for (String f : fields) {
            if (f.equals(field)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String a = "getAsasdfsdafb";
        String b = a.substring(3, 4).toLowerCase() + a.substring(4);

        System.out.println(a.substring(3, 4));
        System.out.println(b);
    }

    /**
     * map 转化为 bean
     * @param clazz
     * @param map
     * @return
     */
    public static <T> T mapToBean(Class<T> clazz, Map<String, Object> map) {
        try {
            return BeanUtil.mapToBean(map, clazz, true);
        } catch (Exception e) {
        }
        return null;
    }

    private static <T> PropertyDescriptor getPropertyDescriptor(String field, Class<T> clazz) {
        try {
            return new PropertyDescriptor(field, clazz);
        } catch (IntrospectionException e) {
            return null;
        }
    }

    /**
     * 将一个List里面的对象 转换成一个新的List 对象
     * @param list  源数据 list
     * @param clasv 需要转换的成为的 bean 的 class
     * @return
     */
    public static <T, V> List<V> autoFill(List<T> list, Class<V> clasv) {
        List<V> listv = new ArrayList<>();
        for (T obj : list) {
            listv.add(autoFill(obj, clasv));
        }
        return listv;
    }

    public static <T, V> V autoFill(T obj, Class<V> v) {
        V toObj = null;
        try {
            if (null == obj) {
                return null;
            }
            if (obj instanceof Map) {
                toObj = (V) MyBeanUtils.mapToBean(v, (Map<String, Object>) obj);
            } else {
                toObj = v.newInstance();
                MyBeanUtils.autoFillEqFields(obj, toObj);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return toObj;
    }
}