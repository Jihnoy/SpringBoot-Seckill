package cn.jihnoy.redis;

public class MiaoshaUserKey extends BasePrefix{
    public static final int TOKEN_EXPIREN = 3600*24*2;
    private MiaoshaUserKey(int expireSeconds, String prefix){
        super(expireSeconds, prefix);
    }


    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIREN,"tk");

}
