package me.jongwoo.userservice.controller;

import io.micrometer.core.annotation.Timed;
import me.jongwoo.userservice.domain.Account;
import me.jongwoo.userservice.dto.AccountDto;
import me.jongwoo.userservice.service.AccountService;
import me.jongwoo.userservice.vo.Greeting;
import me.jongwoo.userservice.vo.RequestAccount;
import me.jongwoo.userservice.vo.ResponseAccount;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class AccountController {

    private Environment env;
    private AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Greeting greeting;

    public AccountController(Environment env, AccountService accountService) {
        this.env = env;
        this.accountService = accountService;
    }

    @GetMapping("/users/{userId}")
    public ResponseAccount getUsers(@PathVariable String userId){
        AccountDto accountDto = accountService.getAccountByAccountId(userId);

        return modelMapper.map(accountDto, ResponseAccount.class);
    }

    @GetMapping("/users")
    public List<ResponseAccount> getUsers(){
        final List<Account> accountList = accountService.getAccountAll();
        List<ResponseAccount> result = new ArrayList<>();
        accountList.forEach(e -> {
            final ResponseAccount resAccount = modelMapper.map(e, ResponseAccount.class);
            result.add(resAccount);
        });
        return result;
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseAccount> createUser(@RequestBody RequestAccount account){
        final AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        final AccountDto savedAccount = accountService.createAccount(accountDto);

        ResponseAccount responseAccount = modelMapper.map(savedAccount, ResponseAccount.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseAccount);
    }


    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    public String status(){
//        return String.format("It`s Working in User Service POrT %s", env.getProperty("local.server.port"));
        return String.format("It`s Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time")
        );
    }

//    @GetMapping("/welcome")
//    public String welcome(){
//        return env.getProperty("greeting.message");
//    }

    @GetMapping("/welcome")
    @Timed(value = "users.welcome", longTask = true)
    public String welcome(){
        return greeting.getMessage();
    }
}
