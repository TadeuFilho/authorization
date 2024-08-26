package com.authorization.domain.service;

import com.authorization.domain.model.MerchantCategoryCode;
import com.authorization.domain.model.entity.Account;
import com.authorization.domain.model.entity.Transaction;
import com.authorization.domain.model.mapper.AccountMapper;
import com.authorization.domain.model.mapper.TransactionMapper;
import com.authorization.domain.model.request.TransactionRequest;
import com.authorization.domain.model.response.TransactionResponse;
import com.authorization.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.authorization.core.util.ConstantsUtil.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionResponse authorize(TransactionRequest transactionRequest) {

        log.info("::: Inicio do processo de autorização da transação :::");
        Account account = accountService.findById(Long.parseLong(transactionRequest.getAccountId()));
        TransactionResponse transactionResponse = verifyTransactionIsApproved(transactionRequest, account);

        MerchantCategoryCode merchantCategoryCode =
                MerchantCategoryCode.verifyEstablishmentRating(transactionRequest.getMcc(),
                        transactionRequest.getMerchant());

        analiseTransaction(transactionResponse.getCode(), transactionRequest, merchantCategoryCode.getName(), account);

        return transactionResponse;

    }

    private void analiseTransaction(String mccCode, TransactionRequest transactionRequest, String mccName, Account account) {
        Transaction transaction;
        Account accountToUpdate = switch (mccCode.toUpperCase()) {
            case APPROVED_TRANSACTION -> {
                transaction = TransactionMapper.toApprovedTransactionEntity(transactionRequest, mccName);
                yield AccountMapper.toUpdateAfterApprovedTransaction(account, account.getBalance().subtract(transactionRequest.getAmount()));
            }
            case NOT_APPROVED_TRANSACTION_BY_LACK_OF_MONEY -> {
                transaction = TransactionMapper.toNotApprovedByBalanceTransactionEntity(transactionRequest, mccName);
                yield AccountMapper.toUpdateAfterNotApprovedTransaction(account);
            }
            default -> {
                transaction = TransactionMapper.toNotApprovedTransactionEntity(transactionRequest, mccName);
                yield AccountMapper.toUpdateAfterNotApprovedTransaction(account);
            }
        };

        log.info("::: Inicio do processo de salvamento de transação e atualização de conta :::");
        transactionRepository.save(transaction);
        accountService.update(accountToUpdate);
        log.info("::: Banco atualizado :::");
    }

    private static TransactionResponse verifyTransactionIsApproved(TransactionRequest transactionRequest, Account account) {
        return switch (account.getBalance().compareTo(transactionRequest.getAmount())) {
            case 1, 0 -> {
                log.info("::: Transação aprovada -> {} :::", transactionRequest);
                yield TransactionResponse.builder()
                        .code(APPROVED_TRANSACTION)
                        .build();
            }
            case -1 -> {
                log.info("::: Transação não aprovada por saldo inferior na conta -> {} :::", transactionRequest);
                yield TransactionResponse.builder()
                        .code(NOT_APPROVED_TRANSACTION_BY_LACK_OF_MONEY)
                        .build();
            }
            default -> {
                log.info("::: Transação não aprovada -> {} :::", transactionRequest);
                yield TransactionResponse.builder()
                        .code(NOT_APPROVED_TRANSACTION)
                        .build();
            }
        };
    }
}

