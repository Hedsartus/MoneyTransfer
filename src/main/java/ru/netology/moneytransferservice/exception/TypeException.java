package ru.netology.moneytransferservice.exception;

public enum TypeException {
    INFO("INFO"),
    WRONGAUTH("AUTHENTICATION_ERROR"),
    ERROR_INPUT_DATA("ERROR_INPUT_DATA"),
    ERROR_OPERATION("ERROR_OPERATION");

    private String type;

    TypeException(String type) {
        this.type = type;
    }
}
