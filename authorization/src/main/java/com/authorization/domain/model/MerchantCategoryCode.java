package com.authorization.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

import static com.authorization.core.util.ConstantsUtil.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MerchantCategoryCode {

    FOOD(ESTABLISHMENT_CLASSIFICATION_FOOD, ESTABLISHMENT_CLASSIFICATION_FOOD_CODES()),
    MEAL(ESTABLISHMENT_CLASSIFICATION_MEAL, ESTABLISHMENT_CLASSIFICATION_MEAL_CODES()),
    DEFAULT(ESTABLISHMENT_CLASSIFICATION_CASH, ESTABLISHMENT_CLASSIFICATION_CASH_EMPTY_LIST_CODES());

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
