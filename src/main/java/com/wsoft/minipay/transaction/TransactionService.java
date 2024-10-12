package com.wsoft.minipay.transaction;

import com.wsoft.minipay.authorization.AuthorizerService;
import com.wsoft.minipay.notification.NotificationService;
import com.wsoft.minipay.wallet.WalletRepository;
import com.wsoft.minipay.wallet.WalletType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final NotificationService notificationService;
    private final AuthorizerService authorizerService;

    public TransactionService(TransactionRepository repository,
                              WalletRepository walletRepo,
                              NotificationService notificationService,
                              AuthorizerService authorizerService1  ){
        this.transactionRepository = repository;
        this.walletRepository = walletRepo;
        this.notificationService = notificationService;
        this.authorizerService = authorizerService1;
    }

    @Transactional
    public Transaction create(Transaction transaction){

        validate(transaction);
        var newTransaction = transactionRepository.save(transaction);

        var walletPayer = walletRepository.findById(transaction.payer()).get();
        var walletPayee = walletRepository.findById(transaction.payee()).get();
        walletRepository.save(walletPayer.debit(transaction.value()));
        walletRepository.save(walletPayee.credit(transaction.value()));

        authorizerService.authorize(transaction);
        notificationService.notify(newTransaction);

        return newTransaction;
    }

    /*
     * A transaction is valid if:
     * - the payer is a common wallet
     * - the payer has enough balance
     * - the payer is not the payee
     */
    private void validate(Transaction transaction) {
        LOGGER.info("Validate transaction {} ...", transaction);

        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                     .map(
                             payer -> payer.type() == WalletType.COMUM.getValue() &&
                               payer.balance().compareTo(transaction.value()) >= 0 &&
                               !payer.id().equals((transaction.payee())) ? true : null)
                    .orElseThrow(() -> new InvalidTransactionException ("Invalid transaction")))
                .orElseThrow(() -> new InvalidTransactionException ("Invalid transaction"));
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }


}
