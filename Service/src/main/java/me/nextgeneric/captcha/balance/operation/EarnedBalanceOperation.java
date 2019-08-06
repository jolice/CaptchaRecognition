package me.nextgeneric.captcha.balance.operation;

import me.nextgeneric.captcha.balance.financial.FinancialOperation;
import me.nextgeneric.captcha.user.UserBalance;

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
