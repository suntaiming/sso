package com.fc.controller;

import com.fc.core.CoreConfig;
import com.fc.form.response.ResponseMsg;
import com.fc.service.GrantingTicketService;
import com.fc.service.ServiceTicketService;
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

import static com.fc.core.Code.*;

/**import com.fc.service.UserACService;

 * 在认证中心注册过的客户端，鉴定ticket相关api
 */
@Controller
@RequestMapping("/client")
public class ClientAuthenticationControlller {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserService userService;

    @Autowired
    GrantingTicketService grantingTicketService;
    @Autowired
    ServiceTicketService serviceTicketService;
    @RequestMapping(value = "/api/v1/{cmd}", produces = "text/html;charset=UTF-8")
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
            case "verifyServiceTicket":{
                logger.info("验证ST临时凭证是否合法请求：{}", reqMsg);
                responseMsg = verifyServiceTicket(reqMsg);
                logger.info("验证ST临时凭证是否合法响应：{}", responseMsg);
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
     * 验证ST临时凭证是否合法
     * @param reqMsg
     * @return
     */
    public String verifyServiceTicket(String reqMsg){
        Map<String, Object> params = GsonUtil.toMap(reqMsg);
        if(params == null || params.isEmpty()){
            return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
        }
        String serviceTicket = (String) params.get("serviceTicket");
        String serviceId = (String) params.get("serviceId");
        String serviceSecret = (String) params.get("serviceSecret");

        boolean verifyResult = serviceTicketService.verifyST(serviceTicket);

        if(!verifyResult){
            return GsonUtil.toJson(new ResponseMsg(CODE_VERIFY_ST_FAIL, "ST临时凭证校验失败"));
        }

        return GsonUtil.toJson(new ResponseMsg(CODE_SUCCESS, ""));

    }

    public String logout(String reqMsg){
        Map<String, Object> params = GsonUtil.toMap(reqMsg);
        if(params == null || params.isEmpty()){
            return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
        }

        String userId = (String) params.get("userId");

        if(StringUtils.isBlank(userId)){
            return GsonUtil.toJson(new ResponseMsg(CODE_PARAMS_ERR, "参数错误"));
        }

        if(CoreConfig.IS_ALLOW_SUBSERVICE_LOGOUT){
            //todo 销毁全局会话，并通知其他子服务销毁该用户的局部会话

        }

        return GsonUtil.toJson(new ResponseMsg(CODE_SUCCESS, ""));


    }

}
