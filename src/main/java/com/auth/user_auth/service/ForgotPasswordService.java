package com.auth.user_auth.service;

import com.auth.share.entity.Result;
import com.auth.user_auth.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ForgotPasswordService {
    private final AccountRepository accountRepository;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Input {
        private String username;
        private String email;
    }

    public Result<?> forgotPassword(Input input) {
        return Result.success("");
    }
}
