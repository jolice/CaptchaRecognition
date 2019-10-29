package io.riguron.captcha.balance;

import io.riguron.captcha.balance.financial.PositiveWithdraw;
import io.riguron.captcha.balance.financial.Deposit;
import io.riguron.captcha.balance.financial.Withdraw;
import io.riguron.captcha.balance.operation.EarnedBalanceOperation;
import io.riguron.captcha.balance.operation.RecognitionBalanceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@Transactional
public class DefaultUserBalanceManager implements UserBalanceManager {

    private static final BigDecimal PRICE_FOR_SOLVING = BigDecimal.valueOf(1.0D);

    private static final BigDecimal PRICE_PER_RECOGNITION = BigDecimal.valueOf(0.5D);

    private UserBalanceOperations userBalanceOperations;

    @Autowired
    public DefaultUserBalanceManager(UserBalanceOperations userBalanceOperations) {
        this.userBalanceOperations = userBalanceOperations;
    }

    @Override
    public void grantForSolving(int userId) {
        this.userBalanceOperations.performOperation(userId, PRICE_FOR_SOLVING, new EarnedBalanceOperation(new Deposit()));
    }

    @Override
    public void withdrawForRecognition(int userId) {
        this.userBalanceOperations.performOperation(userId, PRICE_PER_RECOGNITION, new RecognitionBalanceOperation(new PositiveWithdraw(new Withdraw())));
    }


}
