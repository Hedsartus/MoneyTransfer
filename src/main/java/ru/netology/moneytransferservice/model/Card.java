package ru.netology.moneytransferservice.model;

import java.util.Objects;

public class Card {
    private String cardNumber;
    private String cardValidTill;
    private String cardCVV;
    private Amount amount;

    public Card(String cardNumber, String cardValidTill, String cardCVV, Amount amount) {
        this.cardNumber = cardNumber;
        this.cardValidTill = cardValidTill;
        this.cardCVV = cardCVV;
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardValidTill() {
        return cardValidTill;
    }

    public void setCardValidTill(String cardValidTill) {
        this.cardValidTill = cardValidTill;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardNumber.equals(card.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber);
    }
}
