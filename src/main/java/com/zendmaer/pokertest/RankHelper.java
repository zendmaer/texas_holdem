package com.zendmaer.pokertest;

import java.util.*;

/**
 * <b>Вспомогательный класс определения ранга карты</b>
 *
 * @version 1.0.1
 * @author Drachev A.S.
 */
public final class RankHelper {
    private final Map<Integer, Integer> collectedInfo = new HashMap<>();
    private boolean containsAce, haveSameSuit, orderedValue;
    private static final int FOUR_CARDS  = 4,
                             THREE_CARDS = 3,
                             TWO_CARDS   = 2;
    private static RankHelper rankHelper = new RankHelper();

    private RankHelper() { }

    private RankHelper resetParams() {
        containsAce  = false;
        haveSameSuit = true;
        orderedValue = true;
        collectedInfo.clear();
        return this;
    }

    /**
     * Т.к. это вспомогательный класс и сохранения состояния не нужно,
     * то сделаем примитивный Синглтон.
     * @return @{@link RankHelper}
     */
    public static RankHelper initialize() {
        return rankHelper.resetParams();
    }

    public void calculate(Card current, Card next) {
        int currentValue = current.getValue();

        collectedInfo.merge(currentValue, 1, Integer::sum);

        if (!containsAce && currentValue == Card.MAX_RANK_VALUE) containsAce = true;

        if (next == null) return;

        int  nextValue = next.getValue();
        char nextSuit  = next.getSuit();
        char currentSuit = current.getSuit();

        if (orderedValue && Math.abs(currentValue - nextValue) != 1) orderedValue = false;
        if (haveSameSuit && currentSuit != nextSuit) haveSameSuit = false;
    }

    private List<Integer> tempList;
    public CardRank getRank() {
        tempList = collectedInfo.values()
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        if (isRoyalFlash())
            return CardRank.ROYAL_FLUSH;
        else if (isStraightFlush())
            return CardRank.STRAIGHT_FLUSH;
        else if (isFourCard())
            return CardRank.FOUR_OF_A_KIND;
        else if (isFullHouse())
            return CardRank.FULL_HOUSE;
        else if (haveSameSuit)
            return CardRank.FLUSH;
        else if (orderedValue)
            return CardRank.STRAIGHT;
        else if (isThreeOfKind())
            return CardRank.THREE_OF_KIND;
        else if (isTwoPairs())
            return CardRank.TWO_PAIRS;
        else if (isPair())
            return CardRank.PAIR;

        return CardRank.HIGH_CARD;
    }

    private boolean isRoyalFlash() {
        return containsAce && haveSameSuit && orderedValue;
    }

    private boolean isStraightFlush() {
        return orderedValue && haveSameSuit;
    }

    private boolean isFourCard() {
        return tempList.get(0) == FOUR_CARDS;
    }
    private boolean isFullHouse() {
        return tempList.get(0) == THREE_CARDS && tempList.get(1) == TWO_CARDS;
    }

    private boolean isThreeOfKind() {
        return tempList.get(0) == THREE_CARDS;
    }

    private boolean isTwoPairs() {
        return tempList.get(0) == TWO_CARDS && tempList.get(1) == TWO_CARDS;
    }

    private boolean isPair() {
        return tempList.get(0) == TWO_CARDS;
    }
}
