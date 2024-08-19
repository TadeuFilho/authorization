package com.authorization.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantsUtil {

    public static final String APPROVED_TRANSACTION = "00";
    public static final String NOT_APPROVED_TRANSACTION = "07";
    public static final String NOT_APPROVED_TRANSACTION_BY_LACK_OF_MONEY = "51";
    public static final String ESTABLISHMENT_CLASSIFICATION_FOOD = "FOOD";
    public static final String ESTABLISHMENT_CLASSIFICATION_MEAL = "MEAL";
    public static final String ESTABLISHMENT_CLASSIFICATION_CASH = "CASH";

    public static List<String> ESTABLISHMENT_CLASSIFICATION_FOOD_CODES() {
        return Arrays.asList("5811", "5812");
    }

    public static List<String> ESTABLISHMENT_CLASSIFICATION_MEAL_CODES() {
        return Arrays.asList("5411", "5412");
    }

    public static List<String> ESTABLISHMENT_CLASSIFICATION_CASH_EMPTY_LIST_CODES() {
        return List.of();
    }

}
