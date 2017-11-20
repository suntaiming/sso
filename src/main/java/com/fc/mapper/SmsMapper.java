package com.fc.mapper;

import com.fc.model.Sms;
import com.fc.model.User;
import org.apache.ibatis.annotations.Param;


public interface SmsMapper {

    Sms getSms(String phoneNumber);

    void updateSms(Sms sms);

    void addSms(Sms sms);

    void deleteSms(String phoneNumber);

}
