package com.wsoft.minipay.transaction;

public class InvalidTransactionException extends RuntimeException{

    public InvalidTransactionException(String message) {
        super(message);
    }
}
