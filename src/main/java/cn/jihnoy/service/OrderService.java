package cn.jihnoy.service;

import cn.jihnoy.dao.OrderDao;
import cn.jihnoy.domain.MiaoshaOrder;
import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.domain.OrderInfo;
import cn.jihnoy.redis.OrderKey;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUidGoodsId(long userId, long goodsId) {
         /*MiaoshaOrder miaoshaOrder = orderDao.getMiaoshaOrderByUidGoodsId(userId, goodsId);
         if(miaoshaOrder != null) return miaoshaOrder;
         return null;*/
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId, MiaoshaOrder.class);

    }

    @Transactional
    public OrderInfo creatOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insertOrderInfo(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());

        orderDao.insertMiaoshaOrder(miaoshaOrder);
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+goods.getId(), miaoshaOrder);

        return orderInfo;

    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
