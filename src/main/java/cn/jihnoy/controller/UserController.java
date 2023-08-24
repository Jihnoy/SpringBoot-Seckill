package cn.jihnoy.controller;

import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.result.Result;
import cn.jihnoy.service.GoodsService;
import cn.jihnoy.service.MiaoshaUserService;
import cn.jihnoy.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model,
                          MiaoshaUser user){
        log.info(String.valueOf(user.getId()));
        return Result.success(user);
    }

}
