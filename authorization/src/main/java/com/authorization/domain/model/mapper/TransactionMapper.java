package com.authorization.domain.model.mapper;

import com.authorization.domain.model.entity.Transaction;
import com.authorization.domain.model.request.TransactionRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionMapper {

    public static Transaction toApprovedTransactionEntity(TransactionRequest transactionRequest, String mccName) {
        return Transaction
                .builder()
                .merchant(mccName)
                .accountId(transactionRequest.getAccountId())
                .amount(transactionRequest.getAmount())
                .isApproved(true)
                .reason("Transação aprovada")
                .build();
    }

    public static Transaction toNotApprovedByBalanceTransactionEntity(TransactionRequest transactionRequest, String mccName) {
        return Transaction
                .builder()
                .accountId(transactionRequest.getAccountId())
                .merchant(mccName)
                .amount(transactionRequest.getAmount())
                .isApproved(false)
                .reason("Transação não aprovada por falta de saldo")
                .build();
    }

    public static Transaction toNotApprovedTransactionEntity(TransactionRequest transactionRequest, String mccName) {
        return Transaction
                .builder()
                .merchant(mccName)
                .amount(transactionRequest.getAmount())
                .accountId(transactionRequest.getAccountId())
                .isApproved(false)
                .reason("Transação não aprovada")
                .build();
    }
    }
