package com.wugui.dataxweb.controller;

import com.wugui.dataxweb.dto.LogOffDTO;
import com.wugui.dataxweb.entity.SystemLogEntity;
import com.wugui.dataxweb.entity.UserEntity;
import com.wugui.dataxweb.log.OperateLog;
import com.wugui.dataxweb.service.SystemLogService;
import com.wugui.dataxweb.service.UserService;
import com.wugui.dataxweb.util.KlksRedisUtils;
import com.wugui.dataxweb.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
@Api(tags = "退出接口")
public class LogoutController extends BaseController{

    private UserService userService;

    @Autowired
    private KlksRedisUtils redisUtils;
    @Autowired
    private SystemLogService systemLogService;

    @ApiOperation(value = "系统退出", notes = "系统退出")
    @PostMapping("/do")
    public ResponseData logout(@Validated @RequestBody LogOffDTO info, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return responseFormError(bindingResult);
        }
        logger.info("【注销接口】入参:{}", info);
        UserEntity user = getCurrentUser();
        if (user == null) {
            return responseError("未找到此用户！");
        }
        SystemLogEntity systemLogEntity = new SystemLogEntity();
        systemLogEntity.setName(user.getName());
        systemLogEntity.setUserId(user.getId());
        systemLogEntity.setIp(getIpAddress(request));
        systemLogEntity.setOperation("退出登录");
        systemLogEntity.setCreateUserId(user.getId());
        systemLogService.save(systemLogEntity);
        return response(redisUtils.deleteToken(user.getId().toString()));
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
