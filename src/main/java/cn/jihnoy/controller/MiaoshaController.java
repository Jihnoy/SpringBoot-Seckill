package cn.jihnoy.controller;

import cn.jihnoy.domain.MiaoshaOrder;
import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.domain.OrderInfo;
import cn.jihnoy.rabbitmq.MQSender;
import cn.jihnoy.rabbitmq.MiaoshaMessage;
import cn.jihnoy.redis.GoodsKey;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.result.CodeMsg;
import cn.jihnoy.result.Result;
import cn.jihnoy.service.GoodsService;
import cn.jihnoy.service.MiaoshaService;
import cn.jihnoy.service.MiaoshaUserService;
import cn.jihnoy.service.OrderService;
import cn.jihnoy.vo.GoodsVo;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    private static Logger log = LoggerFactory.getLogger(MiaoshaController.class);

    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    MQSender mqSender;

    public void afterPropertiesSet() throws Exception{
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if(goodsVoList == null) return;
        for(GoodsVo goodsVo:goodsVoList){
            redisService.set(GoodsKey.getGoodsStock, ""+goodsVo.getId(), goodsVo.getStockCount());
        }
    }

    @RequestMapping(value="/do_miaosha", method=RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model,MiaoshaUser user,
                                   @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //内存标记，减少redis访问
        //预减库存
        long stock = redisService.decr(GoodsKey.getGoodsStock, ""+goodsId);//10
        if(stock < 0) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUidGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.MIAOSHA_REPEAT);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setMiaoshaUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);
        return Result.success(0);//排队中
    	/*
    	//判断库存
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);//10个商品，req1 req2
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
    		return Result.error(CodeMsg.MIAO_SHA_OVER);
    	}
    	//判断是否已经秒杀到了
    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		return Result.error(CodeMsg.REPEATE_MIAOSHA);
    	}
    	//减库存 下订单 写入秒杀订单
    	OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        return Result.success(orderInfo);
        */
    }

    /*@RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> doMiaosha(Model model,
                                     MiaoshaUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null) {return Result.error(CodeMsg.SESSION_ERROR);}
        *//*long stock = redisService.decr(GoodsKey.getGoodsStock, ""+goodsId);
        //预减库存
        if(stock < 0){
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUidGoodsId(user.getId(), goodsId);
        if(miaoshaOrder != null){
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_REPEAT.getMsg());
            return Result.error(CodeMsg.MIAOSHA_REPEAT);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setMiaoshaUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);
        log.info("完毕");
        return Result.success(0);//0代表排队中。*//*
        GoodsVo goodsVo = goodsService.goodsById(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0){
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_OVER.getMsg());
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //有库存,判断是否已经秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUidGoodsId(user.getId(), goodsId);
        if(miaoshaOrder != null){
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_REPEAT.getMsg());
            return Result.error(CodeMsg.MIAOSHA_REPEAT);
        }
        //开始秒杀，1减库存，2下订单，3加入秒杀订单

        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);

        return Result.success(orderInfo);
    }*/

    /*
    * order Id= success
    * -1: fail
    * 0: in queue
    * */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,
                                     MiaoshaUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null) {return Result.error(CodeMsg.SESSION_ERROR);}
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);//0代表排队中。
    }

}
