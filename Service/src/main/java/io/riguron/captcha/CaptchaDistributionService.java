package io.riguron.captcha;

import io.riguron.captcha.exception.CaptchaNotFoundException;
import io.riguron.captcha.exception.IllegalCaptchaStateException;
import io.riguron.captcha.options.CaptchaOptions;
import io.riguron.captcha.queue.IdentifierQueue;
import io.riguron.captcha.repository.CaptchaRepository;
import io.riguron.captcha.transaction.Procedure;
import lombok.extern.slf4j.Slf4j;
import io.riguron.captcha.type.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.OptimisticLockException;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CaptchaDistributionService {

    private CaptchaOptions captchaOptions;
    private ScheduledExecutorService executorService;
    private CaptchaRepository captchaRepository;
    private IdentifierQueue identifierQueue;
    private TransactionTemplate transactionTemplate;

    @Autowired
    public CaptchaDistributionService(CaptchaOptions captchaOptions, ScheduledExecutorService executorService, CaptchaRepository captchaRepository, IdentifierQueue identifierQueue, TransactionTemplate transactionTemplate) {
        this.captchaOptions = captchaOptions;
        this.executorService = executorService;
        this.captchaRepository = captchaRepository;
        this.identifierQueue = identifierQueue;
        this.transactionTemplate = transactionTemplate;
    }

    public Optional<Captcha> takeNextCaptcha(final int solverId) {
        Optional<Integer> nextCaptchaIdOptional = identifierQueue.pollFirst();
        if (nextCaptchaIdOptional.isPresent()) {
            try {
                return this.postDistribution(nextCaptchaIdOptional.get(), solverId);
            } catch (OptimisticLockException exp) {
                log.error("Failed to to assign next captcha due to concurrent modification", exp);
            }
        }
        return Optional.empty();
    }

    private Optional<Captcha> postDistribution(final int captchaId, final int solverId) {

        Optional<Captcha> result = transactionTemplate.execute(status -> {

            Captcha freshCaptcha = loadCaptchaWithSolvers(captchaId);
            this.validate(freshCaptcha);

            if (freshCaptcha.hasNotSolver(captchaId)) {
                return Optional.empty();
            }

            freshCaptcha.addSolver(solverId);
            return Optional.of(freshCaptcha);

        });

        if (!result.isPresent()) {
            identifierQueue.pushFront(captchaId);
        } else {
            this.schedulePostChecks(captchaId, solverId);
        }

        return result;
    }

    private void validate(Captcha captcha) {
        if (captcha.getCaptchaStatus() != CaptchaStatus.PROCESSING) {
            throw new IllegalCaptchaStateException();
        }
    }

    private void schedulePostChecks(final int captchaId, int solverId) {
        executorService.schedule(() -> {
            transactionTemplate.execute(new Procedure(() -> {
                try {
                    if (transactionTemplate.execute(status -> requeueAfterExpiration(captchaId, solverId))) {
                        identifierQueue.pushFront(captchaId);
                    }
                } catch (OptimisticLockException exp) {
                    log.error("Attempted to Re-Queue captcha, but solution has been accepted during the transaction");
                }
            }));
        }, captchaOptions.getTimeForSolving(), TimeUnit.SECONDS);
    }

    private boolean requeueAfterExpiration(int captchaId, int solverId) {
        Captcha captcha = loadCaptchaWithSolvers(captchaId);
        if (!captcha.hasSolved(solverId)) {
            captcha.removeSolver(solverId);
            if (captcha.getTimeoutAttempts() < captchaOptions.getRetriesAfterSolveTimeout()) {
                captcha.setTimeoutAttempts(captcha.getTimeoutAttempts() + 1);
                return true;
            } else {
                captcha.setCaptchaStatus(CaptchaStatus.NOT_SOLVED);
            }
        }
        return false;
    }

    private Captcha loadCaptchaWithSolvers(final int captchaId) {
        return captchaRepository.findById(captchaId).orElseThrow(CaptchaNotFoundException::new);
    }

}
