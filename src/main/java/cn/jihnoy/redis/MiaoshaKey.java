package cn.jihnoy.redis;

public class MiaoshaKey extends BasePrefix{
    private MiaoshaKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey(0,"goodsOver");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "gpath");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");
}
