package com.authorization.api.controller;

import com.authorization.domain.model.request.TransactionRequest;
import com.authorization.domain.model.response.TransactionResponse;
import com.authorization.domain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/transaction", produces = "application/json")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> authorize(@RequestBody TransactionRequest transactionRequest) {
        log.info("::: Inicio do processamento de transação. Requisição -> {} :::", transactionRequest);
        return ResponseEntity.ok(transactionService.authorize(transactionRequest));
    }

}
