package com.wsoft.minipay.wallet;

import com.wsoft.minipay.transaction.TransactionRepository;
import com.wsoft.minipay.transaction.TransactionService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

public interface WalletRepository extends CrudRepository<Wallet, Long> {


}
