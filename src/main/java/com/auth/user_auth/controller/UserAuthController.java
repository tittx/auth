package com.auth.user_auth.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.auth.share.entity.Result;
import com.auth.user_auth.model.Role;
import com.auth.user_auth.service.LoginService;
import com.auth.user_auth.service.RegisterService;
import com.auth.user_auth.service.AuthService.AccountAuthorized;

import com.auth.user_auth.service.AuthService;


@RestController
public class UserAuthController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private AuthService authService;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {
        public String username;
        public String email;
        public String password;
    }

    @PostMapping(value = "/login")
    public Result<?> login(@RequestBody LoginService.Input loginInput){
            return loginService.login(loginInput);
    }

    @PostMapping(value = "/register")
    public Result<?> register(@RequestBody Input input){
        RegisterService.Input inputObject = new RegisterService.Input(input.username,input.email,input.password, Role.USER);
        return registerService.register(inputObject);
    }
    @PostMapping(value = "register/admin")
    public Result<?> registerAdmin(@RequestHeader("access-token") String accessToken, @RequestBody Input input) {
        Result<?> authorize = this.authService.authorize(accessToken);
        if (!authorize.isSuccess()) {
            return Result.failed(authorize.getData());
        }
        AccountAuthorized account = (AccountAuthorized) authorize.getData();
        if (account.getRole().equals(Role.ADMIN)){
            RegisterService.Input inputObject = new RegisterService.Input(input.username,input.email,input.password, Role.ADMIN);
            return registerService.register(inputObject);
        }
        return Result.failed("Only admin can call this api");
    }
}
