package cn.jihnoy.controller;

import cn.jihnoy.domain.User;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.result.Result;
import cn.jihnoy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Controller
@RequestMapping("/demo")
public class SampleController {
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Joshua");
        return "hello";
    }

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/getuser")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getByid(1);
        return Result.success(user);
    }

    @RequestMapping("/insertUser")
    @ResponseBody
    public Result<Boolean> dbPut(){
        userService.insert();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<Long> redisGet(){
        Long v1 = redisService.get("key1", Long.class);
        System.out.println(v1);
        return Result.success(v1);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<String> redisSet(){
        boolean v1 = redisService.set("key2", "hello????");
        String str = redisService.get("key2", String.class);
        return Result.success(str);
    }

}
