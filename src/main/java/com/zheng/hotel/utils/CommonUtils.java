package com.zheng.hotel.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class CommonUtils {

    //加密盐值
    private static final String salt = "jweiorunnnq123";

    /**
     * 对密码加密算法
     *
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        return md5(salt + md5(password + salt));
    }


    /**
     * md5摘要
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            return bytesToHex(MessageDigest.getInstance("MD5").digest(str.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            log.error("md5摘要出错", e);
            return null;
        }
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
