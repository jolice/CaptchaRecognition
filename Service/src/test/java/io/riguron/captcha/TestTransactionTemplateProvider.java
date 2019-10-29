package io.riguron.captcha;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestTransactionTemplateProvider {

    public TransactionTemplate transactionTemplate() {
        TransactionTemplate transactionTemplate = mock(TransactionTemplate.class);
        when(transactionTemplate.execute(any())).thenAnswer(invocationOnMock -> {
            TransactionCallback<?> callback = invocationOnMock.getArgument(0);
            return callback.doInTransaction(mock(TransactionStatus.class));
        });
        return transactionTemplate;
    }
}
