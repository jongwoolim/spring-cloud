package me.jongwoo.userservice.service;

import me.jongwoo.userservice.dto.AccountDto;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);
}
