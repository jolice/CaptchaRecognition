package me.nextgeneric.captcha.balance.operation;

import me.nextgeneric.captcha.user.UserBalance;

import java.math.BigDecimal;

public interface BalanceOperation {

    void perform(UserBalance userBalance, BigDecimal amount);
}
