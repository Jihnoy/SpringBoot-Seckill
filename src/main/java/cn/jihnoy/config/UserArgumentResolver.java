package cn.jihnoy.config;

import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoshaUserService miaoshaUserService;
    public boolean supportsParameter(MethodParameter parameter){
        Class<?> clazz = parameter.getParameterType();
        return clazz == MiaoshaUser.class;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
                           NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception{
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = httpServletRequest.getParameter(MiaoshaUserService.COOKIE_NAME);
        String cookieToken = getCookieValue(httpServletRequest, MiaoshaUserService.COOKIE_NAME);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)? cookieToken : paramToken;
        MiaoshaUser user = miaoshaUserService.getByToken(token, httpServletResponse);
        return user;
    }

    private String getCookieValue(HttpServletRequest httpServletRequest, String cookieName) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies == null || cookies.length <= 0){return null;}
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }


}
