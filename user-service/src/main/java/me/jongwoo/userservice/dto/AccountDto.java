package me.jongwoo.userservice.dto;

import lombok.Data;
import me.jongwoo.userservice.vo.ResponseOrder;

import java.util.Date;
import java.util.List;

@Data
public class AccountDto {

    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String encryptedPwd;

    private List<ResponseOrder> orders;
}
