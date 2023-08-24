package cn.jihnoy.vo;

import cn.jihnoy.domain.MiaoshaUser;

public class GoodsDetailVo {
    int miaoshaStatus ;
    int remainSeconds ;
    MiaoshaUser miaoshaUser;
    public MiaoshaUser getMiaoshaUser() {
        return miaoshaUser;
    }

    public void setMiaoshaUser(MiaoshaUser miaoshaUser) {
        this.miaoshaUser = miaoshaUser;
    }



    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    private GoodsVo goodsVo;
}
