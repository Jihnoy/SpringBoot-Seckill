package cn.jihnoy.controller;

import cn.jihnoy.domain.MiaoshaOrder;
import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.domain.OrderInfo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

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

    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> doMiaosha(Model model,
                                       MiaoshaUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user == null) {return Result.error(CodeMsg.SESSION_ERROR);}
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
    }

}
