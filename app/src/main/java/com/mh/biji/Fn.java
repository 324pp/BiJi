package com.mh.biji;

import java.util.UUID;

/**
 * Created by MH on 2015-06-24.
 */
public class Fn {
    public static String getGuid() {
        UUID id = UUID.randomUUID();
        return id.toString();
    }

    /**
     * 替换字符串
     *
     * @param from String 原始字符串
     * @param to String 目标字符串
     * @param source String 母字符串
     * @return String 替换后的字符串
     */
    public static String replaceString(String source, String from, String to) {
        if (source == null || from == null || to == null)
            return null;
        StringBuffer bf = new StringBuffer("");
        int index = -1;
        while ((index = source.indexOf(from)) != -1) {
            bf.append(source.substring(0, index) + to);
            source = source.substring(index + from.length());
            index = source.indexOf(from);
        }
        bf.append(source);
        return bf.toString();
    }
}
