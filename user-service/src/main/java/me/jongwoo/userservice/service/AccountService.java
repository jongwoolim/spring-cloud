package me.jongwoo.userservice.service;

import me.jongwoo.userservice.domain.Account;
import me.jongwoo.userservice.dto.AccountDto;

import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountByAccountId(String accountId);
    List<Account> getAccountAll();
}
