package com.wsoft.minipay.transaction;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transaction")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transaction> list(){
        return service.list();
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction){
        return service.create(transaction);
    }
}
