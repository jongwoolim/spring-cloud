package me.jongwoo.userservice.controller;

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

    @PostMapping("/users")
    public ResponseEntity<ResponseAccount> createUser(@RequestBody RequestAccount account){
        final AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        final AccountDto savedAccount = accountService.createAccount(accountDto);

        ResponseAccount responseAccount = modelMapper.map(savedAccount, ResponseAccount.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseAccount);
    }


    @GetMapping("/health_check")
    public String status(){
        return "It`s Working in User Service";
    }

//    @GetMapping("/welcome")
//    public String welcome(){
//        return env.getProperty("greeting.message");
//    }

    @GetMapping("/welcome")
    public String welcome(){
        return greeting.getMessage();
    }
}
