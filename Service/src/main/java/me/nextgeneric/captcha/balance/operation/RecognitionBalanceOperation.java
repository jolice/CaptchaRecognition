package me.nextgeneric.captcha.balance.operation;

import me.nextgeneric.captcha.balance.financial.FinancialOperation;
import me.nextgeneric.captcha.user.UserBalance;

import java.math.BigDecimal;

public class RecognitionBalanceOperation implements BalanceOperation {

    private FinancialOperation financialOperation;

    public RecognitionBalanceOperation(FinancialOperation financialOperation) {
        this.financialOperation = financialOperation;
    }

    @Override
    public void perform(UserBalance userBalance, BigDecimal amount) {
        userBalance.setRecognitionBalance(financialOperation.accept(userBalance.getRecognitionBalance(), amount));
    }
}
