package cn.jihnoy.util;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidataUtil {

    private static final  Pattern mobile_pattern = Pattern.compile("1\\d{10}");
    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher m = mobile_pattern.matcher(mobile);
        return m.matches();
    }
}
