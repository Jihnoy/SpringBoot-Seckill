package cn.jihnoy.controller;

import cn.jihnoy.domain.User;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.result.CodeMsg;
import cn.jihnoy.result.Result;
import cn.jihnoy.service.MiaoshaUserService;
import cn.jihnoy.util.ValidataUtil;
import cn.jihnoy.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        log.info(loginVo.toString());

        miaoshaUserService.Login(response, loginVo);
        return Result.success(true);
    }
}
