package com.fc.mapper;

import com.fc.model.Info;
import com.fc.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserMapper {

    void insertUser(User user);

    User selectByUserId(int userId);

    User selectUserByPhoneNumber(String phoneNumber);

    void updateUser(User user);

    void updatePostCount(Integer uid);

    void updateStatus(String userId, String status);

    void updatePassword(@Param("password") String password,@Param("phoneNumber") String phoneNumber);


}
