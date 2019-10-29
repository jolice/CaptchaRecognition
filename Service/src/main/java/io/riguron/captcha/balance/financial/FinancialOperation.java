package io.riguron.captcha.balance.financial;

import java.math.BigDecimal;

public interface FinancialOperation {

    BigDecimal accept(BigDecimal current, BigDecimal amount);
}
