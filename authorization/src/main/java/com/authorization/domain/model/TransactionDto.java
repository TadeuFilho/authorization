package com.authorization.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TransactionDto {

    private String code;
}
