package me.nextgeneric.captcha.balance;

public interface UserBalanceManager {

    void grantForSolving(int userId);

    void withdrawForRecognition(int userId);
}
