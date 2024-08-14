package com.authorization.domain.service;

import com.authorization.domain.model.entity.Account;
import com.authorization.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account findById(Long accountId) {
        log.info("::: Buscando a conta por id -> {} :::", accountId);
        return accountRepository
                .findById(accountId)
                .orElseThrow(RuntimeException::new);
    }

    public void update(Account account) {
        log.info("::: Atualizando conta  -> {} :::", account);
        accountRepository.save(account);
    }
}
