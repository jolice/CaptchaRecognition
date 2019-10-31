package io.riguron.captcha.balance;

import io.riguron.captcha.balance.operation.BalanceOperation;
import io.riguron.captcha.repository.UserBalanceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class UserBalanceOperations {

    private UserBalanceRepository userBalanceRepository;

    public UserBalanceOperations(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
    }

    @Transactional
    public void performOperation(int userId, BigDecimal amount, BalanceOperation balanceOperation) {
        balanceOperation.perform( userBalanceRepository.findByUserId(userId), amount);
    }


}
