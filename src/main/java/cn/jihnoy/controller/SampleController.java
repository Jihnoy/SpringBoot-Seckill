package cn.jihnoy.controller;

import cn.jihnoy.domain.User;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.redis.UserKey;
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
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById, ""+1, user);
        return Result.success(true);
    }

}
