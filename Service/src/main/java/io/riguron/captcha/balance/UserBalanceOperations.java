package io.riguron.captcha.balance;

import io.riguron.captcha.balance.operation.BalanceOperation;
import io.riguron.captcha.repository.UserProfileRepository;
import io.riguron.captcha.user.UserBalance;
import io.riguron.captcha.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class UserBalanceOperations {

    private UserProfileRepository userProfileRepository;

    @Autowired
    public UserBalanceOperations(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public void performOperation(int userId, BigDecimal amount, BalanceOperation balanceOperation) {
        UserProfile userProfile = userProfileRepository.retrieveForUpdate(userId);
        UserBalance userBalance = userProfile.getUserBalance();
        balanceOperation.perform(userBalance, amount);
    }



}
