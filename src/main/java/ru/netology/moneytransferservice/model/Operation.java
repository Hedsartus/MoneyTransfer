package ru.netology.moneytransferservice.model;

public class Operation {
    private Transfer transfer;
    private boolean isCompleted;

    public Operation(Transfer transfer) {
        this.transfer = transfer;
        this.isCompleted = false;
    }

    public Transfer getTransfer() {
        return this.transfer;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
