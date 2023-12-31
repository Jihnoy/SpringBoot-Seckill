package cn.jihnoy.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "zxyclcnwnsns1234";

    public static String inputPassFormPass(String inputPass){
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt){
        String str = salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(3) + salt.charAt(5);
        return md5(str);
    }

    public static String inputPassToDBPass(String input, String saltDB){
        String formPass = inputPassFormPass(input);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("123456", salt));
    }
}
