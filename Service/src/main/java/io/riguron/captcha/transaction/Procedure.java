package io.riguron.captcha.transaction;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

public class Procedure implements TransactionCallback<Void> {

    private Runnable operation;

    public Procedure(Runnable operation) {
        this.operation = operation;
    }

    @Override
    public Void doInTransaction(TransactionStatus status) {
        operation.run();
        return null;
    }
}
