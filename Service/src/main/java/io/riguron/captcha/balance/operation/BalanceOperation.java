package io.riguron.captcha.balance.operation;

import io.riguron.captcha.user.UserBalance;

import java.math.BigDecimal;

public interface BalanceOperation {

    void perform(UserBalance userBalance, BigDecimal amount);
}
