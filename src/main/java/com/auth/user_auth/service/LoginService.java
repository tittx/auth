package com.auth.user_auth.service;

import com.auth.share.entity.Result;
import com.auth.user_auth.model.Account;
import com.auth.user_auth.model.Role;
import com.auth.user_auth.model.Session;
import com.auth.user_auth.repository.AccountRepository;
import com.auth.user_auth.repository.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class LoginService {
    private final AuthService authService;
    private final AccountRepository accountRepository;
    private final SessionRepository sessionRepository;
    private final Environment env;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Input {
        private String username;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Output {
        private String accessToken;
        private long accountId;
        private Role role;
    }

    public Result<?> login(Input input) {
        Account account = this.accountRepository.findByUsername(input.username);
        if (account == null) {
            return Result.failed("This account not found");
        }
        Result<?> isAuthenticated = this.authService.authenticate(account, input.getPassword());
        if(!isAuthenticated.isSuccess()) {
            return Result.failed("Wrong password");
        }
        Session session = new Session(account.getId(), account.getRole(), Instant.now().plus(2,  ChronoUnit.HOURS));
        String accessToken = session.genAccessToken(env.getProperty("auth.secret"));
        this.sessionRepository.save(session);
        return Result.success(new Output(accessToken, account.getId(), account.getRole()));
    }
}
