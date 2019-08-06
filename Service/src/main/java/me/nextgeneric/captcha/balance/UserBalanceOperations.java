package me.nextgeneric.captcha.balance;

import me.nextgeneric.captcha.balance.operation.BalanceOperation;
import me.nextgeneric.captcha.repository.UserProfileRepository;
import me.nextgeneric.captcha.user.UserBalance;
import me.nextgeneric.captcha.user.UserProfile;
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
