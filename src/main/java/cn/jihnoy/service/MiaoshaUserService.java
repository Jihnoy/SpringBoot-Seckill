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
        //取缓存
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if(miaoshaUser != null){
            return miaoshaUser;
        }
        //查数据库
        MiaoshaUser user =  miaoshaUserDao.getById(id);
        if(user != null){
            return user;
        }
        redisService.set(MiaoshaUserKey.getById,""+id, user);
        return user;
    }

    public boolean updatePassword(String token, long id, String password){
        //取user
        MiaoshaUser user = getById(id);
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(password, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);
        return true;
    }

    public boolean Login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if(miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass = miaoshaUser.getPassword();
        String salt = miaoshaUser.getSalt();
        String pass = MD5Util.formPassToDBPass(formPass, salt);
        System.out.println(formPass);
        System.out.println(pass);
        System.out.println(dbPass);
        if(!pass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        //生成cookie
        addACookie(miaoshaUser, token,response);
        return true;
    }

    public MiaoshaUser getByToken(String token, HttpServletResponse response) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if(miaoshaUser != null){
            addACookie(miaoshaUser, token,response);
        }

        return miaoshaUser;
    }

    private void addACookie(MiaoshaUser miaoshaUser,String token, HttpServletResponse response){

        redisService.set(MiaoshaUserKey.token, token, miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
