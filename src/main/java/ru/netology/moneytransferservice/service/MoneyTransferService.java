package ru.netology.moneytransferservice.service;

import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.exception.InvalidTransferException;
import ru.netology.moneytransferservice.exception.TypeException;
import ru.netology.moneytransferservice.model.Confirm;
import ru.netology.moneytransferservice.model.Transfer;
import ru.netology.moneytransferservice.repository.MoneyTransferRepository;

@Service
public class MoneyTransferService {
    private final MoneyTransferRepository moneyTransferRepository;

    public MoneyTransferService(MoneyTransferRepository moneyTransferRepository) {
        this.moneyTransferRepository = moneyTransferRepository;
    }

    public String transferMoneyPrepare(Transfer transfer) {
        if (transfer.getCardFromNumber().equals(transfer.getCardToNumber())) {
            throw new InvalidTransferException(TypeException.ERROR_OPERATION,
                    "Карта отправления и карта назначения совпадают!");
        }
        return this.moneyTransferRepository.transferMoneyPrepare(transfer);
    }

    public String transferMoney(Confirm confirm) {
        long operationId = Long.parseLong(confirm.getOperationId());
        if (operationId > 0) {
            if (confirm.getCode().equals("0000")) {
                return this.moneyTransferRepository.operationTransfer(operationId);
            } else {
                this.moneyTransferRepository.removeTransferOperation(operationId);
                throw new InvalidTransferException(TypeException.ERROR_INPUT_DATA,
                        "Ошибка кода подтверждения!");
            }
        } else {
            throw new InvalidTransferException(TypeException.ERROR_INPUT_DATA,
                    "Непредвиденная ошибка!");
        }
    }
}
