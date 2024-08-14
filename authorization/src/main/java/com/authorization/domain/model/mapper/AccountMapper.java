package com.authorization.domain.model.mapper;

import com.authorization.domain.model.entity.Account;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMapper {

    public static Account toUpdateAfterApprovedTransaction(Account account, BigDecimal newBalanceAfterTransaction) {
        account.setUpdatedAt(LocalDateTime.now());
        account.setBalance(newBalanceAfterTransaction);
        return account;
    }

    public static Account toUpdateAfterNotApprovedTransaction(Account account) {
        account.setUpdatedAt(LocalDateTime.now());
        return account;
    }
}
