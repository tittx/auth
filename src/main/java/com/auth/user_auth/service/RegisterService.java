package com.auth.user_auth.service;

import com.auth.share.entity.Result;
import com.auth.user_auth.model.Account;
import com.auth.user_auth.model.Role;
import com.auth.user_auth.repository.AccountRepository;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterService {
    private final BCryptPasswordService bCryptPasswordService;
    private final AccountRepository accountRepository;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Input {
        private String username;
        private String email;
        private String password;
        private Role role;
    }

    public Result<?> register(Input input) {
        Account account = accountRepository.findByUsername(input.username);
        if (account != null) {
            return Result.failed("This username was already exist");
        }
        if (!validatePassword(input.password)) {
            return Result.failed("Invalid password");
        }
        String encryptedPassword = this.bCryptPasswordService.encodePassword(input.getPassword());
        Account newAccount = new Account(input.username, encryptedPassword, input.role);
        this.accountRepository.save(newAccount);

        //TODO: create user profile for this account if needed
        // Account getNewAccount = accountRepository.findByUsername(input.username);
        // UserProfile userProfile = UserProfile.create(getNewAccount.getId(), input.email);
        // this.userProfileRepository.save(userProfile);
        return Result.success("Successfuly Register");
    }

    private boolean validatePassword(String password) {
        //TODO: extends this if needed
        return password.length() >= 8;
    }
}