package com.authorization.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MerchantCategoryCode {

    FOOD("Food", Arrays.asList("5411", "5412")),
    MEAL("Meal", Arrays.asList("5811", "5812")),
    DEFAULT("Cash", new ArrayList<>());

    private final String name;
    private final List<String> codes;

    public static MerchantCategoryCode verifyEstablishmentRating(String code, String merchant) {
        return Stream.of(values())
                .filter(merchantCategoryCode -> merchantCategoryCode.getCodes() != null &&
                        merchantCategoryCode.getCodes().stream()
                                .anyMatch(c -> c.equalsIgnoreCase(code)))
                .filter(merchantCategoryCode -> merchantCategoryCode.getName().equalsIgnoreCase(merchant))
                .findFirst()
                .orElse(DEFAULT);
    }

}
