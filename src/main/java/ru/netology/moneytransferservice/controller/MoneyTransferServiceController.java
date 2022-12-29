package ru.netology.moneytransferservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netology.moneytransferservice.exception.InvalidTransferException;
import ru.netology.moneytransferservice.exception.TypeException;
import ru.netology.moneytransferservice.model.Confirm;
import ru.netology.moneytransferservice.model.Transfer;
import ru.netology.moneytransferservice.service.MoneyTransferService;

@RestController
public class MoneyTransferServiceController {
    private final MoneyTransferService moneyTransferService;

    public MoneyTransferServiceController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }
    @CrossOrigin(origins = "https://serp-ya.github.io")
    @PostMapping("/transfer")
    public String transferMoney(@RequestBody @Validated Transfer transfer) {
        return this.moneyTransferService.transferMoneyPrepare(transfer);
    }

    @CrossOrigin(origins = "https://serp-ya.github.io")
    @PostMapping("/confirmOperation")
    public String confirm(@RequestBody @Validated Confirm confirm) {
        return this.moneyTransferService.transferMoney(confirm);
    }

    @ExceptionHandler(InvalidTransferException.class)
    public ResponseEntity<String> invalidCredentialsHandler(InvalidTransferException e) {
        if (e.getTypeException() == TypeException.ERROR_OPERATION) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
