package io.riguron.captcha.solve;

import io.riguron.captcha.CaptchaStatus;
import io.riguron.captcha.balance.UserBalanceManager;
import io.riguron.captcha.callback.SolvingCallback;
import io.riguron.captcha.type.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SuccessfulSolving {

    private SolvingCallback solvingCallback;
    private UserBalanceManager userBalanceManager;

    @Autowired
    public SuccessfulSolving(SolvingCallback solvingCallback, UserBalanceManager userBalanceManager) {
        this.solvingCallback = solvingCallback;
        this.userBalanceManager = userBalanceManager;
    }

    public void successfulRecognition(Captcha captchaToBeSolved, String solution) {
        this.makePayments(captchaToBeSolved.getSolvers(), captchaToBeSolved.getOriginatorId());
        captchaToBeSolved.setCaptchaStatus(CaptchaStatus.SOLVED);
        captchaToBeSolved.setFinalSolution(solution);
        this.solvingCallback.captchaSolved(captchaToBeSolved);
    }

    public void makePayments(Set<Integer> solvers, int originatorId) {
        solvers.forEach(solverId -> {
            userBalanceManager.grantForSolving(solverId);
            userBalanceManager.withdrawForRecognition(originatorId);
        });
    }

}
