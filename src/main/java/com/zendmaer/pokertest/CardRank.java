package com.zendmaer.pokertest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>Порядок набора карт (от меньшего к большему)</b>
 *
 * @author Drachev A.S.
 * @version 1.0.2
 */
public enum CardRank {

    /**
     * Старшая карта, шанс выпадения ~ 50.12%.
     * Выигрыш определяется по большей карте в наборе.
     */
    HIGH_CARD(1) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            for (int i = 0, tempValue = 0; i < one.size(); i++) {
                if ((tempValue = one.get(i).getValue() - two.get(i).getValue()) != 0) return tempValue;
            }
            return 0;
        }
    },

    /**
     * Пара, шанс выпадения ~ 42.26%.
     * Набор содержит одну пару карт с одним значением.
     */
    PAIR(2) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            return commonCompareForOneOrTwoPair(one, two, 1);
        }
    },

    /**
     * Две пары, шанс выпадения ~ 4.75%.
     * Набор содержит две пары карт, содержащих общие значения.
     */
    TWO_PAIRS(3) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            return commonCompareForOneOrTwoPair(one, two, 2);
        }
    },

    /**
     * Тройка, шанс выпадения ~ 2.1%.
     * Набор содержит три одинаковые карты.
     */
    THREE_OF_KIND(4) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            return commonCompareForThreeAndFourSameCards(one, two, 2, (v1, v2, list1, list2) -> HIGH_CARD.compareSameRank(one, two));
        }
    },

    /**
     * Стрит, шанс выпадения ~ 0.39%.
     * Последовательный набор пяти последовательных карт различной масти.
     */
    STRAIGHT(5) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            return HIGH_CARD.compareSameRank(one, two);
        }
    },

    /**
     * Флеш, шанс выпадения ~ 0.2%.
     * Все карты одной масти, порядок не имеет значения.
     */
    FLUSH(6) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            return HIGH_CARD.compareSameRank(one, two);
        }
    },

    /**
     * Фул-хаус, шанс выпадения ~ 0.14%.
     * В данном наборе расположены 3 карты с одним значением + 2 карты с одним значением.
     */
    FULL_HOUSE(7) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            return FOUR_OF_A_KIND.compareSameRank(one, two);
        }
    },

    /**
     * Каре, шанс выпадения ~ 0.024%.
     * В данном наборе расположен сет 4 карт с одним значением + 1 одна любая карта.
     */
    FOUR_OF_A_KIND(8) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            return commonCompareForThreeAndFourSameCards(one, two, 3, (v1, v2, list1, list2) -> Integer.compare(v1, v2));
        }
    },

    /**
     * Стрит-флеш, шанс выпадения ~ 0.0015%.
     * В данном наборе расположены 5 последовательных карт одной масти, похож на королевскую масть, но не содержит туз.
     */
    STRAIGHT_FLUSH(9) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            return HIGH_CARD.compareSameRank(one, two);
        }
    },

    /**
     * Королевская масть, шанс выпадения ~ 0.0002%.
     * Является наивысшей комбинацией.
     * В данном наборе расположены 5 карт одной масти от 10 до туза.
     */
    ROYAL_FLUSH(10) {
        public int compareSameRank(List<Card> one, List<Card> two) {
            return 0;
        }
    };

    private final int rankNumber;

    public int getRankNumber() {
        return rankNumber;
    }

    CardRank(int rankNumber) {
        this.rankNumber = rankNumber;
    }

    @Override
    public String toString() {
        return name();
    }

    abstract public int compareSameRank(List<Card> one, List<Card> two);

    protected Map<Integer, Long> convert(List<Card> cards) {
        return cards.stream().collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));
    }

    protected List<Map.Entry<Integer, Long>> sortByValueAndKey(Set<Map.Entry<Integer, Long>> cards) {
        return cards.stream().sorted(Map.Entry.<Integer, Long>comparingByValue(Comparator.reverseOrder()).thenComparing(Map.Entry::getKey)).collect(Collectors.toList());
    }

    protected int commonCompareForThreeAndFourSameCards(List<Card> one, List<Card> two, int index, ComparingFunction<Card> function) {
        int oF = one.get(0).getValue();
        int tF = two.get(0).getValue();

        int lastIndex = one.size() - 1;
        int oL = one.get(lastIndex).getValue();
        int tL = two.get(lastIndex).getValue();

        if (oF == one.get(index).getValue()) {

            if (tF == two.get(index).getValue()) {
                if (oF > tF) return 1;
                else if (oF < tF) return -1;
                return function.someData(oL, tL, one.subList(index + 1, lastIndex), two.subList(index + 1, lastIndex));
            } else {
                if (oF > tL) return 1;
                else if (oF < tL) return -1;
                return function.someData(oL, tF, one.subList(index + 1, lastIndex), two.subList(0, index));
            }
        } else {

            if (tF == two.get(index).getValue()) {
                if (oL > tF) return 1;
                else if (oL < tF) return -1;
                return function.someData(oF, tL, one.subList(0, index), two.subList(index + 1, lastIndex));
            } else {
                if (oL > tL) return 1;
                else if (oL < tL) return -1;
                return function.someData(oF, tF, one.subList(0, index), two.subList(0, index));
            }
        }
    }

    protected int commonCompareForOneOrTwoPair(List<Card> one, List<Card> two, int pair) {
        List<Map.Entry<Integer, Long>> data1 = sortByValueAndKey( convert(one).entrySet() );
        List<Map.Entry<Integer, Long>> data2 = sortByValueAndKey( convert(two).entrySet() );

        int tempValue = 0;

        for (int i = pair - 1; i >= 0; i--) {
            if ( (tempValue = Integer.compare(data1.get(i).getKey(), data2.get(i).getKey())) != 0) return tempValue;
        }

        for (int i = data1.size() - 1; i >= pair; i--) {
            if ( (tempValue = Integer.compare(data1.get(i).getKey(), data2.get(i).getKey())) != 0) return tempValue;
        }
        return tempValue;
    }

    @FunctionalInterface
    protected interface ComparingFunction<T> {
        int someData(int v1, int v2, List<T> list1, List<T> list2);
    }

}
