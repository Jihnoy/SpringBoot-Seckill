package cn.jihnoy.access;

import cn.jihnoy.domain.MiaoshaUser;

public class UserContext {
    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }
}
