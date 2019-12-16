package com.wugui.dataxweb.controller;


import com.wugui.dataxweb.entity.UserEntity;
import com.wugui.dataxweb.service.UserService;
import com.wugui.dataxweb.util.KlksRedisUtils;
import com.wugui.dataxweb.vo.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    HttpServletRequest request;

    @Autowired
    private KlksRedisUtils redis;

    @Autowired
    private UserService userService;

    StringRedisTemplate template;

    <T> ResponseData<T> response(T data, String message, Integer code){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setCode(code);
        responseData.setData(data);
        responseData.setMessage(message);
        return responseData;
    }

    ResponseData<String> response() {
        return response("OK", "OK", 200);
    }

    <T> ResponseData<T> response(T data) {
        return response(data, "OK", 200);
    }

    <T> ResponseData<T> responseError(String message) {
        return responseError(message, 400);
    }

    <T> ResponseData<T> responseError(String message, Integer code) {
        return response(null, message, code);
    }

    ResponseData<List<ObjectError>> responseFormError(BindingResult bindingResult) {
        String defaultMessage = bindingResult.getAllErrors().iterator().next().getDefaultMessage();
        return response(bindingResult.getAllErrors(), defaultMessage, 400);
    }

    UserEntity getCurrentUser() {
//        return authenticationFacade.getUser();
        HttpSession session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        UserEntity userInfo = redis.getUserInfoFromCache(userId);
        if (userInfo == null) {
            userInfo = userService.getById(userId);
            redis.saveUserInfoToCache(userId, userInfo);
        }
        return userInfo;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }


    @Autowired
    public void setTemplate(StringRedisTemplate template){
        this.template = template;
    }
}
