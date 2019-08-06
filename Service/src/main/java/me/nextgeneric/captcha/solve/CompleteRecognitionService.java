package me.nextgeneric.captcha.solve;

import me.nextgeneric.captcha.CaptchaStatus;
import me.nextgeneric.captcha.IdentifierQueue;
import me.nextgeneric.captcha.exception.CaptchaNotFoundException;
import me.nextgeneric.captcha.repository.CaptchaRepository;
import me.nextgeneric.captcha.type.Captcha;
import me.nextgeneric.captcha.user.CompleteRecognition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CompleteRecognitionService {

    private static final int ADDITIONAL_WORKERS = 2;

    private CaptchaSolvingValidator captchaSolvingValidator;
    private SuccessfulSolving successfulSolving;
    private IdentifierQueue identifierQueue;
    private CaptchaRepository captchaRepository;

    @Autowired
    public CompleteRecognitionService(CaptchaSolvingValidator captchaSolvingValidator, SuccessfulSolving successfulSolving, IdentifierQueue identifierQueue, CaptchaRepository captchaRepository) {
        this.captchaSolvingValidator = captchaSolvingValidator;
        this.successfulSolving = successfulSolving;
        this.identifierQueue = identifierQueue;
        this.captchaRepository = captchaRepository;
    }

    public void proceedCompleteRecognition(CaptchaSolution captchaSolution, CompleteRecognition completeRecognition) {
        Captcha captchaToBeSolved = this.findAndValidate(captchaSolution);
        captchaToBeSolved.addSolution(captchaSolution.getSolution());
        this.mainStage(captchaToBeSolved, captchaToBeSolved.getAcceptedSolutions(), completeRecognition);
    }

    private void mainStage(Captcha captchaToBeSolved, int currentSolutionCount, CompleteRecognition completeRecognition) {
        if (currentSolutionCount == completeRecognition.getMinimumAttempts()) {
            this.trySolveOrForward(captchaToBeSolved, completeRecognition, currentSolutionCount);
        } else if (currentSolutionCount == completeRecognition.getMaximumAttempts()) {
            if (this.insufficientResponses(captchaToBeSolved, completeRecognition)) {
                this.successfulSolving.makePayments(captchaToBeSolved.getSolvers(), captchaToBeSolved.getOriginatorId());
                captchaToBeSolved.setCaptchaStatus(CaptchaStatus.NOT_SOLVED);
            }
        } else if (currentSolutionCount > completeRecognition.getMinimumAttempts()) {

            int difference = currentSolutionCount - completeRecognition.getMinimumAttempts();

            if (difference % ADDITIONAL_WORKERS == 0) {
                trySolveOrForward(captchaToBeSolved, completeRecognition, currentSolutionCount);
            }
        }
    }

    private void trySolveOrForward(Captcha captchaToBeSolved, CompleteRecognition completeRecognition, int currentSolutionCount) {
        if (this.insufficientResponses(captchaToBeSolved, completeRecognition)) {
            this.sendToAdditionalWorkers(currentSolutionCount, completeRecognition, captchaToBeSolved);
        }
    }

    private void sendToAdditionalWorkers(int currentSolutions, CompleteRecognition recognition, Captcha captcha) {
        for (int i = 0; i < ADDITIONAL_WORKERS && currentSolutions + i < recognition.getMaximumAttempts(); i++) {
            this.identifierQueue.pushFront(captcha.getId());
        }
    }

    private boolean insufficientResponses(Captcha captchaToBeSolved, CompleteRecognition completeRecognition) {
        return compareAnswers(captchaToBeSolved, completeRecognition).map(acceptedSolution -> {
            successfulSolving.successfulRecognition(captchaToBeSolved, acceptedSolution);
            return true;
        }).orElse(false);
    }

    private Optional<String> compareAnswers(Captcha captchaToBeSolved, CompleteRecognition completeRecognition) {
        Collection<String> currentSolutions = captchaToBeSolved.getSolutions();
        Map<String, Integer> solutionMapping = new HashMap<>();
        for (String solution : currentSolutions) {
            int solutionCount = solutionMapping.merge(solution, 1, Integer::sum);
            if (solutionCount == completeRecognition.getMinimumMatches()) {
                return Optional.of(solution);
            }
        }
        return Optional.empty();
    }

    private Captcha findAndValidate(CaptchaSolution captchaSolution) {
        Captcha captchaToBeSolved = captchaRepository.findByIdWithSolutions(captchaSolution.getCaptchaId()).orElseThrow(CaptchaNotFoundException::new);
        this.captchaSolvingValidator.validateBeforeAcceptingSolution(captchaToBeSolved, captchaSolution.getSolverId());
        return captchaToBeSolved;
    }

}
