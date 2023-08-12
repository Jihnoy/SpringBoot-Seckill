package cn.jihnoy.service;

import cn.jihnoy.dao.GoodsDao;
import cn.jihnoy.domain.Goods;
import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.domain.OrderInfo;
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

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存，下订单，写入秒杀订单
        goodsService.reduceStock(goods);
        //order_info, miaosha_order
        OrderInfo orderInfo = orderService.creatOrder(user, goods);


        return orderInfo;
    }
}
