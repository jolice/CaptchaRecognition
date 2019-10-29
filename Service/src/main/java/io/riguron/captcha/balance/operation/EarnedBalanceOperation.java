package io.riguron.captcha.balance.operation;

import io.riguron.captcha.user.UserBalance;
import io.riguron.captcha.balance.financial.FinancialOperation;

import java.math.BigDecimal;

public class EarnedBalanceOperation implements BalanceOperation {

    private FinancialOperation financialOperation;

    public EarnedBalanceOperation(FinancialOperation financialOperation) {
        this.financialOperation = financialOperation;
    }

    @Override
    public void perform(UserBalance userBalance, BigDecimal amount) {
        userBalance.setEarnedBalance(financialOperation.accept(userBalance.getEarnedBalance(), amount));
    }
}
