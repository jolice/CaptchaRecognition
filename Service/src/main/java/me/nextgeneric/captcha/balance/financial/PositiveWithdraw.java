package me.nextgeneric.captcha.balance.financial;

import java.math.BigDecimal;

public class PositiveWithdraw implements FinancialOperation {

    private Withdraw delegate;

    public PositiveWithdraw(Withdraw delegate) {
        this.delegate = delegate;
    }

    @Override
    public BigDecimal accept(BigDecimal current, BigDecimal amount) {
        BigDecimal withdraw = delegate.accept(current, amount);
        if (withdraw.signum() == 0) {
            throw new InsufficientFundsException();
        }
        return withdraw;
    }
}
