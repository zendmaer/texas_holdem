package com.zendmaer.pokertest;

import com.zendmaer.pokertest.exceptions.PokerCardException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * Проверка функционала игральных карт и наборов.
 * @version 1.0.1
 * @author Drachev A.S.
 */
public class PokerTest implements TestHelper {

    @Test
    void royalFlash() {

        testSets("Тестирование на принадлежность к royal flash", CardRank.ROYAL_FLUSH,
                "tc ac kc jc qc",
                "ac kc jc qc tc",
                "kc jc qc tc ac",
                "jc qc tc ac kc",
                "qc tc ac kc jc",
                "tc ac kc jc qc tc",
                "td ad kd jd qd td",
                "jh qh th ah kh",
                "qs ts as ks js",
                "tC ac kc jC qc tc",
                "Jh qh TH ah kh");
    }
    @Test
    void straightFlush() {
        testSets("Тестирование на принадлежность к straight flash", CardRank.STRAIGHT_FLUSH,
                "9c tc jc qc kc",
                "2c 3c 4c 5c 6c",
                "5h 3h 4h 6h 7H");
    }

    @Test
    void fourOfKind() {
        testSets("Тестирование на принадлежность к four of kind", CardRank.FOUR_OF_A_KIND,
                "9c 9h 9s 9d kc",
                "ac ah as ad 2d",
                "7s 7h 7c ad 7d");
    }

    @Test
    void fullHouse() {

        testSets("Тестирование на принадлежность к full house", CardRank.FULL_HOUSE,
                "9c 9h 9s 5h 5d",
                "as ah 5s 5d ad",
                "as 3h 3s 3d ad");
    }

    @Test
    void flush() {
        testSets("Тестирование на принадлежность к flush", CardRank.FLUSH,
                "9c tc 8c 5c 2c",
                "2d kd jd 3d 4d",
                "tC 6c jc qc kc");
    }

    @Test
    void straight() {
        testSets("Тестирование на принадлежность к straight", CardRank.STRAIGHT,
                "9c td jh qh ks",
                "jc td kh qh 9s",
                "3c 7d 6h 4h 5s");
    }

    @Test
    void threeOfKind() {
        testSets("Тестирование на принадлежность к three of kind", CardRank.THREE_OF_KIND,
                "9c 9d 9h qh ks",
                "9c 7s 9h qh 9s",
                "ts 9d tc 2h th");
    }

    @Test
    void twoPairs() {
        testSets("Тестирование на принадлежность к two pairs", CardRank.TWO_PAIRS,
                "9c 9d 7h 7s ks",
                "tc jh 7s js td",
                "3c 8d 4c 8s 3D");
    }

    @Test
    void pair() {
        testSets("Тестирование на принадлежность к pair", CardRank.PAIR,
                "9c 3d 7h 8s 9s",
                "5c qd 7h qs 9s",
                "kc jd kh qs 9s");
    }

    @Test
    void highCard() {

        testSets("Тестирование на принадлежность к high card", CardRank.HIGH_CARD,
                "2c ad 7h 4s 9s",
                "kd ad 7h 4s ts",
                "ad Td 2s 4h 8s");
    }

    @Test
    void incorrectCardSetLessValues() {
        Exception exception = Assertions.assertThrows(PokerCardException.class, () -> new PokerHand("2c ad 7h 4s"));
        Assertions.assertEquals("Проверьте набор. Должно быть ровно 5 уникальных карт. [2c ad 7h 4s]", exception.getMessage());
    }

    @Test
    void incorrectCardSetMoreValues() {
        Exception exception = Assertions.assertThrows(PokerCardException.class, () -> new PokerHand("2c ad 7h 4s 3s 2d"));
        Assertions.assertEquals("Проверьте набор. Должно быть ровно 5 уникальных карт. [2c ad 7h 4s 3s 2d]", exception.getMessage());
    }

    @Test
    void setNullArgument() {
        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> new PokerHand(null));
        Assertions.assertEquals("Значение набора не должно быть null.", exception.getMessage());
    }

    @Test
    void setEmptyArgument() {
        Exception exception = Assertions.assertThrows(PokerCardException.class, () -> new PokerHand(""));
        Assertions.assertEquals("Некорректные данные игральной карты: [].", exception.getMessage());
    }

    @Test
    void setIncorrectCardValue() {
        Exception exception = Assertions.assertThrows(PokerCardException.class, () -> new PokerHand("1c ad 7h 4s 3s"));
        Assertions.assertEquals("Некорректные данные игральной карты: [1C].", exception.getMessage());
    }

    @Test
    void setIncorrectCardSuit() {
        Exception exception = Assertions.assertThrows(PokerCardException.class, () -> new PokerHand("2z ad 7h 4s 3s"));
        Assertions.assertEquals("Некорректные данные игральной карты: [2Z].", exception.getMessage());
    }
    @Test
    void royalFlashVSFourOfAKind() {
        testOrderSet(List.of("9c 9h 9s 9d kc", "tc ac kc jc qc"), List.of("tc ac kc jc qc (ROYAL_FLUSH)", "9c 9h 9s 9d kc (FOUR_OF_A_KIND)"));
    }

    @Test
    void orderOfSets() {
        testOrderSet(List.of(
                "kd ad 7h 4s ts", //high
                "9c 7s 9h qh 9s", //three of kind
                "9c 3d 7h 8s 9s", //pair
                "ac ah as ad 2d", //four of card
                "tc jh 7s js td", //two pair
                "jh qh th ah kh", //royal flash
                "9c tc jc qc kc", //straight flash
                "jc td kh qh 9s", //straight
                "tC 6c jc qc kc", //flush
                "as 3h 3s 3d ad" //full house
        ),
                List.of("jh qh th ah kh (ROYAL_FLUSH)",
                        "9c tc jc qc kc (STRAIGHT_FLUSH)",
                        "ac ah as ad 2d (FOUR_OF_A_KIND)",
                        "as 3h 3s 3d ad (FULL_HOUSE)",
                        "tC 6c jc qc kc (FLUSH)",
                        "jc td kh qh 9s (STRAIGHT)",
                        "9c 7s 9h qh 9s (THREE_OF_KIND)",
                        "tc jh 7s js td (TWO_PAIRS)",
                        "9c 3d 7h 8s 9s (PAIR)",
                        "kd ad 7h 4s ts (HIGH_CARD)"));
    }

    @Test
    void orderSetsOfHigh() {
        testOrderSet(List.of("kd ad 7h 4s ts",  //high k + 7 + a + 4 + t -> k
                             "ad Td 2s 4h 8s"), //high 8 + 2 + a + 4 + t -> 8

                List.of("kd ad 7h 4s ts (HIGH_CARD)",
                        "ad Td 2s 4h 8s (HIGH_CARD)"));
    }

    @Test
    void orderSetsOfFlush() {
        testOrderSet(List.of("9c tc 8c 5c 2c",  //high 2 + 5 + 8 + 9 + t = t
                             "2d kd jd 3d 4d"), //high 2 + 3 + 4 + j + k = k (старшая карта)
                List.of("2d kd jd 3d 4d (FLUSH)",
                        "9c tc 8c 5c 2c (FLUSH)"));
    }

    @Test
    void orderSetTwoPairs() {

        testOrderSet(List.of("as 2c 2d 7h 7c",
                             "kd 3c 3h 7s 7c"),

                List.of("kd 3c 3h 7s 7c (TWO_PAIRS)",
                        "as 2c 2d 7h 7c (TWO_PAIRS)"));
    }
    @Test
    void orderSetOnePairs() {

        testOrderSet(List.of("5c 4d 6h 4s 9s",
                             "5c 3d 7h 3s 9s"),

                List.of("5c 4d 6h 4s 9s (PAIR)",
                        "5c 3d 7h 3s 9s (PAIR)"));
    }

    @Test
    void orderSetFour() {

        testOrderSet(List.of("9c 9h 9s 9d kc",
                        "9c 9h 9s 9d 6c"),

                List.of("9c 9h 9s 9d kc (FOUR_OF_A_KIND)",
                        "9c 9h 9s 9d 6c (FOUR_OF_A_KIND)"));
    }

    @Test
    void orderSetThree() {

        testOrderSet(List.of("7c 7d 7h qh ks",
                             "9c 9d 9h qh 6s"),

                List.of("7c 7d 7h qh ks (THREE_OF_KIND)",
                        "9c 9d 9h qh 6s (THREE_OF_KIND)"));
    }
}
