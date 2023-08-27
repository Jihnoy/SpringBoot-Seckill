package cn.jihnoy.service;

import cn.jihnoy.dao.GoodsDao;
import cn.jihnoy.domain.Goods;
import cn.jihnoy.domain.MiaoshaOrder;
import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.domain.OrderInfo;
import cn.jihnoy.redis.MiaoshaKey;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存，下订单，写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        //order_info, miaosha_order
        if(success){
            return orderService.creatOrder(user, goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    private void setGoodsOver(long id) {
        redisService.set(MiaoshaKey.isGoodsOver,""+id, true);
    }

    public long getMiaoshaResult(long id, long goodsId) {
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUidGoodsId(id, goodsId);
        if(miaoshaOrder != null){
            return miaoshaOrder.getOrderId();
        }
        else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exist(MiaoshaKey.isGoodsOver,""+goodsId);
    }
}
