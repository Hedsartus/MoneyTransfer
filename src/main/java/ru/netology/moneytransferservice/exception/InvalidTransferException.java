package ru.netology.moneytransferservice.exception;

import ru.netology.moneytransferservice.log.Log;

public class InvalidTransferException extends RuntimeException {
    private Log log = Log.getInstance();
    private TypeException typeException;

    public InvalidTransferException(TypeException typeException) {
        super("Попробуйте осуществить перевод еще раз!");
        setLog(typeException, "Попробуйте осуществить перевод еще раз!");
    }

    public InvalidTransferException(TypeException typeException, String message) {
        super("{\"message\":\"" + message + "\"}");
        setLog(typeException, message);
    }

    private void setLog(TypeException typeException, String message) {
        this.typeException = typeException;
        log.log(this.typeException.name(), message);
    }

    public TypeException getTypeException() {
        return typeException;
    }

}
