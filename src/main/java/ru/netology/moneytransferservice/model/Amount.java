package ru.netology.moneytransferservice.model;

import java.math.BigDecimal;

public class Amount {
    private BigDecimal value;
    private String currency;

    public Amount() {
    }

    public Amount(int value, String currency) {
        this.value = new BigDecimal(value);
        this.currency = currency;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public String getNormalMoney() {
        return this.getValue().divide(new BigDecimal(100)) + " " + getCurrency();
    }

    @Override
    public String toString() {
        return this.getValue() + " " + getCurrency();
    }
}
