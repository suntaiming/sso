package com.fc.service;

import com.fc.async.MailTask;
import com.fc.mapper.UserMapper;
import com.fc.model.Info;
import com.fc.model.SubService;
import com.fc.model.User;
import com.fc.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    ServiceTicketService serviceTicketService;
    @Autowired
    GrantingTicketService grantingTicketService;

    @Autowired
    SubServiceService subServiceService;

    public void register(User user) throws Exception{
        if(user == null){
            throw new NullPointerException("UserService--register方法参数为空");
        }
        if(StringUtils.isBlank(user.getUserId())){
            user.setUserId(IdGenerator.nextUuid());
        }
        user.setRegisterTime(new Date());
        userMapper.insertUser(user);
    }

    public User getByPhoneNumber(String phoneNumber){

        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }



    public void updatePassword(String username, String password){
        userMapper.updatePassword(password, username);
    }
    /**
     * 退出登录
     * @param userId
     * @param fromServiceId
     */
    public void logout(String userId, String fromServiceId){
        if(StringUtils.isBlank(userId)){
            return ;
        }

        SubService subService = null;
        if(StringUtils.isNotBlank(fromServiceId)){
            subService = subServiceService.getService(fromServiceId);
            if(subService.getLogoutAll() == SubService.LOGOUTALL_NO){
                return ;
            }
        }

        //销毁全局会话
        String tgc = grantingTicketService.getTGC(userId);
        grantingTicketService.removeTGT(tgc);
        grantingTicketService.removeTGC(userId);
        serviceTicketService.removeSTByUserId(userId);

        List<SubService> subServices = subServiceService.findAll();
        for(SubService service : subServices){
            if(service.getServiceId().equals(fromServiceId)){
                continue;
            }
            String logoutUrl = service.getLogoutUrl();
            //todo 通知子服务登出
            System.out.println("通知子服务登出....");
        }

    }

}

