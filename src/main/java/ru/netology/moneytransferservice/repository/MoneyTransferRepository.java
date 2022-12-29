package ru.netology.moneytransferservice.repository;


import org.springframework.stereotype.Repository;
import ru.netology.moneytransferservice.common.ValidationTransfer;
import ru.netology.moneytransferservice.exception.InvalidTransferException;
import ru.netology.moneytransferservice.exception.TypeException;
import ru.netology.moneytransferservice.log.Log;
import ru.netology.moneytransferservice.model.Amount;
import ru.netology.moneytransferservice.model.Card;
import ru.netology.moneytransferservice.model.Operation;
import ru.netology.moneytransferservice.model.Transfer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MoneyTransferRepository {
    private final Map<String, Card> cardsMap = new ConcurrentHashMap<>();
    private final Map<Long, Operation> transfersMap = new ConcurrentHashMap<>();
    private final Log log = Log.getInstance();

    private AtomicLong transfersID;

    public MoneyTransferRepository() {
        this.transfersID = new AtomicLong(0);
        this.cardsMap.put("1234567891234567",
                new Card("1234567891234567", "12/25", "118",
                        new Amount(2700000, "RUR")));

        this.cardsMap.put("1111222233334444",
                new Card("1111222233334444", "04/23", "458",
                        new Amount(2000000, "RUR")));
    }

    /**
     * Остуществляем проверку данных и подготовку к переводу
     */
    public String transferMoneyPrepare(Transfer transfer) {
        // проверяем отправителя и получателя в бд
        Card sender = ValidationTransfer.validCardSender(transfer, this.cardsMap);
        Card reciver = ValidationTransfer.existenceCheck(transfer.getCardToNumber(), this.cardsMap);

        // проверяем соответствуют ли валюты
        ValidationTransfer.checkCurrencyCardsTransfer(sender, reciver);

        // проверяем достаточно ли средств для перевода
        ValidationTransfer.checkSum(sender, transfer.getAmount());

        // формируем индентификатор и добавляем в коллекцию
        var newIdTransfer = this.transfersID.incrementAndGet();
        this.transfersMap.put(newIdTransfer, new Operation(transfer));

        this.log.log(TypeException.INFO.name(), "Пройдена подготовка перевода №" + newIdTransfer);

        return "{\"operationId\":\"" + newIdTransfer + "\"}";
    }

    /**
     * Остуществляем проверку и перевод денег
     */
    public synchronized String operationTransfer(long idOperation) {
        // есть ли в бд такой трансфер и не завершен ли он
        if (this.transfersMap.containsKey(idOperation) && !this.transfersMap.get(idOperation).isCompleted()) {
            // снова проверяем отправителя и получателя
            Transfer transfer = this.transfersMap.get(idOperation).getTransfer();
            Card sender = ValidationTransfer.validCardSender(transfer, this.cardsMap);
            Card reciver = ValidationTransfer.existenceCheck(transfer.getCardToNumber(), this.cardsMap);

            // считаем комиссию
            BigDecimal sumWithComission = ValidationTransfer.getSumWithComission(transfer.getAmount());

            // проверяем достаточно ли средств для перевода
            ValidationTransfer.checkSum(sender, transfer.getAmount());

            // остуществляем перевод денег
            sender.getAmount().setValue(sender.getAmount().getValue().subtract(sumWithComission));
            reciver.getAmount().setValue(reciver.getAmount().getValue().add(transfer.getAmount().getValue()));

            // фиксируем в бд завершение операции
            this.transfersMap.get(idOperation).setCompleted(true);

            this.log.log(TypeException.INFO.name(), "Перевод №" + idOperation + " осуществлен, баланс: " +
                    sender.getAmount().getNormalMoney());

            return "{\"operationId\":\"" + idOperation + "\"}";
        } else {
            throw new InvalidTransferException(
                    TypeException.ERROR_INPUT_DATA,
                    "Невозможная операция, перевод №" + idOperation + " не существует!");
        }
    }

    /**
     * Удаление операции из хранилища
     */
    public void removeTransferOperation(long idOperation) {
        if (this.transfersMap.containsKey(idOperation)) {
            if (!this.transfersMap.get(idOperation).isCompleted()) {
                this.transfersMap.remove(idOperation);
                this.log.log(TypeException.INFO.name(), "Операция №" + idOperation + " удалена!");
            }
        }
    }


}
