package me.jongwoo.userservice.service;

import me.jongwoo.userservice.domain.Account;
import me.jongwoo.userservice.dto.AccountDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends UserDetailsService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountByAccountId(String accountId);
    List<Account> getAccountAll();

    AccountDto getAccountDetailsByEmail(String userName);

}
