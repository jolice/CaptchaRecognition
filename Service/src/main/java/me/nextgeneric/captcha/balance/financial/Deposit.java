package me.nextgeneric.captcha.balance.financial;

import java.math.BigDecimal;

public class Deposit implements FinancialOperation {

    @Override
    public BigDecimal accept(BigDecimal current, BigDecimal amount) {
        return current.add(amount);
    }
}
