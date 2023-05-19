package com.zendmaer.pokertest;

import com.zendmaer.pokertest.exceptions.PokerCardException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <b>Интерфейс, реализующий часть логики и представляющий каркас для класса-реализатора для ранжирования карт Техасского холдема</b>
 * @param <T> тип класса, реализующего данный интерфейс
 *
 * @version 1.0.0
 * @author Drachev A.S.
 */
interface PokerHandBasic<T extends PokerHandBasic<T>> extends Comparable<T> {

    int MAX_CARDS_SET = 5;

    String getCards();
    CardRank getCardRank();

    static List<Card> convertToCardList(String textCard) {
        return Stream.of(textCard.split("\\s+"))
                .map(String::toUpperCase)
                .distinct()
                .map(Card::new)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    default CardRank calculateCardRank(List<Card> cardCollection, RankHelper rankHelper) {

        if (cardCollection.size() != MAX_CARDS_SET)
            throw new PokerCardException("Проверьте набор. Должно быть ровно %d уникальных карт. [%s]".formatted(MAX_CARDS_SET, getCards()));

        for (int i=0, j=1; i < cardCollection.size(); j=++i+1) {
            rankHelper.calculate(cardCollection.get(i), j == cardCollection.size() ? null : cardCollection.get(j));
        }

        return rankHelper.getRank();
    }

}
