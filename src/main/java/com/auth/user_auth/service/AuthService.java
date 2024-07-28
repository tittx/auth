package com.auth.user_auth.service;

import java.util.Map;
import java.util.Objects;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.auth.share.entity.Result;
import com.auth.user_auth.model.Account;
import com.auth.user_auth.model.Role;
import com.auth.user_auth.model.Session;
import com.auth.user_auth.repository.SessionRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class AuthService {
    private final BCryptPasswordService bcryptPasswordSerivce;
    private final SessionRepository sessionRepository;
    private final Environment env;

    @AllArgsConstructor
    @Getter
    public class AccountAuthorized {
        private long accountId;
        private Role role;
    }

    public Result<?> authenticate(Account account, String inputPassword) {
        return this.bcryptPasswordSerivce.validateEncodedPassword(inputPassword, account.getPassword()) 
        ? Result.success(null) 
        : Result.failed(null);
    }

    public Result<?> authorize(String accessToken) {
        Map<String, Object> userData = Session.decodeAccessToken(accessToken, env.getProperty("auth.secret"));
        long sessionId = Long.valueOf(userData.get("session").toString());
        Session session = this.sessionRepository.findById(sessionId);
        if (session.isExpired()) {
            return Result.failed("Session is expired");
        };
        long accountId = Long.valueOf(userData.get("accountId").toString());
        Role role = Objects.equals(userData.get("role").toString(), String.valueOf(Role.USER)) ? Role.USER : Role.ADMIN;
        AccountAuthorized account = new AccountAuthorized(
            accountId,
            role
        );
        return Result.success(account);
    }
}
