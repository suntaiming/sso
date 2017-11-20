package com.fc.controller;

import com.fc.domain.GrantingTicket;
import com.fc.form.response.GetServiceTicketResponse;
import com.fc.form.response.LoginResponse;
import com.fc.form.response.ResponseMsg;
import com.fc.model.User;
import com.fc.service.GrantingTicketService;
import com.fc.service.ServiceTicketService;
import com.fc.service.SmsService;
import com.fc.service.UserService;
import com.fc.util.GsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.regex.Pattern;

import static com.fc.core.Code.*;
/**
 * 用户登录、认证相关api
 */
@RequestMapping("/terminal/")
@Controller
public class UserAuthenticationController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserService userService;
    @Autowired
    GrantingTicketService grantingTicketService;
    @Autowired
    ServiceTicketService serviceTicketService;
    @Autowired
    SmsService smsService;

    public UserAuthenticationController() {
        System.out.println("初始化UserAuthenticationController。。。。。。");
    }

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(){
        return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
    }


    @RequestMapping(value = "/api/v1/{cmd}", produces="text/html;charset=UTF-8")
    @ResponseBody
    public String terminalApi(@PathVariable(value = "cmd") String cmd, String reqMsg){


        if(StringUtils.isBlank(reqMsg)){
            return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
        }

        String responseMsg = handleApi(cmd, reqMsg);

        return responseMsg;



    }
    public String handleApi(String cmd, String reqMsg){
        String responseMsg = "";
        switch (cmd) {
            case "login":{
                logger.info("登录请求：{}", reqMsg);
                responseMsg = login(reqMsg);
                logger.info("登录响应：{}", responseMsg);
                break;
            }

            case "register":{
                logger.info("注册请求：{}", reqMsg);
                responseMsg = register(reqMsg);
                logger.info("注册响应：{}", responseMsg);
                break;
            }
            case "acquireServiceTicket":{
                logger.info("获取ST临时凭证请求：{}", reqMsg);
                responseMsg = acquireServiceTicket(reqMsg);
                logger.info("获取ST临时凭证响应：{}", responseMsg);
                break;
            }

            case "sendSms":{
                logger.info("请求发送短信验证码请求：{}", reqMsg);
                responseMsg = sendSms(reqMsg);
                logger.info("请求发送短信验证码响应：{}", responseMsg);
                break;
            }

            case "verifyCode":{
                logger.info("校验短信验证码请求：{}", reqMsg);
                responseMsg = verifyCode(reqMsg);
                logger.info("校验短信验证码响应：{}", responseMsg);
                break;
            }

            case "resetPassword":{
                logger.info("重置密码请求：{}", reqMsg);
                responseMsg = resetPassword(reqMsg);
                logger.info("重置密码响应：{}", responseMsg);
                break;
            }
            case "logout":{
                logger.info("重置密码请求：{}", reqMsg);
                responseMsg = logout(reqMsg);
                logger.info("重置密码响应：{}", responseMsg);
                break;
            }

            default:
                logger.info("请求：{}", reqMsg);
                responseMsg = GsonUtil.toJson(new ResponseMsg(CODE_UNKNOW_CMD, "指令不存在"));
                logger.info("响应：{}", responseMsg);
                break;
        }

        return responseMsg;
    }

    /**
     * 登录接口
     * @param reqMsg
     * @return
     */
    public String login(String reqMsg){
        Map<String, Object> params = GsonUtil.toMap(reqMsg);
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        String serviceId = (String) params.get("serviceId");
        if(!verifyNullParams(username, password)){
            return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
        }
        User user = userService.getByPhoneNumber(username);
        if(user == null){
            return GsonUtil.toJson(new ResponseMsg(CODE_NO_USER, "用户不存在"));
        }

        if(!user.getPassword().equals(password)){
            return GsonUtil.toJson(new ResponseMsg(CODE_PW_FAIL, "密码不正确"));
        }

        String ticketGrantingCookie = grantingTicketService.awardTGT(user.getUserId());
        String serviceTicket = null;
        if(StringUtils.isNotBlank(serviceId)){
            serviceTicket = serviceTicketService.awardST(user.getUserId(), serviceId);
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setServiceId(serviceId);
        loginResponse.setServiceTicket(serviceTicket);
        loginResponse.setTicketGrantingCookie(ticketGrantingCookie);

        return GsonUtil.toJson(new ResponseMsg(CODE_SUCCESS, "", loginResponse));
    }

    /**
     * 注册接口
     * @param reqMsg
     * @return
     */
    public String register(String reqMsg){
        User user = GsonUtil.fromJson(reqMsg, User.class);

        if(user == null || !verifyNullParams(user.getUsername(), user.getPassword(), user.getSn())){
            return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
        }

        if(!isMobile(user.getUsername())){
            return GsonUtil.toJson(new ResponseMsg(CODE_PHONE_FAIL, "手机号格式不正确"));
        }

        User originUser = userService.getByPhoneNumber(user.getUsername());
        if(originUser != null){
            return GsonUtil.toJson(new ResponseMsg(CODE_REPEAT_REGISTER, "该手机号已注册"));
        }


        user.setPhoneNumber(user.getUsername());

        try {
            userService.register(user);
        } catch (Exception e) {
            e.printStackTrace();

            logger.info("注册失败：{}", e.getMessage());
            return GsonUtil.toJson(new ResponseMsg(CODE_UNKNOW_ERRO, "未知错误"));
        }


        return GsonUtil.toJson(new ResponseMsg(CODE_SUCCESS, ""));
    }


    /**
     * 请求发送送验证码
     * @param reqMsg
     * @return
     */
    public String sendSms(String reqMsg){
        Map<String, Object> params = GsonUtil.toMap(reqMsg);
        String phoneNumber = String.valueOf(params.get("phoneNumber")).replace(" ", "");
        if(!isMobile(phoneNumber)){
            return GsonUtil.toJson(new ResponseMsg(CODE_PHONE_FAIL, "手机号格式不正确"));
        }

        boolean isSuccess = smsService.sendSms(phoneNumber);
        if(isSuccess){
            logger.info("验证码已发送到手机号为：{}的用户", phoneNumber);
        }else{
            logger.info("验证码发送失败，手机号为：{}", phoneNumber);
        }

        return GsonUtil.toJson(new ResponseMsg(CODE_SUCCESS, ""));
    }

    /**
     * 校验手机验证码是否正确
     * @param reqMsg
     * @return
     */
    public String verifyCode(String reqMsg){
        Map<String, Object> params = GsonUtil.toMap(reqMsg);
        String phoneNumber = String.valueOf(params.get("phoneNumber")).replace(" ", "");
        String verifyCode = String.valueOf(params.get("verifyCode"));
        if(StringUtils.isBlank(phoneNumber) || StringUtils.isBlank(verifyCode)){
            return GsonUtil.toJson(new ResponseMsg(CODE_VERIFY_CODE_FAIL, "手机验证码不正确"));
        }

        if(!smsService.verifyCode(phoneNumber, verifyCode)){
            return GsonUtil.toJson(new ResponseMsg(CODE_VERIFY_CODE_FAIL, "手机验证码不正确"));
        }

        return GsonUtil.toJson(new ResponseMsg(CODE_SUCCESS, ""));

    }


    /**
     * 重置密码
     * @param reqMsg
     * @return
     */
    public String resetPassword(String reqMsg){
        Map<String, Object> params = GsonUtil.toMap(reqMsg);
        String phoneNumber = (String)params.get("phoneNumber");
        String password = (String)params.get("password");
        if(!verifyNullParams(phoneNumber, password)){
            return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
        }

        User user = userService.getByPhoneNumber(phoneNumber);
        if(user == null){
            return GsonUtil.toJson(new ResponseMsg(CODE_NO_USER, "该用户不存在"));
        }
        userService.updatePassword(phoneNumber, password);

        return GsonUtil.toJson(new ResponseMsg(CODE_SUCCESS, ""));
    }


    /**
     * 退出登录接口
     * @param reqMsg
     * @return
     */
    public String logout(String reqMsg){
        Map<String, Object> params = GsonUtil.toMap(reqMsg);
        String userId = (String) params.get("userId");
        if(!verifyNullParams(userId)){
            return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
        }

        userService.logout(userId, null);

        return GsonUtil.toJson(new ResponseMsg(CODE_SUCCESS, ""));
    }

    /**
     * 获取ST临时登录凭证
     * @param reqMsg
     * @return
     */
    public String acquireServiceTicket(String reqMsg){
        Map<String, Object> params = GsonUtil.toMap(reqMsg);
        String serviceId = (String) params.get("serviceId");
        String ticketGrantingCookie = (String) params.get("ticketGrantingCookie");
        if(!verifyNullParams(serviceId, ticketGrantingCookie)){
            return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
        }

        GrantingTicket grantingTicket = grantingTicketService.getGrantingTicket(ticketGrantingCookie);
        if(grantingTicket == null){
            return GsonUtil.toJson(new ResponseMsg(CODE_VERIFY_TGC_FAIL, "TGC授权凭证验证失败"));
        }
        String st = serviceTicketService.awardST(grantingTicket.getUserId(), serviceId);

        GetServiceTicketResponse response = new GetServiceTicketResponse();
        response.setServiceId(serviceId);
        response.setServiceTicket(st);
        response.setUserId(grantingTicket.getUserId());

        return GsonUtil.toJson(new ResponseMsg(CODE_SUCCESS, "", response));
    }



    private boolean verifyNullParams(String... params){
        if(params == null || params.length == 0){
            return false;
        }
        for (String param : params){
            if(StringUtils.isBlank(param)){
                return false;
            }
        }
        return true;
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }


}
