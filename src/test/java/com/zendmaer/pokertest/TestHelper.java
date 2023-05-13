package com.zendmaer.pokertest;

import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <b>Вспомогательный интерфейс для тестирования</b>
 * @version 1.0.1
 * @author Drachev A.S.
 */
public interface TestHelper {
    /**
     * Вспомогательный метод для проверки принадлежности набора к рангу.
     * @param description описание операции массового сравнивания
     * @param expectedRank ожидаемый Ранг
     * @param values наборы карт
     */
    default void testSets(String description, CardRank expectedRank, String... values) {
        Assertions.assertAll(description,
                Arrays.stream(values).map(PokerHand::new).map(currentPokerHand ->
                        () -> Assertions.assertEquals(currentPokerHand.getCardRank(), expectedRank))
        );
    }

    /**
     * Вспомогательный метод для проверки порядка следования наборов карт.
     * @param input входные наборы
     * @param expected ожидаемые наборы
     */
    default void testOrderSet(List<String> input, List<String> expected) {
        List<String> result = input.stream()
                .map(PokerHand::new)
                /* От большего к меньшему по ТЗ */
                .sorted(Collections.reverseOrder())
                .map(PokerHand::toString).toList();

        Assertions.assertEquals(result, expected);
    }
}
