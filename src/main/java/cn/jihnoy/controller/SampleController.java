package cn.jihnoy.controller;

import cn.jihnoy.domain.User;
import cn.jihnoy.rabbitmq.MQSender;
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

    @Autowired
    MQSender sender;

   /* @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        sender.send("hello lcn");
        return Result.success("hello, world");
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic(){
        sender.sendTopic("hello lcn");
        return Result.success("hello, world");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout(){
        sender.sendFanout("hello lcn");
        return Result.success("hello, world");
    }

    @RequestMapping("/mq/headers")
    @ResponseBody
    public Result<String> headers(){
        sender.sendHeaders("hello lcn");
        return Result.success("hello, world");
    }*/
}
