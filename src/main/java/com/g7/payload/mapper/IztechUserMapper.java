package com.g7.payload.mapper;

import com.g7.model.IztechUser;
import com.g7.payload.response.IztechUserResponse;
import org.springframework.stereotype.Component;

@Component
public class IztechUserMapper {

    public IztechUserResponse toResponse(IztechUser iztechUser) {
        IztechUserResponse response = new IztechUserResponse();
        response.setName(iztechUser.getName());
        response.setSurname(iztechUser.getSurname());
        response.setGrade(iztechUser.getGrade());
        response.setGpa(iztechUser.getGpa());
        response.setEmail(iztechUser.getEmail());
        response.setRole(iztechUser.getRole());
        response.setPhoneNumber(iztechUser.getPhoneNumber());
        response.setStudentNo(iztechUser.getStudentNo());
        return response;
    }
}
