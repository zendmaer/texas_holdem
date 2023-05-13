package com.zendmaer.pokertest;

import java.util.List;
import java.util.Objects;

/**
 * <b>Класс ранжирования покерных карт по правилам Техасского холдема</b>
 * <br>
 * Примечание по ТЗ: Туз в комбинациях стрит или стрит-флэш может быть только наивысшей картой.
 *
 * @version 1.0.0
 * @author Drachev A.S.
 */
public final class PokerHand implements PokerHandImpl<PokerHand> {
    private final List<Card> cardCollection;
    private final String cards;
    private final CardRank cardRank;

    public PokerHand(String cards) {
        this.cards          = Objects.requireNonNull(cards, "Значение набора не должно быть null.").trim();
        this.cardCollection = PokerHandImpl.convertToCardSet(cards);
        this.cardRank       = calculateCardRank(cardCollection, RankHelper.initialize());
    }

    @Override
    public CardRank getCardRank() {
        return cardRank;
    }

    @Override
    public String getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return cards + " (" + cardRank + ")";
    }

    @Override
    public int compareTo(PokerHand o) {
        int tempValue =  Integer.compare(cardRank.getRankNumber(), o.cardRank.getRankNumber());
        if (tempValue != 0) return tempValue;

        for (int i = 0; i < cardCollection.size(); i++) {
            if ( ( tempValue = Integer.compare(cardCollection.get(i).getValue(),
                    o.cardCollection.get(i).getValue()) ) != 0) return tempValue;
        }
        return 0;
    }
}
