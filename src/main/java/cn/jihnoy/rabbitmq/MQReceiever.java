package cn.jihnoy.rabbitmq;



import cn.jihnoy.domain.MiaoshaOrder;
import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.result.CodeMsg;
import cn.jihnoy.result.Result;
import cn.jihnoy.service.GoodsService;
import cn.jihnoy.service.MiaoshaService;
import cn.jihnoy.service.MiaoshaUserService;
import cn.jihnoy.service.OrderService;
import cn.jihnoy.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiever {
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;

    private static Logger log = LoggerFactory.getLogger(MQReceiever.class);
    /*@RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message){
        log.info("receive message: " + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message){
        log.info("receive topic queue1 message: " + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message){
        log.info("receive topic queue2 message: " + message);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiveTopic2(byte[] message){
        log.info("headers topic queue2 message: " + new String(message) );
    }*/
    @RabbitListener(queues = MQConfig.MS_QUEUE)
    public void receiveTopic1(String message){
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getMiaoshaUser();
        long goodsId = mm.getGoodsId();
        GoodsVo goodsVo = goodsService.goodsById(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0){
            return;
        }
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUidGoodsId(user.getId(), goodsId);
        if(miaoshaOrder != null){
            return;
        }
        miaoshaService.miaosha(user, goodsVo);
        log.info("receive message: " + message);
    }
}
