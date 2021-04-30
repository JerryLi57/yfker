package com.lolaage.codegenerator.freemarker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-03-06 17:41
 **/
public class Tool {

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 表字段的前缀
     */
    private static final String TABLE_PREFIX = "f_";

    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        // 处理表字段 f_ 前缀
        if (str.startsWith(TABLE_PREFIX)) {
            str = str.substring(2);
        }
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        String lineToHump = lineToHump("f_parent_no_leader");
        // fParentNoLeader
        System.out.println(lineToHump);
        // f_parent_no_leader
        System.out.println(humpToLine(lineToHump));
        lineToHump = lineToHump.substring(0, 1).toUpperCase() + lineToHump.substring(1);
        System.out.println(lineToHump);

    }
}
