package com.authorization.domain.model.request;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ToString
@Getter
public class TransactionRequest {

    @NotNull
    private String accountId;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String merchant;

    @NotBlank
    private String mcc;
}
