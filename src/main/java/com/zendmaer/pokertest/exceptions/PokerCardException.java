package com.zendmaer.pokertest.exceptions;

/**
 * <b>Собственный тип ошибки для текущей программы</b>
 *
 * @version 1.0.0
 * @author Drachev A.S.
 *
 */
public class PokerCardException extends RuntimeException {

    public PokerCardException(String message) {
        super(message);
    }
}
