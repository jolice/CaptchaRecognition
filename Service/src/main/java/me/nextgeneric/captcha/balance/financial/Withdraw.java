package me.nextgeneric.captcha.balance.financial;

import java.math.BigDecimal;

public class Withdraw implements FinancialOperation {

    @Override
    public BigDecimal accept(BigDecimal current, BigDecimal amount) {
        return current.subtract(amount);
    }
}
