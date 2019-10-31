package io.riguron.captcha.solve;

import io.riguron.captcha.exception.CaptchaSolvingException;
import io.riguron.captcha.repository.CompleteRecognitionRepository;
import io.riguron.captcha.transaction.Procedure;
import io.riguron.captcha.user.CompleteRecognition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.OptimisticLockException;
import java.util.Optional;

@Slf4j
@Component
public class CaptchaSolvingService {

    private TransactionTemplate transactionTemplate;
    private CompleteRecognitionRepository completeRecognitionRepository;
    private CompleteRecognitionService completeRecognitionService;
    private CaptchaImmediateSolver captchaImmediateSolver;

    @Autowired
    public CaptchaSolvingService(TransactionTemplate transactionTemplate, CompleteRecognitionRepository completeRecognitionRepository, CompleteRecognitionService completeRecognitionService, CaptchaImmediateSolver captchaImmediateSolver) {
        this.transactionTemplate = transactionTemplate;
        this.completeRecognitionRepository = completeRecognitionRepository;
        this.completeRecognitionService = completeRecognitionService;
        this.captchaImmediateSolver = captchaImmediateSolver;
    }

    public void solveCaptcha(CaptchaSolution captchaSolution) {
        try {
            transactionTemplate.execute(new Procedure(() -> {
                Optional<CompleteRecognition> completeRecognition = completeRecognitionRepository.findByUserId(captchaSolution.getSolverId());
                if (completeRecognition.isPresent()) {
                    this.completeRecognitionService.proceedCompleteRecognition(captchaSolution, completeRecognition.get());
                } else {
                    this.captchaImmediateSolver.solveImmediately(captchaSolution);
                }
            }));
        } catch (OptimisticLockException e) {
            throw new CaptchaSolvingException("Ran out of time or captcha has been modified by another transaction");
        }
    }


}
