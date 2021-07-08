package me.jongwoo.userservice.service;

import lombok.RequiredArgsConstructor;
import me.jongwoo.userservice.domain.Account;
import me.jongwoo.userservice.dto.AccountDto;
import me.jongwoo.userservice.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        accountDto.setUserId(UUID.randomUUID().toString());
        final Account account = modelMapper.map(accountDto, Account.class);
        account.setEncryptedPwd("encrypted_password");

        final Account savedAccount = accountRepository.save(account);

        return modelMapper.map(savedAccount, AccountDto.class);
    }
}
