package com.fc.core;

import com.fc.model.SubService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class SubServiceTemplate {

    /**
     * 牛盾密语 服务注册信息
     */
    public final static SubService  VOIP  = new SubService("VOIP", "VOIPxindun123", SubService.LOGOUTALL_YES);

    /**
     * 牛盾输入法  服务注册信息
     */
    public final static SubService PINYINIME = new SubService("Pinyinime", "Pinyinimexindun123", SubService.LOGOUTALL_YES);

    /**
     *  牛盾安全平台 服务注册信息
     */
    public final static SubService SAFETYPLATFROM = new SubService("SafetyPlatfrom", "SafetyPlatfromxindun123", SubService.LOGOUTALL_YES);

    /**
     *  牛盾对讲 服务注册信息
     */
    public final static SubService POC = new SubService("POC", "POCxindun123", SubService.LOGOUTALL_YES);
    public  static List<SubService> subServices =  new ArrayList<>();

    static{


        Date nowTime = new Date();
        //牛盾密语
        VOIP.setLogoutUrl("");
        VOIP.setRegisterTime(nowTime);
        VOIP.setModifyTime(nowTime);
        subServices.add(VOIP);

        //牛盾输入法
        PINYINIME.setLogoutUrl("");
        PINYINIME.setRegisterTime(nowTime);
        PINYINIME.setModifyTime(nowTime);
        subServices.add(PINYINIME);

        //牛盾安全平台
        SAFETYPLATFROM.setLogoutUrl("");
        SAFETYPLATFROM.setRegisterTime(nowTime);
        SAFETYPLATFROM.setModifyTime(nowTime);
        subServices.add(SAFETYPLATFROM);

        //牛盾对讲
        POC.setLogoutUrl("");
        POC.setRegisterTime(nowTime);
        POC.setModifyTime(nowTime);
        subServices.add(POC);




    }


}
