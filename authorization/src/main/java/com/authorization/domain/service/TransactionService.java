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
            case "00" -> {
                transaction = TransactionMapper.toApprovedTransactionEntity(transactionRequest, mccName);
                yield AccountMapper.toUpdateAfterApprovedTransaction(account, account.getBalance().subtract(transactionRequest.getAmount()));
            }
            case "51" -> {
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
        if (account.getBalance().compareTo(transactionRequest.getAmount()) >= 0) {
            log.info("::: Transação aprovada -> {} :::", transactionRequest);
            return TransactionResponse
                    .builder()
                    .code("00")
                    .build();

        } else if (account.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
            log.info("::: Transação não aprovada por saldo inferior na conta -> {} :::", transactionRequest);
            return TransactionResponse
                    .builder()
                    .code("51")
                    .build();
        } else {
            log.info("::: Transação não aprovada -> {} :::", transactionRequest);
            return TransactionResponse
                    .builder()
                    .code("07")
                    .build();
        }

    }

}

