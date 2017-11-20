package com.fc.mapper;

import com.fc.model.SubService;

import java.util.List;

public interface SubServiceMapper {
    void insertSubService(SubService service);

    SubService selectByServiceId(String serviceId);

    List<SubService> selectAll();

    void updateLogoutAll(String serviceId, int logoutAll);

    void deleteByServiceId(String serviceId);

    void deleteAll();

}
