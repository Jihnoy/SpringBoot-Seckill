package cn.jihnoy.controller;

import cn.jihnoy.domain.User;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> error(){
        return null;
    }
}
