package cn.jihnoy.service;

import cn.jihnoy.dao.MiaoshaUserDao;
import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.exception.GlobalException;
import cn.jihnoy.redis.MiaoshaUserKey;
import cn.jihnoy.redis.RedisService;
import cn.jihnoy.result.CodeMsg;
import cn.jihnoy.util.MD5Util;
import cn.jihnoy.util.UUIDUtil;
import cn.jihnoy.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME = "token";
    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;
    public MiaoshaUser getById(Long id){
        return miaoshaUserDao.getById(id);
    }

    public boolean Login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if(miaoshaUser == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass = miaoshaUser.getPassword();
        String salt = miaoshaUser.getSalt();
        String pass = MD5Util.formPassToDBPass(formPass, salt);
        System.out.println(pass);
        System.out.println(dbPass);
        if(!pass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        addACookie(miaoshaUser, response);
        return true;
    }

    public MiaoshaUser getByToken(String token, HttpServletResponse response) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if(miaoshaUser != null){
            addACookie(miaoshaUser, response);
        }

        return miaoshaUser;
    }

    private void addACookie(MiaoshaUser miaoshaUser, HttpServletResponse response){
        String token = UUIDUtil.uuid();
        redisService.set(MiaoshaUserKey.token, token, miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
