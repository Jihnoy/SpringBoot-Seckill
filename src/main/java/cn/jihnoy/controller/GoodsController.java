package cn.jihnoy.controller;

import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.redis.GoodsKey;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.result.Result;
import cn.jihnoy.service.GoodsService;
import cn.jihnoy.service.MiaoshaUserService;
import cn.jihnoy.vo.GoodsDetailVo;
import cn.jihnoy.vo.GoodsVo;
import cn.jihnoy.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toLogin(Model model,
                          MiaoshaUser user, HttpServletRequest request, HttpServletResponse response){
        String html = redisService.get(GoodsKey.getGoodsList, "" ,String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        /*return "goods_list";*/


        //手动渲染
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }
    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toDetail2(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId,
                           HttpServletResponse response, HttpServletRequest request){
        model.addAttribute("user", user);

        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId ,String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        //查询商品列表
        GoodsVo goodsVo = goodsService.goodsById(goodsId);
        model.addAttribute("goods",goodsVo);
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;

        if(now < startAt){
            //倒计时,没开始
            miaoshaStatus = 0;
            remainSeconds = (int)(startAt -now)/1000;
        }else if(now > endAt){
            //结束状态2
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            //正在状态1
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        /*return "goods_detail";*/
        //手动渲染
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId,
                                          HttpServletResponse response, HttpServletRequest request){

        //查询商品列表
        GoodsVo goodsVo = goodsService.goodsById(goodsId);
        model.addAttribute("goods",goodsVo);
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;

        if(now < startAt){
            //倒计时,没开始
            miaoshaStatus = 0;
            remainSeconds = (int)(startAt -now)/1000;
        }else if(now > endAt){
            //结束状态2
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            //正在状态1
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setMiaoshaUser(user);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setGoodsVo(goodsVo);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        return Result.success(goodsDetailVo);
    }
}
