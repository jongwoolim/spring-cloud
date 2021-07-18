package me.jongwoo.userservice.service;

import lombok.RequiredArgsConstructor;
import me.jongwoo.userservice.client.OrderServiceClient;
import me.jongwoo.userservice.domain.Account;
import me.jongwoo.userservice.dto.AccountDto;
import me.jongwoo.userservice.repository.AccountRepository;
import me.jongwoo.userservice.vo.ResponseAccount;
import me.jongwoo.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final OrderServiceClient orderServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Account account = accountRepository.findByEmail(username);

        if(account == null){
            throw new UsernameNotFoundException(username);
        }

        return new User(account.getEmail(), account.getEncryptedPwd(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        accountDto.setUserId(UUID.randomUUID().toString());
        final Account account = modelMapper.map(accountDto, Account.class);
        account.setEncryptedPwd(passwordEncoder.encode(accountDto.getPwd()));

        final Account savedAccount = accountRepository.save(account);

        return modelMapper.map(savedAccount, AccountDto.class);
    }

    @Override
    public AccountDto getAccountByAccountId(String accountId) {

        final Account account = accountRepository.findByUserId(accountId);

        if(account == null)
            throw new UsernameNotFoundException("User not found");

        final AccountDto accountDto = modelMapper.map(account, AccountDto.class);
//        List<ResponseOrder> orders = new ArrayList();

        /* using as rest template*/
//        final String orderUrl = String.format(env.getProperty("order_service.url"), accountId);
////        String orderUrl = "http://127.0.0.1:8000/order-service/%s/orders";
//
//        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {});
//
//        final List<ResponseOrder> ordersList = orderListResponse.getBody();

        /* using as feign client */
        List<ResponseOrder> ordersList = orderServiceClient.getOrders(accountId);

        accountDto.setOrders(ordersList);

        return accountDto;
    }

    @Override
    public List<Account> getAccountAll() {
        return accountRepository.findAll();
    }

    @Override
    public AccountDto getAccountDetailsByEmail(String email) {
        Account account = accountRepository.findByEmail(email);

        if(account == null)
            throw new UsernameNotFoundException(email);
        final AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        return accountDto;
    }

}
