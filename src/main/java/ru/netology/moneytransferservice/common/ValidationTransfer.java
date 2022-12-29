package ru.netology.moneytransferservice.common;

import ru.netology.moneytransferservice.exception.InvalidTransferException;
import ru.netology.moneytransferservice.exception.TypeException;
import ru.netology.moneytransferservice.model.Amount;
import ru.netology.moneytransferservice.model.Card;
import ru.netology.moneytransferservice.model.Transfer;

import java.math.BigDecimal;
import java.util.Map;

public class ValidationTransfer {
    public static final String COMISSION = "1.01";

    /**
     * проверка карты отправителя
     */
    public static Card validCardSender(Transfer transfer, Map<String, Card> cardsMap) {
        Card cardSender = existenceCheck(transfer.getCardFromNumber(), cardsMap);
        if (!cardSender.getCardCVV().equals(transfer.getCardFromCVV()) &&
                !cardSender.getCardValidTill().equals(transfer.getCardFromValidTill())) {

            throw new InvalidTransferException(
                    TypeException.ERROR_OPERATION,
                    "Данные карты №" + cardSender.getCardNumber() + " не соответствуют!");
        }

        return cardSender;
    }

    /**
     * Проверяем существует ли карта
     */
    public static Card existenceCheck(String numCard, Map<String, Card> cardsMap) {
        if (!cardsMap.containsKey(numCard)) {
            throw new InvalidTransferException(
                    TypeException.ERROR_OPERATION,
                    "Карта с номером " + numCard + " не существует!");
        } else {
            return cardsMap.get(numCard);
        }
    }

    /**
     * проверка на соответствие валюты
     */
    public static void checkCurrencyCardsTransfer(Card sender, Card reciver) {
        if (!sender.getAmount().getCurrency().equals(reciver.getAmount().getCurrency())) {
            throw new InvalidTransferException(TypeException.ERROR_OPERATION,
                    "Валютные счета карт (" +
                            sender.getCardNumber() + " " + sender.getAmount().getCurrency() +
                            " и (" + reciver.getCardNumber() + " " + reciver.getAmount().getCurrency() +
                            ") не совпадают!");
        }
    }

    /**
     * проверка достаточно ли средств
     */
    public static void checkSum(Card card, Amount amount) {
        BigDecimal sum = card.getAmount().getValue();

        int result = sum.compareTo(getSumWithComission(amount));

        if (result < 0) {
            throw new InvalidTransferException(TypeException.ERROR_OPERATION,
                    "Недостаточно средств для перевода " +
                            amount.getNormalMoney() + " c карты " + card.getCardNumber() + "!");
        }
    }

    /**
     * считаем комиcсию
     */
    public static BigDecimal getSumWithComission(Amount amount) {
        return amount.getValue().multiply(new BigDecimal(COMISSION));
    }
}
