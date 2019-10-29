package io.riguron.captcha.solve;

import lombok.extern.slf4j.Slf4j;
import io.riguron.captcha.exception.CaptchaSolvingException;
import io.riguron.captcha.repository.UserProfileRepository;
import io.riguron.captcha.transaction.Procedure;
import io.riguron.captcha.user.CompleteRecognition;
import io.riguron.captcha.user.UserNotFoundException;
import io.riguron.captcha.user.UserProfile;
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
