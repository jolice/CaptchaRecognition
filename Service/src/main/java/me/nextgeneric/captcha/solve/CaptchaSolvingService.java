package me.nextgeneric.captcha.solve;

import lombok.extern.slf4j.Slf4j;
import me.nextgeneric.captcha.CaptchaStatus;
import me.nextgeneric.captcha.exception.CaptchaNotFoundException;
import me.nextgeneric.captcha.exception.CaptchaSolvingException;
import me.nextgeneric.captcha.exception.InactiveCaptchaException;
import me.nextgeneric.captcha.exception.InvalidSolverException;
import me.nextgeneric.captcha.repository.CaptchaRepository;
import me.nextgeneric.captcha.repository.UserProfileRepository;
import me.nextgeneric.captcha.transaction.Procedure;
import me.nextgeneric.captcha.type.Captcha;
import me.nextgeneric.captcha.user.CompleteRecognition;
import me.nextgeneric.captcha.user.UserNotFoundException;
import me.nextgeneric.captcha.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.OptimisticLockException;

@Slf4j
@Component
public class CaptchaSolvingService {

    private TransactionTemplate transactionTemplate;
    private UserProfileRepository userProfileRepository;
    private CompleteRecognitionService completeRecognitionService;
    private CaptchaImmediateSolver captchaImmediateSolver;

    @Autowired
    public CaptchaSolvingService(TransactionTemplate transactionTemplate, UserProfileRepository userProfileRepository, CompleteRecognitionService completeRecognitionService, CaptchaImmediateSolver captchaImmediateSolver) {
        this.transactionTemplate = transactionTemplate;
        this.userProfileRepository = userProfileRepository;
        this.completeRecognitionService = completeRecognitionService;
        this.captchaImmediateSolver = captchaImmediateSolver;
    }

    public void solveCaptcha(CaptchaSolution captchaSolution) {
        try {
            transactionTemplate.execute(new Procedure(() -> {

                UserProfile userProfile = userProfileRepository.findById(captchaSolution.getSolverId()).orElseThrow(UserNotFoundException::new);
                CompleteRecognition completeRecognition = userProfile.getCompleteRecognition();

                if (completeRecognition.isEnabled()) {
                    this.completeRecognitionService.proceedCompleteRecognition(captchaSolution, completeRecognition);
                } else {
                    this.captchaImmediateSolver.solveImmediately(captchaSolution);
                }
            }));
        } catch (OptimisticLockException e) {
            throw new CaptchaSolvingException("Ran out of time or captcha has been modified by another transaction");
        }
    }


}
