package ru.netology.moneytransferservice.log;

public class Printer implements PrintLog {
    @Override
    public void logShow(String message) {
        System.out.print(message);
    }
}
