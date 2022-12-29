package ru.netology.moneytransferservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.moneytransferservice.exception.InvalidTransferException;
import ru.netology.moneytransferservice.model.Amount;
import ru.netology.moneytransferservice.model.Confirm;
import ru.netology.moneytransferservice.model.Transfer;
import ru.netology.moneytransferservice.repository.MoneyTransferRepository;

class MoneyTransferServiceTest {

    @Test
    void testTransferMoneyPrepare() {
        MoneyTransferRepository repository = Mockito.mock(MoneyTransferRepository.class);

        var service = new MoneyTransferService(repository);

        Transfer transfer = new Transfer(
                "1234567891234567",
                "12/25",
                "118",
                "1234567891234567",
                new Amount(20000, "RUR")
        );

        InvalidTransferException thrown = Assertions.assertThrows(InvalidTransferException.class, () -> {
            service.transferMoneyPrepare(transfer);
        });

        Assertions.assertEquals("{\"message\":\"Карта отправления и карта назначения совпадают!\"}",
                thrown.getMessage());
    }

    @Test
    void testTransferMoney() {
        MoneyTransferRepository repository = Mockito.mock(MoneyTransferRepository.class);

        var service = new MoneyTransferService(repository);

        Confirm confirm = new Confirm("1", "2222");

        InvalidTransferException thrown = Assertions.assertThrows(InvalidTransferException.class, () -> {
            service.transferMoney(confirm);
        });

        Assertions.assertEquals("{\"message\":\"Ошибка кода подтверждения!\"}", thrown.getMessage());


    }
}