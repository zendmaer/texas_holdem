package com.zendmaer.pokertest;

import com.zendmaer.pokertest.exceptions.PokerCardException;

import java.util.Objects;

/**
 * <b>Представление отдельно взятой карты</b>
 * <br>
 * Описание каждой карты состоит из двух символов:
 * <ul>
 *     <li>Номинал карты. Допустимые значения: 2, 3, 4, 5, 6, 7, 8, 9, T(en), J(ack), Q(ueen), K(ing), A(ce)</li>
 *     <li>Масть. Допустимые значения: S(pades), H(earts), D(iamonds), C(lubs)</li>
 * </ul>
 *
 * @version 1.0.1
 * @author Drachev A.S.
 *
 */
public final class Card implements Comparable<Card> {
    private static final String CARD_PATTERN = "^[2-9|T|J|Q|K|A][S|D|H|C]$";
    public static final int MAX_RANK_VALUE = 14;
    private final String cardValue;
    private final int value;
    private final char suit;

    public Card(String cardValue) {
        this.cardValue = Objects.requireNonNull(cardValue, "Значение карты не должно быть null.");

        if (!cardValue.matches(CARD_PATTERN))
            throw new PokerCardException("Некорректные данные игральной карты: [%s].".formatted(cardValue));

        value = translateRank(cardValue.charAt(0));
        suit = cardValue.charAt(1);
    }

    private static int translateRank(char value) {
        return switch (value) {
            case 'T' -> 10;
            case 'J' -> 11;
            case 'Q' -> 12;
            case 'K' -> 13;
            case 'A' -> 14;
            default -> Character.getNumericValue(value);
        };
    }

    public int getValue() {
        return value;
    }

    public char getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "Card{cardValue='%s', value=%d, suit=%s}".formatted(cardValue, value, suit);
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.value, o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return cardValue.equals(card.cardValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardValue);
    }
}
